package Client;

/**
 * Simple class for storing buddies, used for displaying buddies in the JList
 */
public class Buddy {
    String username;
    boolean isOnline;
    ChatWindow chatWindow;

    Buddy(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return isOnline ? "* " + username : username;
    }
}
