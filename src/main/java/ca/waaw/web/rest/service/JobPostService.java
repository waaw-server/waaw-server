package ca.waaw.web.rest.service;

import ca.waaw.domain.jobpost.JobPost;
import ca.waaw.domain.organization.OrganizationHolidays;
import ca.waaw.domain.user.UserOrganization;
import ca.waaw.dto.holiday.HolidayDto;
import ca.waaw.dto.jobpost.JobPostDto;
import ca.waaw.enumration.user.Authority;
import ca.waaw.mapper.JobPostMapper;
import ca.waaw.mapper.OrganizationHolidayMapper;
import ca.waaw.repository.jobpost.JobPostRepository;
import ca.waaw.repository.locationandroles.LocationRepository;
import ca.waaw.repository.user.UserOrganizationRepository;
import ca.waaw.security.SecurityUtils;
import ca.waaw.web.rest.errors.exceptions.AuthenticationException;
import ca.waaw.web.rest.errors.exceptions.EntityNotFoundException;
import ca.waaw.web.rest.errors.exceptions.UnauthorizedException;
import ca.waaw.web.rest.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobPostService {

    private final JobPostRepository jobPostRepository;

    private final LocationRepository locationRepository;

    private final UserOrganizationRepository userOrganizationRepository;

    public List<JobPostDto> getAllHoliday() {
        UserOrganization userDetails = SecurityUtils.getCurrentUserLogin()
                .flatMap(username -> userOrganizationRepository.findOneByUsernameAndDeleteFlag(username, false))
                .orElseThrow(AuthenticationException::new);

        Set<JobPost> jobPostSet = null;

        if (StringUtils.isNotEmpty(userDetails.getLocationId())) {
            jobPostSet = new HashSet<>(jobPostRepository.getAllForLocation(userDetails.getLocationId()));
        }

        assert jobPostSet != null;

        return jobPostSet.stream().map(JobPostMapper::entityToDto).collect(Collectors.toList());
    }

    public void addJobPost(JobPostDto jobPostDto) {
        CommonUtils.checkRoleAuthorization(Authority.ADMIN, Authority.MANAGER);
        SecurityUtils.getCurrentUserLogin()
                .flatMap(username -> userOrganizationRepository.findOneByUsernameAndDeleteFlag(username, false))
                .map(user -> {
                            AtomicReference<String> timezone = new AtomicReference<>();
                            if (SecurityUtils.isCurrentUserInRole(Authority.MANAGER)) {
                                jobPostDto.setLocationId(user.getLocationId());
                            }
                            if (StringUtils.isNotEmpty(jobPostDto.getLocationId())) {
                                locationRepository.findOneByIdAndDeleteFlag(jobPostDto.getLocationId(), false)
                                        .map(location -> {
                                            if (!user.getOrganizationId().equals(location.getOrganizationId())) {
                                                throw new UnauthorizedException();
                                            }
                                            timezone.set(location.getTimezone());
                                            return location;
                                        });
                            } else {
                                timezone.set(user.getOrganization().getTimezone());
                            }
                            // validateDate(jobPostDto, timezone.get());
                            JobPost jobPost = JobPostMapper.newDtoToEntity(jobPostDto);
                            jobPost.setCreatedBy(user.getId());
                            return jobPost;
                        }
                ).map(jobPostRepository::save)
                .map(jobPost -> CommonUtils.logMessageAndReturnObject(JobPost.class, "info",
                        JobPostService.class, "New JobPost added: {}", jobPost));
    }

    /**
     * @param jobPostDto JobPost info to be updated
     */
    public void editJobPost(JobPostDto jobPostDto) {
        CommonUtils.checkRoleAuthorization(Authority.ADMIN, Authority.MANAGER);
        SecurityUtils.getCurrentUserLogin()
                .flatMap(username -> userOrganizationRepository.findOneByUsernameAndDeleteFlag(username, false)
                        .map(user -> jobPostRepository.findOneByIdAndDeleteFlag(jobPostDto.getId(), false)
                                .map(jobPost -> {
                                    AtomicReference<String> timezone = new AtomicReference<>("");
                                    if (SecurityUtils.isCurrentUserInRole(Authority.MANAGER) &&
                                            !user.getLocationId().equals(jobPost.getLocationId())) {
                                        throw new UnauthorizedException();
                                    }

                                    // If locationId is being changed check admin authorization
                                    if (StringUtils.isNotEmpty(jobPostDto.getLocationId()) && (StringUtils.isEmpty(jobPost.getLocationId())) ||
                                            !jobPostDto.getLocationId().equals(jobPost.getLocationId())) {
                                        locationRepository.findOneByIdAndDeleteFlag(jobPostDto.getLocationId(), false)
                                                .map(location -> {
                                                    timezone.set(location.getTimezone());
                                                    if (!location.getOrganizationId().equals(user.getOrganizationId())) {
                                                        throw new UnauthorizedException();
                                                    }
                                                    return location;
                                                })
                                                .orElseThrow(() -> new EntityNotFoundException("location"));
                                    } else {
                                        timezone.set(user.getOrganization().getTimezone());
                                    }
                                    // validateDate(holidayDto, timezone.get());
                                    return jobPost;
                                })
                                .map(jobPost -> {
                                    JobPostMapper.updateDtoToEntity(jobPostDto, jobPost);
                                    jobPost.setLastModifiedBy(user.getId());
                                    return jobPost;
                                })
                                .orElseThrow(() -> new EntityNotFoundException("jobPost"))
                        )
                        .map(jobPostRepository::save)
                        .map(jobPost -> CommonUtils.logMessageAndReturnObject(JobPost.class, "info",
                                JobPostService.class, "JobPost updated: {}", jobPost))
                );
    }

    public void deleteJobPost(String jobPostId) {
        CommonUtils.checkRoleAuthorization(Authority.ADMIN, Authority.MANAGER);
        SecurityUtils.getCurrentUserLogin()
                .flatMap(username -> userOrganizationRepository.findOneByUsernameAndDeleteFlag(username, false))
                .map(user -> jobPostRepository.findOneByIdAndDeleteFlag(jobPostId, false)
                        .map(jobPost -> {
                            if (SecurityUtils.isCurrentUserInRole(Authority.MANAGER) && !jobPost.getLocationId().equals(user.getLocationId())) {
                                throw new UnauthorizedException();
                            }
                            jobPost.setDeleteFlag(true);
                            jobPost.setLastModifiedBy(user.getId());
                            System.out.println("3");
                            System.out.println(jobPost);
                            return jobPostRepository.save(jobPost);
                        })
                        .map(jobPost -> CommonUtils.logMessageAndReturnObject(JobPost.class, "info",
                                JobPostService.class, "JobPost deleted successfully: {}", jobPost))
                        .orElseThrow(() -> new EntityNotFoundException("jobPost"))
                );
    }
}
