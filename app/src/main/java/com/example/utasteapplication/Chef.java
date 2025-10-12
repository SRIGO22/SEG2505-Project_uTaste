/*
* Author: Sara Rigotti
*/

public class Chef extends User implements Role {
    public Chef(String email, String password) {
        super(email, password);
    }

    @Override
    public String getRole() {
        return "Chef";
    }

    @Override
    public String getRoleName() {
        return getRole();
    }
}
