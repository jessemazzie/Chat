package Server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    final int portNumber = 1337;
    ServerSocket serverSocket;
    private UserList users;
    private Vector<User> loggedInUsers;

    Server() {
        File userFile;
        try {
            userFile = new File("users.xyz");

            users = new UserList(new DataInputStream(new FileInputStream(userFile)));
            System.out.println("Users loaded from file.");
        } catch(IOException ioe) {
            users = new UserList();
            System.out.println("Created new user file.");
        }
        loggedInUsers = new Vector<User>();
        try {
            serverSocket = new ServerSocket(portNumber);

            startAcceptingConnections();
        } catch (IOException e) {
            System.out.println("Server unable to start. Check if port " + portNumber + " is in use.");
        }
    }

    /**
     * This is an infinite loop that waits on new sockets to open and spins up a CTC for each new connection.
     */
    void startAcceptingConnections() {
        Socket tempSocket;

        try {
            while(true) {
                tempSocket = serverSocket.accept();
                new CTC(tempSocket, this); //TODO: Maybe find a more clever way to assign these to users.
            }
        } catch (IOException e) {}
    }

    /**
     * Adds a user to the user hashtable. Used for registration.
     * @param newUser
     */
    void addUser(User newUser) {
        users.addUser(newUser);
    }

    /**
     * Adds a user to the vector of currently logged in users.
     * @param user
     */
    void logUserIn(User user) {
        loggedInUsers.add(user);
    }

    /**
     * Wrapper method for getting a specific user from the users hashtable.
     * @param username
     * @return user. Null if user does not exist
     */
    User getUser(String username) {
        return users.get(username);
    }

    public static void main(String[] args) {
        new Server();
    }
}
