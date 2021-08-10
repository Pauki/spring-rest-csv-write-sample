package cz.pauki.controller;

import cz.pauki.source.RestSource;
import cz.pauki.request.ContactGetRequest;
import cz.pauki.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Controller class for 'contact' REST service
 */
@RestController
public class ContactController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
    private ContactService contactService;

    public ContactController(@NotNull ContactService contactService) {
        Assert.notNull(contactService, "contactService cannot be null");
        this.contactService = contactService;
    }

    @GetMapping(value = RestSource.CONTACT_GET)
    public void getContact(@ModelAttribute @Valid ContactGetRequest request) throws IOException {
        LOGGER.debug("getContact {}", request);

        contactService.storeContact(request);
    }
}
