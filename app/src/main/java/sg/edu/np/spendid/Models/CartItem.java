package sg.edu.np.spendid.Models;

public class CartItem {
    private int itemId;
    private String name;
    private double amount;
    private boolean check;
    private int cartId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public CartItem() {
    }

    public CartItem(String name, double amount, boolean check, int cartId) {
        this.name = name;
        this.amount = amount;
        this.check = check;
        this.cartId = cartId;
    }

    public CartItem(int itemId, String name, double amount, boolean check, int cartId) {
        this.itemId = itemId;
        this.name = name;
        this.amount = amount;
        this.check = check;
        this.cartId = cartId;
    }
}
