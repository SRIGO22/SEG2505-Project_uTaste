/*
* Author: Sara Rigotti
*/

public class Chef extends User {
    public Chef(String email, String password) {
        super(email, password);
    }

    @Override
    public String getRole() {
        return "Chef";
    }
}
