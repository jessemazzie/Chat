package Client;

public class Buddy {
    String username;
    boolean isOnline;

    Buddy(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return isOnline ? "* " + username : username;
    }
}
