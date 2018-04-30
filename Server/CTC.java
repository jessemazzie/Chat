package Server;

import Shared.Talker;

import java.io.IOException;
import java.net.Socket;

/**
 * Connection to client. This handles sending commands to/receiving commands from the client via the talker.
 */
public class CTC implements Runnable {
    Server server;
    Talker talker;

    public CTC(Socket socket, Server server) throws IOException {
        this.server = server;
        talker = new Talker(socket, "CTC");
        new Thread(this).start();
    }

    /**
     * Wrapper method for talker.send(String s)
     * @param stringToSend
     * @throws IOException
     */
    void send(String stringToSend) throws IOException {
        talker.send(stringToSend);
        //TODO: Replace all calls to 'talker.send(s)' with calls to this method.
    }

    /**
     * Spins up a new thread to infinitely loop, waiting for commands.
     */
    @Override
    public void run() {
        String cmd;
        String[] commandParts;
        User user;
        while(true) {
            try {
                cmd = talker.receive();

                if(cmd.startsWith("REGISTER")) {
                    commandParts = cmd.split(" ");
                    //Needs to include command, username, and password.
                    if (commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Registration failed.");
                    else {
                        System.out.println("Registered new username: " + commandParts[1] + " password: " + commandParts[2]);
                        user = new User(commandParts[1].trim(), commandParts[2].trim());
                        user.ctc = this;
                        server.addUser(user);
                    }
                } else if(cmd.startsWith("LOGIN")) {
                    commandParts = cmd.split(" ");
                    //Needs to include command, username, and password.
                    if (commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Login failed.");
                    else {
                        //Check if user exists by attempting to get user from hashtable in server
                        user = server.getUser(commandParts[1].trim());
                        if(user == null)
                            talker.send("BAD_USERNAME");
                        else if(!user.password.equals(commandParts[2].trim())) {
                            talker.send("BAD_PASSWORD");
                        } else {
                            talker.send("LOGGED_IN " + commandParts[1]); //Return the username so the CTS can set it
                            user.ctc = this; //Assign the user a CTC so the server can talk to it.
                            server.logUserIn(user);
                        }
                    }
                } else if(cmd.startsWith("BUDDY_REQUEST_ACCEPTED")) {
                    System.out.println(cmd);
                    commandParts = cmd.split(" ");

                    if(server.getUser(commandParts[1]) != null) {
                        server.getUser(commandParts[1]).send("BUDDY_REQUEST_ACCEPTED " + commandParts[2]);
                        //talker.send("BUDDY_REQUEST " + commandParts[2]); //TODO: Send to the correct user
                    }
                } else if(cmd.startsWith("BUDDY_REQUEST")) {
                    commandParts = cmd.split(" ");
                    //Needs to include command, username, and password.
                    if(commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Buddy request failed.");

                    if(server.getUser(commandParts[1]) != null) { //TODO: Store this in a 'user' variable for simplicity
                        server.getUser(commandParts[1]).send("BUDDY_REQUEST " + commandParts[2]);
                    } else {
                        talker.send("NONEXISTENT_USER"); // TODO: Handle this client-side.
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
