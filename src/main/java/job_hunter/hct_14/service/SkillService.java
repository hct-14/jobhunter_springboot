package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobCreateDTO;
import job_hunter.hct_14.entity.response.ResUpdateUserDTO;
import job_hunter.hct_14.entity.response.ResUserDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.entity.response.SkillResponsetory.ResCreateSkillsDTO;
import job_hunter.hct_14.entity.response.SkillResponsetory.ResSkillDTO;
import job_hunter.hct_14.entity.response.SkillResponsetory.ResUpdateSkillDTO;
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

    public boolean findSkillByName(String name) {
        return this.skillReponsetory.existsByName(name);

    }
    public Skills handleCreate(Skills skills) {
//        Boolean optionalSkills = skillReponsetory.findByName(skills.getName());
//        System.out.println(optionalSkills);
        return this.skillReponsetory.save(skills);
    }
    public Skills findByid(int id){
        Optional<Skills> skillsCheck = skillReponsetory.findById(id);
        if (skillsCheck.isPresent()){
            return skillsCheck.get();
        }
        return null;
    }
    public void deleteSkill(int id) {
        // delete job (inside job_skill table)
        Optional<Skills> skillOptional = this.skillReponsetory.findById(id);
        Skills currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete skill
        this.skillReponsetory.delete(currentSkill);
    }
//    public void

//    public ResultPaginationDTO findbyAll(Specification<Skills> spec, Pageable pageable){
//        Page<Skills> pageSkils = this.skillReponsetory.findAll(spec, pageable);
//        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
//        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
//        mt.setPage(pageable.getPageNumber() + 1);
//        mt.setPageSize(pageable.getPageSize());
//        mt.setPages(pageSkils.getTotalPages());
//        mt.setTotal(pageSkils.getTotalElements());
//
//        resultPaginationDTO.setMeta(mt);
//        List<ResSkillDTO> listSkilss = pageSkils.getContent()
//                .stream().map(item -> new ResSkillDTO(
//                        item.getId(),
//                        item.getName(),
//
//                        item.getCreatedAt(),
//                        item.getUpdatedAt(),
//                        item.getCreatedBy(),
//                        item.getUpdatedBy()
////                        item.getJobs() != null ? new ResSkillDTO().getJobSkills(
////                                item.getJobs().getId(),
////                                item.getJobs().getName()
////                        ) : null
//                ))
//                .collect(Collectors.toList());
//        resultPaginationDTO.setResult(listSkilss);
//        return resultPaginationDTO;
//
//    }
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
    public ResultPaginationDTO findbyAllSkills(Specification<Skills> spec, Pageable pageable){
        Page<Skills> pageSkill = this.skillReponsetory.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalPages());

        rs.setMeta(mt);

//        DOMKeyInfo pageSkill;
        List<ResSkillDTO> listSkill = pageSkill.getContent()

                .stream()
                .map(item -> new ResSkillDTO(
                        item.getId(),
                        item.getName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy()

                ))
                .collect(Collectors.toList());


        rs.setResult(listSkill);

        return rs;
    }
    public ResCreateSkillsDTO convertCreateSkillDTO(Skills skills){
        // check skills

        Skills currentJob = this.skillReponsetory.save(skills);

        ResCreateSkillsDTO res = new ResCreateSkillsDTO();

        res.setId(skills.getId());
        res.setName(skills.getName());

        res.setCreatedAt(skills.getCreatedAt());
        res.setUpdatedAt(skills.getUpdatedAt());
        res.setCreatedBy(skills.getCreatedBy());
        res.setUpdatedBy(skills.getUpdatedBy());


        return  res;
    }
    public ResUpdateSkillDTO converUpdateSkillDTO(Skills skills){
        ResUpdateSkillDTO res = new ResUpdateSkillDTO();
        res.setId(skills.getId());
        res.setName(skills.getName());

        res.setCreatedAt(skills.getCreatedAt());
        res.setUpdatedAt(skills.getUpdatedAt());
        res.setCreatedBy(skills.getCreatedBy());
        res.setUpdatedBy(skills.getUpdatedBy());
        return  res;
    }
}
