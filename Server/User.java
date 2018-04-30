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
    }

    User(DataInputStream dis) throws IOException {
        this.username = dis.readUTF();
        this.password = dis.readUTF();
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
     * Writes the username and password of this user to a DataOutputStream.
     * @param dos
     * @throws IOException
     */
    void store(DataOutputStream dos) throws IOException {
        dos.writeUTF(username);
        dos.writeUTF(password);
        //TODO: Write size of friend list
        //TODO: Write all usernames in friend list.
    }

    @Override
    public String toString() {
        return username;
    }
}
