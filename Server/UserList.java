package Server;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

public class UserList extends HashMap<String, User> {

    public UserList(){}

    public UserList(DataInputStream dis) throws IOException {
        int numUsers = dis.readInt();

        for(int i = 0; i < numUsers; i++) {
            User tempUser = new User(dis);
            put(tempUser.username, tempUser);
        }
    }

    public void addUser(User user) {
        put(user.username, user);
        try {
            store();
        } catch(IOException ioe) {}
    }

    void store() throws IOException {
//        File file = new File("users.xyz");
//        Enumeration<User> users = elements();
//        DataOutputStream dos = new DataOutputStream(new FileOutputStream((file)));
//        System.out.println("Number of elements" + size());
//        dos.writeInt(size());
//        while(users.hasMoreElements())
//            users.nextElement().store(dos);
    }
}
