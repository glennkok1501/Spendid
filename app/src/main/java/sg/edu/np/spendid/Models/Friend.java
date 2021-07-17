package sg.edu.np.spendid.Models;

public class Friend {
    private int friendId;
    private String name;
    private String dateAdded;
    private String publicKey;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Friend() { }

    public Friend(String name, String dateAdded, String publicKey) {
        this.name = name;
        this.dateAdded = dateAdded;
        this.publicKey = publicKey;
    }

    public Friend(int friendId, String name, String dateAdded, String publicKey) {
        this.friendId = friendId;
        this.name = name;
        this.dateAdded = dateAdded;
        this.publicKey = publicKey;
    }
}