package intellichef.intellichef;

/**
 * Created by b on 2/17/17.
 */

public class User {
    private RegistrationInfo registrationInfo;
    private int entityPk;
    private boolean isNewUser;

    public User(int entityPk) {
        this.entityPk = entityPk;
    }
    public User(RegistrationInfo registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    public RegistrationInfo getRegistrationInfo() {
        return registrationInfo;
    }

    public int getEntityPk() {
        return entityPk;
    }

    public void setEntityPk(int entityPk) {
        this.entityPk = entityPk;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }
}
