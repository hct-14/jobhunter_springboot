package job_hunter.hct_14.controller;


import com.turkraft.springfilter.boot.Filter;
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
    @PutMapping("jobs")
    public ResponseEntity<ResJobUpdateDTO> UpdateJob(@RequestBody Job job) throws IdInvaldException {
        Job jobCheck = this.jobService.handleUpdate(job);
        if (jobCheck != null){
            Job updateJob = this.jobService.handleUpdate(jobCheck);
            return ResponseEntity.status(HttpStatus.OK).body(this.jobService.convertUpdateJobDTO(updateJob));
        }
        throw new IdInvaldException("khoogn có id nào như này đâu em ơi");

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
    public ResponseEntity<Job> getJobByid(Job job,@PathVariable int id) throws IdInvaldException {
        Job JobCheck = this.jobService.findById(id);
        if (JobCheck == null){
            throw new IdInvaldException("job này khoogn tồn tại emo ơi");
        }
        return ResponseEntity.status(HttpStatus.OK).body(JobCheck);

    }
    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<String> delete(@PathVariable("id") int id) throws IdInvaldException {
        Job currentJob = this.jobService.findById(id);
        if (currentJob==null) {
            throw new IdInvaldException("Job not found");
        }
        this.jobService.delete(id);
        return ResponseEntity.ok().body("xoa oke");
    }




}
