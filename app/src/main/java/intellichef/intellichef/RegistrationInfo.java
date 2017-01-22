package intellichef.intellichef;

/**
 * Created by b on 1/22/17.
 */

public class RegistrationInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    /**
     * Getter method for user's first name
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter method for user's last name
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter method for user's email address
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter method for user's password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Change user's first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Change user's last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Change user's email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Change user's password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
