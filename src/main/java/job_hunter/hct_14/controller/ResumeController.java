package job_hunter.hct_14.controller;

import com.turkraft.springfilter.boot.Filter;
import job_hunter.hct_14.entity.Resume;
import job_hunter.hct_14.entity.response.ResResumeponsetory.ResResumeDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.service.ResumeService;
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

public class ResumeController {
    @Autowired
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("call api create resume success")

    public ResponseEntity<ResResumeDTO> addResume(@RequestBody Resume resume) {
        Resume addResume = this.resumeService.handleCreateResume(resume);
//        Resume saveResume = this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.convertResumeToResumeDTO(addResume));
    }
    @PutMapping("resumes")
    @ApiMessage("call api update resume success")
    public ResponseEntity<ResResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvaldException {
        Resume updateResume = this.resumeService.handleUpdateResume(resume);
        if (updateResume != null) {
//           Resume updateResume = this.resumeService.handleUpdateResume(updateResume);
            return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.convertResumeToResumeDTO(updateResume));
        }
        throw new IdInvaldException("khoong cso resume nay dau em oi");
    }

    @GetMapping("resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.findAllResume(spec, pageable));

    }
    @GetMapping("resumes/{id}")
    public ResponseEntity<ResResumeDTO> getResumes(@PathVariable int id) throws IdInvaldException {
        Resume resume = this.resumeService.findByIdResume(id);
        if (resume != null) {
            return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.convertResumeToResumeDTO(resume));
        }
        throw new IdInvaldException("khong co resume nay dau ma tim em oi");
    }
    @DeleteMapping("resumes/{id}")
    public ResponseEntity<String> deleteResume(@PathVariable int id) throws IdInvaldException {
        Resume resume = this.resumeService.findByIdResume(id);
        if (resume != null) {
            this.resumeService.handleDeleteResume(id);
           return ResponseEntity.status(HttpStatus.OK).body("xoa ok");
        }
        throw new IdInvaldException("khong co resume nay dau ma xoa em oi");
    }
    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }

}
