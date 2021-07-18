package sg.edu.np.spendid.Models;

public class Recurring {
    private int recurringId;
    private String recurringname;
    private String recurringdescription;
    private String recurringcurrency;
    private String recurringstartDate;

    public int getRecurringId() { return recurringId; }

    public void setRecurringId(int recurringId) { this.recurringId = recurringId; }

    public String getRecurringname() { return recurringname; }

    public void setRecurringname(String recurringname) { this.recurringname = recurringname; }

    public String getRecurringdescription() { return recurringdescription; }

    public void setRecurringdescription(String recurringdescription) { this.recurringdescription = recurringdescription; }

    public String getRecurringcurrency() { return recurringcurrency; }

    public void setRecurringcurrency(String recurringcurrency) { this.recurringcurrency = recurringcurrency; }

    public String getRecurringstartDate() { return recurringstartDate; }

    public void setRecurringstartDate(String recurringstartDate) { this.recurringstartDate = recurringstartDate; }

    public Recurring() {
    }

    public Recurring(String recurringname, String recurringdescription, String recurringcurrency, String recurringstartDate) {
        this.recurringname = recurringname;
        this.recurringdescription = recurringdescription;
        this.recurringcurrency = recurringcurrency;
        this.recurringstartDate = recurringstartDate;
    }

    public Recurring(int recurringId, String recurringname, String recurringdescription, String recurringcurrency, String recurringstartDate) {
        this.recurringId = recurringId;
        this.recurringname = recurringname;
        this.recurringdescription = recurringdescription;
        this.recurringcurrency = recurringcurrency;
        this.recurringstartDate = recurringstartDate;
    }
}
