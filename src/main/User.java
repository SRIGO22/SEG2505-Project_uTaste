/** 
* Author: Sara Rigotti
*
**/
public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected final String createdAt;
    protected String modifiedAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = getCurrentTimestamp();
        this.modifiedAt = this.createdAt;
    }

    public boolean authenticate(String inputPwd) {
        return this.password.equals(inputPwd);
    }

    public void changePassword(String newPwd) {
        this.password = newPwd;
        updateModifiedAt();
    }

    public void updateProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        updateModifiedAt();
    }

    protected void updateModifiedAt() {
        this.modifiedAt = getCurrentTimestamp();
    }

    private String getCurrentTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }

    public abstract String getRole();
}
