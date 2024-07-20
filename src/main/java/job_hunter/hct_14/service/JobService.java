package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Company;
import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobCreateDTO;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobDTO;
import job_hunter.hct_14.entity.response.JobResponsetory.ResJobUpdateDTO;
import job_hunter.hct_14.entity.response.ResUserDTO;
import job_hunter.hct_14.entity.response.ResultPaginationDTO;
import job_hunter.hct_14.repository.CompanyRepository;
import job_hunter.hct_14.repository.JobReponsetory;
import job_hunter.hct_14.repository.SkillReponsetory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobReponsetory jobReponsetory;
    private final SkillReponsetory skillReponsetory;
    private final CompanyRepository companyRepository;
    public JobService(JobReponsetory jobReponsetory, SkillReponsetory skillReponsetory, CompanyRepository companyRepository) {
        this.jobReponsetory = jobReponsetory;
        this.skillReponsetory = skillReponsetory;
        this.companyRepository = companyRepository;
    }



    public Job handleCreateJob(Job job){
        return this.jobReponsetory.save(job);
    }

    public Optional<Job> findById(int id){
        return this.jobReponsetory.findById(id);
//        if (jobOptional.isPresent()){
//            return jobOptional.get();
////            return userOptional.get();
//
//        }
//        return null;
    }
    public ResJobUpdateDTO update(Job jobFe, Job jobInDB) {
        // Kiểm tra skills
        if (jobFe.getSkills() != null) {
            List<Integer> reqSkills = jobFe.getSkills()
                    .stream().map(skillfe -> skillfe.getId())
                    .collect(Collectors.toList());

            List<Skills> dbSkills = this.skillReponsetory.findByIdIn(reqSkills);
            jobInDB.setSkills(dbSkills);
        }

        // Kiểm tra company
        if (jobFe.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(jobFe.getCompany().getId());
            if (cOptional.isPresent()) {
                jobInDB.setCompany(cOptional.get());
            }
        }

        // Cập nhật thông tin công việc
        if (jobFe.getName() != null) {
            jobInDB.setName(jobFe.getName());
        }
        if (jobFe.getSalary() != 0.0) {
            jobInDB.setSalary(jobFe.getSalary());
        }
        if (jobFe.getQuantity() != 0) {
            jobInDB.setQuantity(jobFe.getQuantity());
        }
        if (jobFe.getLocation() != null) {
            jobInDB.setLocation(jobFe.getLocation());
        }
        if (jobFe.getLevel() != null) {
            jobInDB.setLevel(jobFe.getLevel());
        }
        if (jobFe.getStartDate() != null) {
            jobInDB.setStartDate(jobFe.getStartDate());
        }
        if (jobFe.getEndDate() != null) {
            jobInDB.setEndDate(jobFe.getEndDate());
        }
        jobInDB.setActive(jobFe.isActive());

        // Cập nhật job
        Job currentJob = this.jobReponsetory.save(jobInDB);

        // Chuyển đổi response
        ResJobUpdateDTO dto = new ResJobUpdateDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }



    public ResultPaginationDTO findByAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobReponsetory.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);

        List<ResJobDTO> listJob = pageJob.getContent()
                .stream().map(item -> new ResJobDTO(
                        item.getId(),
                        item.getName(),
                        item.getLocation(), // Assuming you have this field in Job
                        item.getSalary(),
                        item.getQuantity(),
                        item.getLevel(),
                        item.getDescription(),
                        item.getStartDate(),
                        item.getEndDate(), // Assuming you have this field in Job
                        item.isActive(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy(),
                        item.getCompany() != null ? new ResUserDTO.CompanyUser(
                                item.getCompany().getId(),
                                item.getCompany().getName()
                        ) : null
                ))
                .collect(Collectors.toList());

        rs.setResult(listJob);

        return rs;
    }


    public ResJobCreateDTO convertCreateJobDTO(Job job){
        // check skills
        if (job.getSkills() != null) {
            List<Integer> reqSkills = job.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skills> dbSkills = this.skillReponsetory.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        Job currentJob = this.jobReponsetory.save(job);

        ResJobCreateDTO res = new ResJobCreateDTO();

        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setCreatedBy(job.getCreatedBy());
        res.setUpdatedBy(job.getUpdatedBy());
        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return  res;
    }
    public void delete(int id) {
        this.jobReponsetory.deleteById(id);
    }
    public ResJobUpdateDTO convertUpdateJobDTO(Job job){
        ResJobUpdateDTO res = new ResJobUpdateDTO();

        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setCreatedBy(job.getCreatedBy());
        res.setUpdatedBy(job.getUpdatedBy());

        return  res;
    }

//    public ResultPaginationDTO findByAllJobs(Specification<User> spec, Pageable pageable) {
//    }
}
