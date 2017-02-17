package intellichef.intellichef;

/**
 * Created by b on 2/17/17.
 */

public class User {
    private RegistrationInfo registrationInfo;

    public User(RegistrationInfo registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    public RegistrationInfo getRegistrationInfo() {
        return registrationInfo;
    }
}
