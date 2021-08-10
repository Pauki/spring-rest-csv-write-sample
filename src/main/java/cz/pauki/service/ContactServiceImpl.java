package cz.pauki.service;

import cz.pauki.source.PropertySource;
import cz.pauki.controller.ContactController;
import cz.pauki.request.ContactGetRequest;
import cz.pauki.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;

/**
 * Implementation of {@link ContactService}
 */
@Service
public class ContactServiceImpl implements ContactService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Value(PropertySource.CONTACT_FILE_PATH)
    private String filePath;
    @Value(PropertySource.CONTACT_FILE_NAME)
    private String fileName;
    @Value(PropertySource.CONTACT_FILE_SEPARATOR)
    private String fileSeparator;

    @Override
    public void storeContact(@NotNull ContactGetRequest request) throws IOException {
        LOGGER.info("storeContact {}", request);
        var line = String.join(fileSeparator, request.getFirstName().trim(), request.getLastName().trim(), request.getEmail().trim());

        FileLock lock = null;
        try {
            File file = new File(filePath + File.separator + fileName);
            //lock file for current thread TODO: add some timeout in future
            while (lock == null) {
                lock = FileUtil.getFileLock(file);
            }

            //search data from request in file, if line with that data not exists -> write it
            if(FileUtil.searchLineInFile(lock.channel(), line).isEmpty()){
                FileUtil.writeToFile(lock.channel(), line + System.lineSeparator());
            }
        }finally {
            if(lock != null){
                lock.channel().close();
            }
        }
    }
}
