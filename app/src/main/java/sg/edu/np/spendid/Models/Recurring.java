package sg.edu.np.spendid.Models;

public class Recurring {
    private int recurringId;
    private String recurringtitle;
    private String recurringdescription;
    private double amount;
    private String category;
    private String recurringstartDate;
    private String recurringendDate;
    private int walletId;
    private String LastUpdated;

    public int getRecurringId() {
        return recurringId;
    }

    public void setRecurringId(int recurringId) {
        this.recurringId = recurringId;
    }

    public String getRecurringtitle() {
        return recurringtitle;
    }

    public void setRecurringtitle(String recurringtitle) {
        this.recurringtitle = recurringtitle;
    }

    public String getRecurringdescription() {
        return recurringdescription;
    }

    public void setRecurringdescription(String recurringdescription) { this.recurringdescription = recurringdescription; }

    public String getLastUpdated() { return LastUpdated; }

    public void setLastUpdated(String lastUpdated) { this.LastUpdated = lastUpdated; }

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

    public String getRecurringstartDate() {
        return recurringstartDate;
    }

    public void setRecurringstartDate(String recurringstartDate) { this.recurringstartDate = recurringstartDate; }

    public String getRecurringendDate() {
        return recurringendDate;
    }

    public void setRecurringendDate(String recurringendDate) { this.recurringendDate = recurringendDate; }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }



    public Recurring() {
    }

    public Recurring(int recurringId, String recurringtitle, String recurringdescription, double amount, String category, String recurringstartDate, String recurringendDate, String lastUpdated, int walletId) {
        this.recurringId = recurringId;
        this.recurringtitle = recurringtitle;
        this.recurringdescription = recurringdescription;
        this.amount = amount;
        this.category = category;
        this.recurringstartDate = recurringstartDate;
        this.recurringendDate = recurringendDate;
        this.LastUpdated = lastUpdated;
        this.walletId = walletId;
    }

    public Recurring(String recurringtitle, String recurringdescription, double amount, String category, String recurringstartDate, String recurringendDate, String lastUpdated, int walletId) {
        this.recurringtitle = recurringtitle;
        this.recurringdescription = recurringdescription;
        this.amount = amount;
        this.category = category;
        this.recurringstartDate = recurringstartDate;
        this.recurringendDate = recurringendDate;
        this.LastUpdated = lastUpdated;
        this.walletId = walletId;
    }
}
