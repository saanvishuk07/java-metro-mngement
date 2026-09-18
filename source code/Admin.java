package metro;

public class Admin extends User {
    public Admin(int id, String name, String phone) {
        super(id, name, phone);
    }

    public String getRole() {
        return "ADMIN";
    }
}
