package job_hunter.hct_14.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.entity.response.SkillResponsetory.ResCreateSkillsDTO;
import job_hunter.hct_14.service.SkillService;
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
public class SkilsController {
    @Autowired
    private final SkillService Skillservice;

    public SkilsController(SkillService skillservice) {
        Skillservice = skillservice;
    }

    @PostMapping("/skills")
    public ResponseEntity<ResCreateSkillsDTO> handleSkills(@Valid @RequestBody Skills skills) throws IdInvaldException {
        // Kiểm tra xem tên kỹ năng đã tồn tại chưa
        boolean skillExists = this.Skillservice.findSkillByName(skills.getName());

        if (skillExists) {
            throw new IdInvaldException("Skill name = " + skills.getName() + " đã tồn tại");
        }

        // Nếu không có, tiến hành tạo mới kỹ năng
        Skills createSkill = this.Skillservice.handleCreate(skills);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.Skillservice.convertCreateSkillDTO(createSkill));
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
    @GetMapping("skill/{id}")
    @ApiMessage("get api success")
    public ResponseEntity<Skills> getSkills(@PathVariable int id, Skills skills)throws IdInvaldException {
        Skills skillsCheck = this.Skillservice.findByid(id);
        if (skillsCheck != null){
            throw new IdInvaldException("không có skill này em ơi");
        }
        return ResponseEntity.status(HttpStatus.OK).body(skillsCheck);
    }
    @GetMapping("skills")
    @ApiMessage("get all skills")
    public ResponseEntity<ResultPaginationDTO> findAllSkills(@Filter Specification<Skills> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.Skillservice.findbyAllSkills(spec, pageable));

    }
}
