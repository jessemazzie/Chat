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
                    client.setVisible(false);
                    client.isLoggedIn = true;
                } else if(msg.startsWith("BROADCAST")) {
                    System.out.println(msg);
//                    messageBox.messageList.addElement(msg.substring(9));
//                    messageBox.repaint();
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
