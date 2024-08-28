package ca.waaw.web.rest;

import ca.waaw.dto.holiday.HolidayDto;
import ca.waaw.dto.jobpost.JobPostDto;
import ca.waaw.web.rest.service.JobPostService;
import ca.waaw.web.rest.utils.customannotations.swagger.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class JobPostController {

    private final JobPostService jobPostService;

    @SwaggerAuthenticated
    @SwaggerUnauthorized
    @Operation(description = "${api.description.jobpost.getAllJobPosts")
    @GetMapping("/v1/organization/jobpost/getall")
    @ApiResponse(responseCode = "200", description = "${api.swagger.schema-description.getAllJobPosts}", content =
            {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = JobPostDto.class)))})
    public ResponseEntity<List<JobPostDto>> getAllJobPosts(@RequestParam(required = false) Integer Year) {
        try{
            return ResponseEntity.ok(jobPostService.getAllHoliday());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SwaggerCreated
    @SwaggerNotFound
    @SwaggerBadRequest
    @SwaggerUnauthorized
    @SwaggerAuthenticated
    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(description = "${api.description.jobpost.addJobPost}")
//    @PostMapping("${api.endpoints.organization.addHoliday}")
    @PostMapping("/v1/organization/jobpost/add")
    public void addJobPost(@Valid @RequestBody JobPostDto jobPostDto) {
        jobPostService.addJobPost(jobPostDto);
    }

    @SwaggerOk
    @SwaggerNotFound
    @SwaggerBadRequest
    @SwaggerUnauthorized
    @SwaggerAuthenticated
    @ResponseStatus(HttpStatus.OK)
//    @Operation(description = "${api.description.organization.editJobPost}")
    @PutMapping("/v1/organization/jobpost/edit")
    public void editJobPost(@RequestBody JobPostDto jobPostDto) {
        jobPostService.editJobPost(jobPostDto);
    }

    @SwaggerOk
    @SwaggerBadRequest
    @ResponseStatus(HttpStatus.OK)
    @SwaggerAuthenticated
    @SwaggerUnauthorized
    @SwaggerNotFound
//    @Operation(description = "${api.description.organization.deleteJobPost}")
    @DeleteMapping("/v1/organization/jobpost/delete")
    public void deleteJobPost(@RequestBody String jobPostId) {
        jobPostService.deleteJobPost(jobPostId);
    }

}
