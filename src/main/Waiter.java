/*
* Author: Sara Rigotti
*/

public class Waiter extends User {
    public Waiter(String email, String password) {
        super(email, password);
    }

    @Override
    public String getRole() {
        return "Waiter";
    }
}
