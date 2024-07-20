package job_hunter.hct_14.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobCreateDTO;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobUpdateDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.service.JobService;
import job_hunter.hct_14.util.annotation.ApiMessage;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobsController {
    @Autowired
    private final JobService jobService;
    public JobsController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("jobs")
    public ResponseEntity<ResJobCreateDTO> Createjob(@RequestBody Job job){
        Job savedJob = this.jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.convertCreateJobDTO(savedJob));
    }
    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResJobUpdateDTO> update(@Valid @RequestBody Job job) throws IdInvaldException {
        Optional<Job> currentJob = this.jobService.findById(job.getId());
        if (currentJob.isEmpty()) {
            throw new IdInvaldException("Job not found");
        }

        return ResponseEntity.ok()
                .body(this.jobService.update(job, currentJob.get()));
    }
//    @GetMapping("jobs")
//    public ResponseEntity<List<Job>> getAllJobs() {
//        List<Job> jobs = jobService.findByAll();
//        return ResponseEntity.ok(jobs);
//    }
    @GetMapping("/jobs")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllJob(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.findByAllJobs(spec, pageable));
    }
    @GetMapping("/jobs/{id}")
    @ApiMessage("fetch all users")
    public ResponseEntity<Optional<Job>> getJobByid(Job job, @PathVariable int id) throws IdInvaldException {
        Optional<Job> currentJob = this.jobService.findById(id);
        if (!currentJob.isPresent()){
            throw new IdInvaldException("job này khoogn tồn tại emo ơi");
        }
        return ResponseEntity.status(HttpStatus.OK).body(currentJob);

    }
    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<String> delete(@PathVariable("id") int id) throws IdInvaldException {
        Optional<Job> currentJob = this.jobService.findById(id);
        if (currentJob.isEmpty()) {
            throw new IdInvaldException("Job not found");
        }
        this.jobService.delete(id);
        return ResponseEntity.ok().body("xoa oke");
    }




}
