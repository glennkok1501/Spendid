package sg.edu.np.spendid.Models;

public class Record {
    private int id;
    private String title;
    private String description;
    private double amount;
    private String category;
    private String dateCreated;
    private String timeCreated;
    private byte[] image;
    private int walletId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public Record() {
    }

    public Record(String title, String description, double amount, String category, String dateCreated, String timeCreated, byte[] image, int walletId) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateCreated = dateCreated;
        this.timeCreated = timeCreated;
        this.image = image;
        this.walletId = walletId;
    }

    public Record(int id, String title, String description, double amount, String category, String dateCreated, String timeCreated, byte[] image, int walletId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateCreated = dateCreated;
        this.timeCreated = timeCreated;
        this.image = image;
        this.walletId = walletId;
    }
}
