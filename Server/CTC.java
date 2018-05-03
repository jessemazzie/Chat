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
    void send(String stringToSend) {
        try {
            talker.send(stringToSend);
        } catch (IOException e) {
            System.out.println("Failed to send message: " + stringToSend);
        }
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
                cmd = talker.receive().trim();

                if(cmd.startsWith("REGISTER")) {
                    commandParts = cmd.split(" ");
                    //Needs to include command, username, and password.
                    if (commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Registration failed.");
                    else {
                        //TODO: Check if username already taken
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
                            send("BAD_USERNAME");
                        else if(!user.password.equals(commandParts[2].trim())) {
                            send("BAD_PASSWORD");
                        } else {
                            send("LOGGED_IN " + commandParts[1]); //Return the username so the CTS can set it
                            user.ctc = this; //Assign the user a CTC so the server can talk to it.
                            server.logUserIn(user);
                        }
                    }
                } else if(cmd.startsWith("BUDDY_REQUEST_ACCEPTED")) {
                    commandParts = cmd.split(" ");
                    server.getUser(commandParts[1]).send("BUDDY_REQUEST_ACCEPTED " + commandParts[2]);

                } else if(cmd.startsWith("BUDDY_REQUEST")) {
                    commandParts = cmd.split(" ");
                    //Needs to include command, username, and password.
                    if(commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Buddy request failed.");

                    user = server.getUser(commandParts[1]);

                    if(user != null) {
                        user.send("BUDDY_REQUEST " + commandParts[2]);
                    } else if(commandParts[1].equals(commandParts[2])) {
                        send("CANT_BUDDY_SELF");
                    } else {
                        send("NONEXISTENT_USER");
                    }
                } else if(cmd.startsWith("MESSAGE")) {
                    commandParts = cmd.split(" ", 4); //limit 4

                    user = server.getUser(commandParts[1]);

                    if(user == null);
                        //TODO: Handle this.
                    else {
                        user.send("MESSAGE " + " " + commandParts[2] + " " + commandParts[3]);
                    }
                } else if(cmd.startsWith("GET_FRIENDS ")) {
                    String replyMsg = "";

                    commandParts = cmd.split(" ");

                    user = server.getUser(commandParts[1]);

                    //TODO: CHECK IF USER VALID

                    for(String s : user.friendsList) {
                        replyMsg += " " + s;
                    }

                    send("FRIENDS_LIST " + replyMsg);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
