package ca.waaw.domain.jobpost;

import ca.waaw.domain.AbstractEntity;
import ca.waaw.enumration.JobPostStatus;
import ca.waaw.web.rest.utils.customannotations.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "job_post")
public class JobPost extends AbstractEntity {

    @Column(name="job_post_id")
    private String jobPostId;

    @Column
    private String jobTitle;

    @Column
    private String description;

    @Column
    private String requiredSkills;

    @Column(name="location_id")
    private String locationId;

    @Column
    private String salaryRange;

    @Column
    private String applicationDeadline;

    @Enumerated(EnumType.STRING)
    private JobPostStatus status;

    @Column
    private String createdOn;

    @Column
    private String updatedOn;

}
