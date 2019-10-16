package web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.payload.AllConfessionResponse;
import web.payload.MessageDetailResponse;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.ConfessionService;
import web.services.EmailService;
import web.services.InfluencerService;
import web.services.MessageService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("api/confession")
public class ConfessionApi {

    @Autowired
    EmailService emailService;

    @Autowired
    InfluencerService influencerService;

    @Autowired
    ConfessionService confessionService;

    @Autowired
    MessageService messageService;

    @GetMapping("")
    public ResponseEntity<?> allConfession(@CurrentUser UserPrincipal userPrincipal) {
        List<AllConfessionResponse> result = confessionService.getAllUserConfession(userPrincipal.getId());
        return result.size() > 0 ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body("You have no conversation!!!");
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> confessionMessage(@PathVariable(value = "id") Long confessionId, @CurrentUser UserPrincipal userPrincipal) {

        if (!confessionService.checkConfessionExist(userPrincipal.getId(), confessionId)) {
            return ResponseEntity.badRequest().body("Confession doesn't exist!!!");
        }

        List<MessageDetailResponse> result = messageService.getMessageDetail(confessionId);
        return result.size() > 0 ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body("Confession doesn't exist!!!");
    }


    @PostMapping("")
    public ResponseEntity<?> sendEmail(@RequestParam("attachFile") @Nullable MultipartFile file,
                                       @RequestParam("subject") @NotBlank String subject,
                                       @RequestParam("body") @NotBlank String body,
                                       @RequestParam("influencerId") String influencerId,
                                       @CurrentUser UserPrincipal userPrincipal) {

        if (!influencerService.checkInfluencerEmail(influencerId)) {
            return ResponseEntity.badRequest().body("Current Influencer has no valid email!");
        }

        if (file != null) {
            try {
                //Get File Name Extension
                String fileNameExtension = (file.getOriginalFilename().split("\\.")[file.getOriginalFilename().split("\\.").length - 1]).toLowerCase();
                if (!fileNameExtension.equals("png") && !fileNameExtension.equals("jpg") && !fileNameExtension.equals("jpeg") &&
                        !fileNameExtension.equals("gif") && !fileNameExtension.equals("doc") && !fileNameExtension.equals("docx") &&
                        !fileNameExtension.equals("docm") && !fileNameExtension.equals("xlsx") && !fileNameExtension.equals("xlsm")) {
                    return ResponseEntity.badRequest().body("Invalid File. Accept file Type: .PNG .JPG .JPEG .GIF .DOC .DOCX .DOCM .XLSX .XLSM");
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid File");
            }
        }

        try {
            emailService.send(file, subject, body, userPrincipal.getId(), influencerId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Your email have been sent successfully!");
    }
}
