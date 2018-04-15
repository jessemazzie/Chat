package Server;

import Shared.Talker;

import java.io.IOException;
import java.net.Socket;

//CTC = "Connection to client"
public class CTC implements Runnable {
    Server.Server server;
    Talker talker;

    public CTC(Socket socket, Server.Server server) throws IOException {
        this.server = server;
        talker = new Talker(socket, "CTC");
        new Thread(this).start();
    }

    @Override
    public void run() {
        String cmd;
        String[] commandParts;
        Server.User user;
        while(true) {
            try {
                cmd = talker.receive();

                if(cmd.startsWith("REGISTER")) {
                    commandParts = cmd.split(" ");
                    if (commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Registration failed.");
                    else {
                        System.out.println("Registered new username: " + commandParts[1] + " password: " + commandParts[2]);
                        user = new Server.User(commandParts[1].trim(), commandParts[2].trim());
//                        user.ctc = this;
//                        server.addUser(user);
                    }
                } else if(cmd.startsWith("LOGIN")) {
                    commandParts = cmd.split(" ");
                    if (commandParts.length != 3)
                        System.out.println("Invalid number of parameters passed. Login failed.");
                    else {
//                        user = server.getUser(commandParts[1].trim());//new User(commandParts[1], commandParts[2]);
                        if(user == null)
                            talker.send("BAD_USERNAME");
                        else if(!user.password.equals(commandParts[2].trim())) {
                            talker.send("BAD_PASSWORD");
                        } else {
                            talker.send("LOGGED_IN");
//                            user.ctc = this;
                        }

//                        server.logUserIn(user);
                    }
                } else if(cmd.startsWith("BROADCAST")) {
//                    server.broadcast(cmd);
                    //talker.send(cmd);
                }
            } catch (IOException ioe) {}
        }
    }
}
