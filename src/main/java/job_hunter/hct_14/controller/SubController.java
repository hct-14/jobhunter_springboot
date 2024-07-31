package job_hunter.hct_14.controller;

import job_hunter.hct_14.entity.Subscribers;
import job_hunter.hct_14.entity.response.SubRepomsetory.SubDTO;
import job_hunter.hct_14.util.annotation.ApiMessage;
import job_hunter.hct_14.util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import job_hunter.hct_14.service.SubscriberService;

@RestController
@RequestMapping("api/v1")
public class SubController {
    @Autowired
    private final SubscriberService subscriberService;


    public SubController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("create oke")

    public ResponseEntity<Subscribers> createSub(@RequestBody Subscribers subscribers) throws IdInvaldException {
        Subscribers subscribers1 = this.subscriberService.createSub(subscribers);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscribers1);
    }
    @PutMapping("/subscribers")
    @ApiMessage("update oke")
    public ResponseEntity<SubDTO> updateSub(@RequestBody Subscribers subscribers) throws IdInvaldException {
        Subscribers subscribers1 = this.subscriberService.updateSub(subscribers);
        return ResponseEntity.status(HttpStatus.OK).body(this.subscriberService.convertCreateSubDTO(subscribers1));
    }
    @DeleteMapping("/subscribers/{id}")
    @ApiMessage("xoa oke")
    public ResponseEntity<String> deleteSub(@PathVariable int id) throws IdInvaldException {
        this.subscriberService.deleteSub(id);
        return ResponseEntity.status(HttpStatus.OK).body("xoa oke");
    }
}
