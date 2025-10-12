/**
* Author: Sara Rigotti
**/

import java.util.HashMap;

public class Administrator extends User implements Role {
    private HashMap<String, Waiter> waiterAccounts = new HashMap<>();

    public Administrator(String email, String password) {
        super(email, password);
    }

    public void createWaiter(String email) {
        Waiter waiter = new Waiter(email, "waiter-pwd");
        waiterAccounts.put(email, waiter);
    }

    public void deleteWaiter(String email) {
        waiterAccounts.remove(email);
    }

    public void modifyWaiter(String email, String newFirstName, String newLastName) {
        Waiter waiter = waiterAccounts.get(email);
        if (waiter != null) {
            waiter.updateProfile(newFirstName, newLastName, email);
        }
    }

    @Override
    public String getRole() {
        return "Administrator";
    }

    @Override
    public String getRoleName() {
        return getRole();
    }
}
