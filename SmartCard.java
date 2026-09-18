package metro;

public class SmartCard {
    private int cardId;
    private String cardNumber;
    private double balance;
    private String status;

    public SmartCard(int cardId, String cardNumber, double balance, String status) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.status = status;
    }

    public int getCardId() { return cardId; }
    public String getCardNumber() { return cardNumber; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
    public boolean isActive() { return "ACTIVE".equals(status); }
}
