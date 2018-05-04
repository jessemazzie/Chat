package Client;

import Shared.Talker;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

///CTS = "Connection to server"
public class CTS implements Runnable {
    Client client;
    Talker talker;
    String ID;
    //MessageBox messageBox;

    public CTS(Client client, String ID) throws IOException {

        this.client = client;
        talker = new Talker("127.0.0.1", 1337, ID);

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

    /**
     * Spins up a new thread to infinitely loop, waiting for commands from the server.
     */
    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                msg = talker.receive().trim();
                System.out.println("Message in CTS: " + msg);
                if(msg.startsWith("LOGGED_IN")) {
                    client.loginScreen.dispose();
                    client.isLoggedIn = true;
                    client.setTitle("Chat - Logged in as " + msg.substring(10));
                    ID = msg.substring(10).trim();

                    send("GET_BUDDIES " + ID);
                } else if(msg.startsWith("BUDDY_REQUEST_ACCEPTED")) {
                    client.buddyList.addElement(new Buddy(msg.substring(22)));
                } else if(msg.startsWith("BUDDY_REQUEST")) {
                    client.showBuddyRequest(msg.substring(13));
                } else if(msg.equals("NONEXISTENT_USER")) {
                    client.displayError("User does not exist. Please try again.", "User does not exist");
                } else if(msg.startsWith("MESSAGE")) {
                    String[] parts;

                    parts = msg.split(" ", 3);

                    if(parts.length == 3) {
                        System.out.println("USERNAME HERE: " + parts[1]);
                        System.out.println("MESSAGE HERE: " + parts[2]);
                        Buddy buddy = client.getBuddy(parts[1].trim());
                        System.out.println(buddy.toString());
                        if(buddy.chatWindow == null)
                            buddy.chatWindow = new ChatWindow(buddy.username, client);

                        buddy.chatWindow.addMessage(parts[2], false);
                    } else
                        System.out.println("Shit's broken, yo.");
                } else if(msg.startsWith("BUDDY_LIST")) {
                    String[] parts;

                    parts = msg.split(" ");
                    if(parts.length > 1) {
                        for(int i = 1; i < parts.length; i++) {
                            client.buddyList.addElement(new Buddy(parts[i]));
                            client.buddyJList.repaint();
                        }
                    }
                }
            }
        } catch(IOException ioe) {
           client.isLoggedIn = false;
        }
    }
}
