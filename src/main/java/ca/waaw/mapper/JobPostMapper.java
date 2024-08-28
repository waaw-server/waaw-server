package ca.waaw.mapper;

import ca.waaw.domain.jobpost.JobPost;
import ca.waaw.domain.organization.OrganizationHolidays;
import ca.waaw.dto.holiday.HolidayDto;
import ca.waaw.dto.jobpost.JobPostDto;
import ca.waaw.enumration.HolidayType;
import ca.waaw.enumration.IsStatPay;
import ca.waaw.enumration.JobPostStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class JobPostMapper {

    public static JobPostDto entityToDto(JobPost source) {
        JobPostDto target = new JobPostDto();
        BeanUtils.copyProperties(source, target);
        target.setStatus(source.getStatus().toString());
        return target;
    }

    public static JobPost newDtoToEntity(JobPostDto source) {
        JobPost target = new JobPost();
        BeanUtils.copyProperties(source, target);
        if (StringUtils.isEmpty(target.getLocationId())) target.setLocationId(null);
        target.setStatus(JobPostStatus.valueOf(source.getStatus()));
        return target;
    }

    public static void updateDtoToEntity(JobPostDto source, JobPost target) {
        if (StringUtils.isNotEmpty(source.getJobTitle())) target.setJobTitle(source.getJobTitle());
        if (StringUtils.isNotEmpty(source.getDescription())) target.setDescription(source.getDescription());
        if (StringUtils.isNotEmpty(source.getRequiredSkills())) target.setRequiredSkills(source.getRequiredSkills());
        if (StringUtils.isNotEmpty(source.getSalaryRange())) target.setSalaryRange(source.getSalaryRange());
        if (StringUtils.isNotEmpty(source.getApplicationDeadline())) target.setApplicationDeadline(source.getApplicationDeadline());
        if (StringUtils.isNotEmpty(source.getStatus())) target.setStatus(JobPostStatus.valueOf(source.getStatus()));
    }

}
