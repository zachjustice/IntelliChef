package intellichef.intellichef;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by b on 1/22/17.
 */

public class RegistrationInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    public RegistrationInfo(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Getter method for user's first name
     * @return firstName
     */
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
