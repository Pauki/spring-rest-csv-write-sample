package cz.pauki.service;

import cz.pauki.request.ContactGetRequest;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Interface for contact service
 */
public interface ContactService {

    /** Store data from request object in CSV file
     * in case not exists yet
     *
     * @param request request object
     * @throws IOException exception
     */
    void storeContact(@NotNull ContactGetRequest request) throws IOException;
}
