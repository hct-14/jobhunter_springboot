package job_hunter.hct_14.service;



import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import com.turkraft.springfilter.parser.node.FilterNode;
import job_hunter.hct_14.entity.Resume;
import job_hunter.hct_14.entity.response.ResResumeponsetory.ResFetchResumeDTO;
import job_hunter.hct_14.entity.response.ResResumeponsetory.ResResumeDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.repository.ResumeReponsetory;
import job_hunter.hct_14.util.SercuryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;
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
    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return res;
    }
    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SercuryUtil.getCurrentUserLogin().isPresent() == true
                ? SercuryUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeReponsetory.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> this.getResume(item))
                .collect(Collectors.toList());

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
