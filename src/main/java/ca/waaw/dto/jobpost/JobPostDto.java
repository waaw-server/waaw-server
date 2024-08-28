package ca.waaw.dto.jobpost;

import ca.waaw.enumration.IsStatPay;
import ca.waaw.enumration.JobPostStatus;
import ca.waaw.web.rest.utils.customannotations.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobPostDto {

    private String id;

    @NotEmpty
    private String jobTitle;

    @NotEmpty
    private String description;

    @NotEmpty
    private String requiredSkills;

    private String locationId;

    private String salaryRange;

    private String applicationDeadline;

    @Schema(allowableValues = {"DRAFT", "PUBLISH"})
    @ValueOfEnum(enumClass = JobPostStatus.class)
    private String status;

    private String createdOn;

    private String UpdatedOn;

}
