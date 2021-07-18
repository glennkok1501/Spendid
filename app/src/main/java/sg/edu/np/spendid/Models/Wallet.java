package sg.edu.np.spendid.Models;

public class Wallet {
    private int walletId;
    private String name;
    private String description;
    private String currency;
    private String dateCreated;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Wallet() {
    }

    public Wallet(String name, String description, String currency, String dateCreated) {
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.dateCreated = dateCreated;
    }

    public Wallet(int walletId, String name, String description, String currency, String dateCreated) {
        this.walletId = walletId;
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.dateCreated = dateCreated;
    }
}
