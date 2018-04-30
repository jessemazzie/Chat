package Server;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * A hashtable of users on the server. Has the ability to write itself to/read itself from a file.
 */
public class UserList extends Hashtable<String, User> {

    /**
     * Default constructor. Needs to exist for instances when there is no file to get a DataInputStream from to call the other constructor.
     */
    public UserList(){}

    /**
     * Constructor that accepts a DataInputStream for the purpose of generating this list and the users within from a file.
     * @param dis
     * @throws IOException
     */
    public UserList(DataInputStream dis) throws IOException {
        int numUsers = dis.readInt();

        for(int i = 0; i < numUsers; i++) {
            User tempUser = new User(dis);
            put(tempUser.username, tempUser);
        }
    }

    /**
     * Wrapper method for put(String, User) that also saves the updated hashtable to a file each time a new user is added.
     * @param user
     */
    public void addUser(User user) {
        put(user.username, user);
        try {
            store();
        } catch(IOException ioe) {}
    }

    /**
     * Saves itself and all elements to a file named "users.xyz"
     * @throws IOException
     */
    void store() throws IOException {
        File file = new File("users.xyz");
        Enumeration<User> users = elements();
        DataOutputStream dos = new DataOutputStream(new FileOutputStream((file)));

        dos.writeInt(size());
        while(users.hasMoreElements())
            users.nextElement().store(dos);
    }
}
