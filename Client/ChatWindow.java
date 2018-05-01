package Client;

import javax.swing.*;
import java.awt.*;

public class ChatWindow extends JFrame {
    JEditorPane messageBox;

    /**
     * Constructor that accepts the username of the buddy with whom you want to chat.
     * @param buddyName
     */
    ChatWindow(String buddyName) {
        Container cp;
        cp = getContentPane();

        messageBox = new JEditorPane();

        cp.add(new JScrollPane(messageBox), BorderLayout.CENTER);
        setupMainFrame(buddyName);
    }

    void setupMainFrame(String buddyName) {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(250, 250);
        setLocation(d.width/4, d.height/4);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Chat with " + buddyName);

        setVisible(true);
    }
}
