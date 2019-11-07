package web.apis;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;
import web.payload.AllConfessionResponse;
import web.payload.MessageDetailResponse;
import web.repositories.InfluencerRepository;
import web.security.CurrentUser;
import web.security.UserPrincipal;
import web.services.ConfessionService;
import web.services.EmailService;
import web.services.InfluencerService;
import web.services.MessageService;

import javax.validation.constraints.NotBlank;
import java.io.*;
import java.util.List;

import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


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
        try {
            emailService.downloadEmailsFromInbox();
        } catch (Exception e) {

        }
        List<AllConfessionResponse> result = confessionService.getAllUserConfession(userPrincipal.getId());
        return result.size() > 0 ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body("You have no conversation!!!");
    }

    @GetMapping("/history/{influencerId}")
    public ResponseEntity<?> confessionMessage(
            @PathVariable(value = "influencerId") String influencerId,
            @CurrentUser UserPrincipal userPrincipal) {
        emailService.downloadEmailsFromInbox();
        String confessionId = confessionService.findConfession(userPrincipal.getId(), influencerId).getId();
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


    @GetMapping("/Download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, @CurrentUser UserPrincipal userPrincipal) {
        try {
            if(!messageService.checkOwnedFile(userPrincipal.getId(), fileName)){
                throw new Exception("File not found!!!");
            }
            File file = new File("Media/Mail/" + fileName);
            Resource resource = new UrlResource(file.toURI());
            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = Files.probeContentType(file.toPath());
            } catch (Exception e) {
                //logger.info("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File not found!!!");
        }
    }
}
