/*
* Author: Sara Rigotti
*/

public class Session {
    private User user;
    private final String loginTime;

    public Session(User user) {
        this.user = user;
        this.loginTime = java.time.LocalDateTime.now().toString();
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
