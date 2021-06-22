package sg.edu.np.spendid;

public class ShoppingCart {
    private int cartId;
    private String name;
    private String dateCreated;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ShoppingCart() {
    }

    public ShoppingCart(String name, String dateCreated) {
        this.name = name;
        this.dateCreated = dateCreated;
    }

    public ShoppingCart(int cartId, String name, String dateCreated) {
        this.cartId = cartId;
        this.name = name;
        this.dateCreated = dateCreated;
    }
}
