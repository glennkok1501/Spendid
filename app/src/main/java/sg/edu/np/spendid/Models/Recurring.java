package sg.edu.np.spendid.Models;

public class Recurring {
    private int recurringId;
    private String recurringname;
    private String recurringdescription;
    private String recurringcurrency;
    private String recurringstartDate;
    private String recurringendDate;

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

    public String getRecurringendDate() { return recurringendDate; }

    public void setRecurringendDate(String recurringendDate) { this.recurringendDate = recurringendDate; }

    public Recurring() {
    }

    public Recurring(String recurringname, String recurringdescription, String recurringcurrency, String recurringstartDate, String recurringendDate) {
        this.recurringname = recurringname;
        this.recurringdescription = recurringdescription;
        this.recurringcurrency = recurringcurrency;
        this.recurringstartDate = recurringstartDate;
        this.recurringendDate = recurringendDate;
    }

    public Recurring(int recurringId, String recurringname, String recurringdescription, String recurringcurrency, String recurringstartDate, String recurringendDate) {
        this.recurringId = recurringId;
        this.recurringname = recurringname;
        this.recurringdescription = recurringdescription;
        this.recurringcurrency = recurringcurrency;
        this.recurringstartDate = recurringstartDate;
        this.recurringendDate = recurringendDate;
    }
}
