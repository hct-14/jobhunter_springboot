package job_hunter.hct_14.controller;

import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.service.SkillService;
import job_hunter.hct_14.util.annotation.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkilsController {
    @Autowired
    private final SkillService Skillservice;

    public SkilsController(SkillService skillservice) {
        Skillservice = skillservice;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skills> handleSkills(@RequestBody Skills skills) {
        Skills createSkilss = this.Skillservice.handleCreate(skills);
        return ResponseEntity.status(HttpStatus.OK).body(createSkilss);
    }
    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> findByAllSkills (Specification<Skills> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.Skillservice.findbyAll(spec, pageable));
    }
    @PutMapping("/skills")
    @ApiMessage("call call")
    public ResponseEntity<Skills> updateSkills(@RequestBody Skills skills){
        Skills skillsCheck = this.Skillservice.handleUpdate(skills);
//        Job jobCheck = this.jobService.handleUpdate(job);

        if (skillsCheck != null){
            return ResponseEntity.status(HttpStatus.OK).body(this.Skillservice.handleUpdate(skillsCheck));
        }
        return null;
    }
}
