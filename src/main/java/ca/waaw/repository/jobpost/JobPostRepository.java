package ca.waaw.repository.jobpost;

import ca.waaw.domain.jobpost.JobPost;
import ca.waaw.domain.organization.OrganizationHolidays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, String> {

    @Query(value = "SELECT j FROM JobPost j WHERE j.locationId = ?1 AND " +
            "j.deleteFlag = false")
    List<JobPost> getAllForLocation(String locationId);

    Optional<JobPost> findOneByIdAndDeleteFlag(String id, boolean deleteFlag);

}
