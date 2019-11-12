package web.apis;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.security.CurrentUser;
import web.security.UserPrincipal;

import java.io.File;
import java.nio.file.Files;

@RestController
@RequestMapping("api/files")
public class FileApi {

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, @CurrentUser UserPrincipal userPrincipal) {
        try {
            //            if(!messageService.checkOwnedFile(userPrincipal.getId(), fileName)){
//                throw new Exception("File not found!!!");
//            }
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

    @GetMapping("/avatar/{img}")
    public ResponseEntity<?> getAvatar(@PathVariable String img) {
        try {
            File file = new File("Media/User/" + img);
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
