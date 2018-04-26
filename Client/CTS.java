package Client;

import Shared.Talker;

import java.io.IOException;
import java.net.Socket;

///CTS = "Connection to server"
public class CTS implements Runnable {
    Client client;
    Talker talker;
    //MessageBox messageBox;

    public CTS(Client client) throws IOException {

        this.client = client;
        talker = new Talker("127.0.0.1", 1337, "");

        new Thread(this).start();
    }

    /**
     * Wrapper method for talker.send(String s)
     * @param stringToSend
     * @throws IOException
     */
    void send(String stringToSend) throws IOException {
        talker.send(stringToSend);
    }

    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                msg = talker.receive().trim();
                System.out.println("Message in CTC: " + msg);
                if(msg.equals("LOGGED_IN")) {
                    client.loginScreen.dispose();
                    client.isLoggedIn = true;
                } else if(msg.startsWith("BUDDY_REQUEST")) {
                    client.showBuddyRequest(msg.substring(13));
                } else if(msg.startsWith("BUDDY_REQUEST_ACCEPTED")) {
                    client.buddyList.addElement(new Buddy(msg.substring(22)));
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
