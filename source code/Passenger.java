package metro;

public class Passenger extends User {
    private String email;

    public Passenger(int id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    @Override
    public String getRole() {
        return "PASSENGER";
    }

    public String getEmail() { return email; }
}
