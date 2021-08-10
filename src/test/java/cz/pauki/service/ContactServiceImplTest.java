package cz.pauki.service;

import cz.pauki.source.PropertySource;
import cz.pauki.request.ContactGetRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest(properties = {"contact.csv.file.name:test-file.csv"}, classes = ContactServiceImpl.class)
@RunWith(SpringRunner.class)
public class ContactServiceImplTest {

    @Autowired
    private ContactService contactService;
    @Value(PropertySource.CONTACT_FILE_PATH)
    private String filePath;
    @Value(PropertySource.CONTACT_FILE_NAME)
    private String fileName;

    @Test
    public void storeContact() throws IOException, InterruptedException {

        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setMaxPoolSize(10);
        pool.initialize();

        //should write 9 new lines
        IntStream.range(1,10).forEach(value ->
                pool.execute(new ContactTestRunnable(
                        new ContactGetRequest("firstName" + value, "lastName" + value, "email@email.cz" + value), contactService)));

        //should not write anything (duplicates)
        IntStream.range(1,5).forEach(value ->
                pool.execute(new ContactTestRunnable(
                        new ContactGetRequest("firstName" + value, "lastName" + value, "email@email.cz" + value), contactService)));

        while(pool.getActiveCount() != 0){
            Thread.sleep(1000);
        }

        List<String> strings = Files.readAllLines(Path.of(filePath + File.separator + fileName));
        Assert.assertEquals(9, strings.size());
        Assert.assertEquals("firstName1,lastName1,email@email.cz1", strings.get(0));
    }

    class ContactTestRunnable implements Runnable{

        private ContactGetRequest request;
        private ContactService contactService;

        ContactTestRunnable(ContactGetRequest request,
                            ContactService contactService) {
            this.request = request;
            this.contactService = contactService;
        }

        @Override
        public void run() {
            try {
                contactService.storeContact(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}