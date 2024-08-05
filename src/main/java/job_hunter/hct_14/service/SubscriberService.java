package job_hunter.hct_14.service;

import job_hunter.hct_14.entity.Job;
import job_hunter.hct_14.entity.Skills;
import job_hunter.hct_14.entity.Subscribers;
import job_hunter.hct_14.entity.response.SubRepomsetory.SubDTO;
import job_hunter.hct_14.entity.response.email.ResEmailJob;
import job_hunter.hct_14.repository.JobReponsetory;
import job_hunter.hct_14.repository.SkillReponsetory;
import job_hunter.hct_14.repository.SubscribersRepository;
import job_hunter.hct_14.repository.UserRepository;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

public class SubscriberService {

    private UserRepository userRepository;
    private SkillReponsetory skillReponsetory;
    private SubscribersRepository subscribersRepository;
    private JobReponsetory jobReponsetory;
    private EmailService emailService;

    @Autowired
    public SubscriberService(UserRepository userRepository, SkillReponsetory skillReponsetory, SubscribersRepository subscribersRepository, JobReponsetory jobReponsetory, EmailService emailService) {
        this.userRepository = userRepository;
        this.skillReponsetory = skillReponsetory;
        this.subscribersRepository = subscribersRepository;
        this.jobReponsetory = jobReponsetory;
        this.emailService = emailService;
//        this,
    }

    public Subscribers createSub(Subscribers subscribers) throws IdInvaldException {
        // Kiểm tra email đã tồn tại hay chưa
                String emailCurrent = SercuryUtil.getCurrentUserLogin().orElse("");
        subscribers.setEmail(emailCurrent);
        boolean isEmailExist = this.subscribersRepository.existsByEmail(subscribers.getEmail());
        if (isEmailExist) {
            throw new IdInvaldException("Email này đã tồn tại rồi em ơi");
        }

        // Kiểm tra và thiết lập danh sách kỹ năng
        if (subscribers.getSkills() != null) {
            List<Integer> reqSkills = subscribers.getSkills().stream()
                    .map(Skills::getId)
                    .collect(Collectors.toList());
            List<Skills> dbSkills = this.skillReponsetory.findByIdIn(reqSkills);
            subscribers.setSkills(dbSkills);
        } else {
            subscribers.setSkills(null);
        }

        return this.subscribersRepository.save(subscribers);
    }

    public SubDTO convertCreateSubDTO(Subscribers subscribers) {
        SubDTO res = new SubDTO();

        res.setId(subscribers.getId());
        res.setName(subscribers.getName());
        res.setEmail(subscribers.getEmail());
        res.setCreatedAt(subscribers.getCreatedAt());
        res.setUpdatedAt(subscribers.getUpdatedAt());
        res.setCreatedBy(subscribers.getCreatedBy());
        res.setUpdatedBy(subscribers.getUpdatedBy());

        if (subscribers.getSkills() != null) {
            List<String> skills = subscribers.getSkills()
                    .stream()
                    .map(Skills::getName)
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    public Subscribers updateSub(Subscribers subscribers) throws IdInvaldException {
        Optional<Subscribers> dbSubscriber = this.subscribersRepository.findById(subscribers.getId());
        if (dbSubscriber.isPresent()) {
            if(subscribers.getEmail()!=null){
                dbSubscriber.get().setEmail(subscribers.getEmail());
            }
            if(subscribers.getName()!=null){
                dbSubscriber.get().setName(subscribers.getName());
            }
            if(subscribers.getSkills()!=null){
                dbSubscriber.get().setSkills(subscribers.getSkills());
            }
            return this.subscribersRepository.save(dbSubscriber.get());
        }
        throw new IdInvaldException("sub nay khont on tai em oi");

    }

    public void deleteSub(int id) throws IdInvaldException {
        Optional<Subscribers> dbSubscriber = this.subscribersRepository.findById(id);
        if (dbSubscriber.isPresent()) {
            dbSubscriber.get().setSkills(null);
            this.subscribersRepository.save(dbSubscriber.get());
            this.subscribersRepository.delete(dbSubscriber.get());
        }
//        throw new IdInvaldException("khon co thang sub nayt ddaau ma xoa");

    }

    public void sendSubEmailJobs(){
        List<Subscribers> listSubscribers = this.subscribersRepository.findAll();
        if (listSubscribers != null && listSubscribers.size()>0){
            for (Subscribers sub : listSubscribers){
                List<Skills> skillsList = sub.getSkills();
                if (skillsList !=null && skillsList.size() > 0){
                    List<Job> jobList = this.jobReponsetory.findBySkillsIn(skillsList);
                    if (jobList !=null && jobList.size() > 0){
                        List<ResEmailJob> arr = jobList.stream().map(
                                job -> this.convertJobSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(sub.getEmail(),"Cơ hội việc làm đang chờ đón bạn", "job", sub.getName(), arr);
                    }
                }
            }
        }
    }

    public ResEmailJob convertJobSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));

        List<Skills> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> skillEmails = skills.stream()
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(skillEmails);  // Sử dụng setter thay vì getter

        return res;
    }
    public Subscribers findByEmail(String email) {
        return this.subscribersRepository.findByEmail(email);
    }

}
