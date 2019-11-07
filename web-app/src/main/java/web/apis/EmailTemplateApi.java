package web.apis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.entities.EmailTemplate;
import web.repositories.EmailTemplateRepository;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/email-template")
public class EmailTemplateApi {
    private EmailTemplateRepository emailTemplateRepository;

    @PostMapping()
    public void addNewEmailTemplate(@RequestBody EmailTemplate emailTemplate) {
        EmailTemplate em = new EmailTemplate();
        em.setContent(emailTemplate.getContent());

        emailTemplateRepository.save(em);
    }

    @GetMapping
    public List<EmailTemplate> getListEmailTemplate() {
        return emailTemplateRepository.findAll();
    }
}
