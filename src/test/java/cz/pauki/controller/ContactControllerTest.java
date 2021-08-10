package cz.pauki.controller;

import cz.pauki.request.ContactGetRequest;
import cz.pauki.service.ContactService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@WebMvcTest(value = ContactController.class)
public class ContactControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @MockBean
    private ContactService contactService;

    @Before
    public void init(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void getContact() throws Exception {
        var captor = ArgumentCaptor.forClass(ContactGetRequest.class);
        Mockito.doNothing().when(contactService).storeContact(captor.capture());

        mvc.perform(get("/contact")
                .param("firstName", "firstName")
                .param("lastName", "lastName")
                .param("email", "aa@bb.cz"))
                .andExpect(status().isOk());

        Assert.assertEquals("firstName", captor.getValue().getFirstName());
        Assert.assertEquals("lastName", captor.getValue().getLastName());
        Assert.assertEquals("aa@bb.cz", captor.getValue().getEmail());
    }
}