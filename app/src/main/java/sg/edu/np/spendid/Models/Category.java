package sg.edu.np.spendid.Models;

public class Category {
    private String title;
    private boolean expense;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExpense() {
        return expense;
    }

    public void setExpense(boolean expense) {
        this.expense = expense;
    }

    public Category() {
    }

    public Category(String title, boolean expense) {
        this.title = title;
        this.expense = expense;
    }
}
