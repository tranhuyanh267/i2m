package web.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@Log4j2
@Service
public class GoogleCloudService {

    @Value("${app.credentialPath}")
    private String credentialPath;

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        log.info("Uploading file name " + fileName);
        log.info("Credential path " + credentialPath);
        try {
            Resource resource = new ClassPathResource(credentialPath);
            File credentialsFile = resource.getFile();
            Credentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsFile));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials)
                    .setProjectId("I2M-Authentication").build().getService();
            //File Rename
            String[] extensions = file.getOriginalFilename().split("\\.");
            fileName = fileName + "." + extensions[extensions.length - 1];
            InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] readBuf = new byte[4096];
            while (is.available() > 0) {
                int bytesRead = is.read(readBuf);
                os.write(readBuf, 0, bytesRead);
            }

            BlobInfo blobInfo =
                    storage.create(
                            BlobInfo
                                    .newBuilder("i2m-bucket", fileName)
                                    // Modify access list to allow all users with link to read file
                                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(), Acl.Role.READER))))
                                    .build(),
                            os.toByteArray());

            return blobInfo.getMediaLink();
        } catch (Exception ex) {
            log.error("Error with message:" + ex.getMessage());
            throw ex;
        }
    }

    public String uploadFileFromInputStream(InputStream is, String fileName) throws IOException {
        Resource resource = new ClassPathResource(credentialPath);
        File credentialsFile = resource.getFile();
        Credentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsFile));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials)
                .setProjectId("I2M-Authentication").build().getService();
        //File Rename

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] readBuf = new byte[4096];
        int byteRead;
        while ((byteRead = is.read(readBuf)) != -1) {

            os.write(readBuf, 0, byteRead);
        }

        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder("i2m-bucket", fileName)
                                // Modify access list to allow all users with link to read file
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(), Acl.Role.READER))))
                                .build(),
                        os.toByteArray());

        return blobInfo.getMediaLink();
    }
}
