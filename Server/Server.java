package Server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
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
            ioe.printStackTrace();
            users = new UserList();
        }
        loggedInUsers = new Vector<User>();
        try {
            serverSocket = new ServerSocket(portNumber);

            startAcceptingConnections();
        } catch (IOException e) {
            System.out.println("Server unable to start. Check if port " + portNumber + " is in use.");
        }
    }

    void startAcceptingConnections() {
        Socket tempSocket;

        try {
            while(true) {
                tempSocket = serverSocket.accept();
                new CTC(tempSocket, this);
            }
        } catch (IOException e) {}
    }

    void addUser(User newUser) {
        logUserIn(newUser);
        users.addUser(newUser);
    }

    void logUserIn(User user) {
        loggedInUsers.add(user);
    }

    User getUser(String username) {
        return users.get(username);
    }

    public void broadcast(String strToBroadcast) {
        Enumeration<User> userEnum;

        userEnum = users.elements();

        while(userEnum.hasMoreElements()) {
            userEnum.nextElement().send(strToBroadcast);
        }
    }
}
