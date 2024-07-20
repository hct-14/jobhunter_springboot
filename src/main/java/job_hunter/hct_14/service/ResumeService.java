package job_hunter.hct_14.service;


import job_hunter.hct_14.entity.Resume;
import job_hunter.hct_14.entity.response.ResResumeponsetory.ResResumeDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.repository.ResumeReponsetory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeReponsetory resumeReponsetory;

    public ResumeService(ResumeReponsetory resumeReponsetory) {
        this.resumeReponsetory = resumeReponsetory;
    }

    public Resume handleCreateResume(Resume resume) {
        return this.resumeReponsetory.save(resume);
    }

    public Resume findByIdResume(int id) {
        Optional<Resume> resume = this.resumeReponsetory.findById(id);
        if (resume.isPresent()) {
            return resume.get();
        }
        return null;
    }
        public ResultPaginationDTO findAllResume(Specification<Resume> spec, Pageable pageable){

        Page<Resume> page = this.resumeReponsetory.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        rs.setMeta(mt);

        List<ResResumeDTO> listResume = page.getContent().stream().map(item -> new ResResumeDTO(
                item.getId(),
                item.getEmail(),
                item.getUrl(),
                item.getStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getCreatedBy(),
                item.getUpdatedBy(),
//                item..
                item.getUser() != null ? new ResResumeDTO.UserResume(
                        item.getUser().getId(),
                        item.getUser().getName()
                ) : null,
                item.getJob() != null ? new ResResumeDTO.JobResume(
                        item.getJob().getId(),
                        item.getJob().getName()
                ) : null

        )).collect(Collectors.toList());

        rs.setResult(listResume);
        return rs;
    }




    public Resume handleUpdateResume(Resume resume) {
        Resume checkResume = this.findByIdResume(resume.getId());
        if (checkResume != null) {
            checkResume.getId();
            checkResume.setStatus(resume.getStatus());
            checkResume = this.resumeReponsetory.save(checkResume);
            return checkResume;
        }
        return null;
    }
    public void handleDeleteResume(int id) {
        this.resumeReponsetory.deleteById(id);


    }
    public ResResumeDTO convertResumeToResumeDTO(Resume resume) {
        ResResumeDTO resumeDTO = new ResResumeDTO();
        resumeDTO.setId(resume.getId());
        resumeDTO.setEmail(resume.getEmail());
        resumeDTO.setUrl(resume.getUrl());
        resumeDTO.setStatus(resume.getStatus());
        resumeDTO.setCreatedAt(resume.getCreatedAt());
        resumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resumeDTO.setCreatedBy(resume.getCreatedBy());
        resumeDTO.setUpdatedBy(resume.getUpdatedBy());
        ResResumeDTO.JobResume jobResume = new ResResumeDTO.JobResume();

       ResResumeDTO.UserResume userResume = new ResResumeDTO.UserResume();
       if (resume.getUser() != null){
           userResume.setId(resume.getUser().getId());
           userResume.setName(resume.getUser().getName());
           resumeDTO.setUserResume(userResume);
       }

//       ResResumeDTO.JobResume jobResume = new ResResumeDTO.JobResume();
       if (jobResume != null){
           jobResume.setId(resume.getJob().getId());
           jobResume.setName(resume.getJob().getName());
           resumeDTO.setJobResume(jobResume);
       }
       return resumeDTO;
    }

}
