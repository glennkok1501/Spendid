package sg.edu.np.spendid;
//public static final String TABLE_RECORD = "Record";
//public static final String COLUMN_RECORD_ID = "RecordId";
//public static final String COLUMN_RECORD_TITLE = "Title";
//public static final String COLUMN_RECORD_DESCRIPTION = "Description";
//public static final String COLUMN_RECORD_AMOUNT = "Currency";
//public static final String COLUMN_RECORD_CATEGORY = "Category";
//public static final String COLUMN_RECORD_DATECREATED = "DateCreated";
//public static final String COLUMN_RECORD_TIMECREATED = "TimeCreated";
public class Record {
    private int id;
    private String title;
    private String description;
    private String amount;
    private String category;
    private String dateCreated;
    private String timeCreated;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public Record() {
    }

    public Record(String title, String description, String amount, String category, String dateCreated, String timeCreated) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateCreated = dateCreated;
        this.timeCreated = timeCreated;
    }

    public Record(int id, String title, String description, String amount, String category, String dateCreated, String timeCreated) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateCreated = dateCreated;
        this.timeCreated = timeCreated;
    }
}
