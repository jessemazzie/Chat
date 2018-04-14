package Shared;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Talker {
    private String ID;
    private BufferedReader reader;
    private DataOutputStream dos;

    public Talker(String domain, int portNum, String ID) throws IOException {
        Socket s;

        s = new Socket(domain, portNum);

        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        dos = new DataOutputStream(s.getOutputStream());
        this.ID = ID;
        System.out.println("Talker with ID: " + ID + " initiated.");
    }

    public Talker(Socket s, String ID) throws IOException {
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        dos = new DataOutputStream(s.getOutputStream());
        this.ID = ID;
        System.out.println("Server talker initiated");
    }

    public void send(String message) throws IOException {
        dos.writeBytes(message + "\n");
        System.out.println("From: " + ID + "\nMessage: " + message + "\n");
    }

    /**
     * @return a line of text from the BufferedReader
     * @throws IOException
     */
    public String receive() throws IOException {
        String message;
        message = reader.readLine(); //Locks here until message is present in the stream.
        System.out.println("Message: " + message + " received by: " + ID);
        return message;
    }

    /**
     * Simple setter for ID.
     * @param ID
     */
    public void setID(String ID) {
        System.out.println("ID set to: " + ID);
        this.ID = ID;
    }
}
