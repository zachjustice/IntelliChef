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

    public RegistrationInfo(String firstName, String lastName, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
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

    public String getUsername() {
        return username;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
