package job_hunter.hct_14.controller;

import job_hunter.hct_14.service.EmailService;
import job_hunter.hct_14.service.SubscriberService;
import job_hunter.hct_14.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    public final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("send simple email")
    public String sendSimpleEmail() {
//        this.emailService.sendEmailSync("hoangthanhgolle@gmail.com","hcttest","<h1><b> hello </b> </h1>", false, true);
        this.subscriberService.sendSubEmailJobs();
        return "send oke";
    }

//    @GetMapping("/email")



}
