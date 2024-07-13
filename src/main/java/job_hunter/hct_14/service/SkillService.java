package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobDTO;
import job_hunter.hct_14.entity.response.ResUserDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.entity.response.SkillResponsetory.ResSkillDTO;
import job_hunter.hct_14.repository.SkillReponsetory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillService {
    private final SkillReponsetory skillReponsetory;

    public SkillService(SkillReponsetory skillReponsetory) {
        this.skillReponsetory = skillReponsetory;
    }

    public Skills handleCreate(Skills skills) {
        return this.skillReponsetory.save(skills);
    }
    public Skills findByid(int id){
        Optional<Skills> skillsCheck = skillReponsetory.findById(id);
        if (skillsCheck.isPresent()){
            return skillsCheck.get();
        }
        return null;
    }
    public ResultPaginationDTO findbyAll(Specification<Skills> spec, Pageable pageable){
        Page<Skills> pageSkils = this.skillReponsetory.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkils.getTotalPages());
        mt.setTotal(pageSkils.getTotalElements());

        resultPaginationDTO.setMeta(mt);
        List<ResSkillDTO> listSkilss = pageSkils.getContent()
                .stream().map(item -> new ResSkillDTO(
                        item.getId(),
                        item.getName(),

//                        item.isActive(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy()
//                        item.getJobs() != null ? new ResSkillDTO().getJobSkills(
//                                item.getJobs().getId(),
//                                item.getJobs().getName()
//                        ) : null
                ))
                .collect(Collectors.toList());
        resultPaginationDTO.setResult(listSkilss);
        return resultPaginationDTO;

    }
    public Skills handleUpdate(Skills skills) {
        Skills skillsCheck = this.findByid(skills.getId());

        if (skillsCheck != null) {
            skillsCheck.setName(skills.getName());
            // Thêm các trường khác mà bạn muốn cập nhật nếu cần
            skillsCheck = this.skillReponsetory.save(skillsCheck);
            return skillsCheck;
        }

        return null;
    }
}
