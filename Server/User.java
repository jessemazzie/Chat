package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * Data representation of a user.
 * Stores username, password, CTC, and buddy list.
 *
 */
public class User {
    String username;
    String password;
    CTC ctc;
    Vector<String> friendsList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        friendsList = new Vector<String>();
    }

    /**
     * Instantiate user from DataInputStream.
     * Reads username, password, and contents of friends list from file.
     * @param dis
     * @throws IOException
     */
    User(DataInputStream dis) throws IOException {
        int size;
        this.username = dis.readUTF();
        this.password = dis.readUTF();
        size = dis.readInt(); //size of friends list.
        friendsList = new Vector<String>(size);
        for(int i = 0; i < size; i++)
            friendsList.add(dis.readUTF());
    }

    /**
     * Wrapper method for sending messages to this user via its CTC.
     * @param messageToBroadcast
     */
    void send(String messageToBroadcast) {
        try {
            ctc.talker.send(messageToBroadcast);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add someone to friend list.
     * @param friendName
     */
    void addFriend(String friendName) {
        friendsList.add(friendName);
    }

    /**
     * Writes the username and password of this user to a DataOutputStream.
     * @param dos
     * @throws IOException
     */
    void store(DataOutputStream dos) throws IOException {
        dos.writeUTF(username);
        dos.writeUTF(password);
        dos.writeInt(friendsList.size()); //size of friends list
        for(String s : friendsList) {
            dos.writeUTF(s);
        }
    }

    @Override
    public String toString() {
        return username;
    }
}
