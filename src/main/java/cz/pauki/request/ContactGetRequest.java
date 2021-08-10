package cz.pauki.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Object for 'contact' REST service query params
 */
public class ContactGetRequest extends RestBaseObject {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    public ContactGetRequest(@NotNull String firstName, @NotNull String lastName, @NotNull @Pattern(regexp = ".+@.+\\..+") String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
