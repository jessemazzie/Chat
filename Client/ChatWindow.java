package Client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.IOException;

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
        messageBox.setContentType("text/html");
        messageBox.setEditable(false);

        cp.add(new JScrollPane(messageBox), BorderLayout.CENTER);
        setupMainFrame(buddyName);
        addMessage("HELLO");
    }

    void addMessage(String msg) {
        HTMLDocument doc;
        Element rootElement;
        Element bodyElement;

        doc = (HTMLDocument) messageBox.getDocument();
        rootElement = doc.getRootElements()[0]; //only one root element exists so it will always be at index 0.
        bodyElement = rootElement.getElement(0);
        try {
            doc.insertBeforeEnd(bodyElement, "<div alignment = 'left' font color = 'blue'>" + msg + "</div>");
        } catch (BadLocationException ble) {
            System.out.println("y tho");
            ble.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageBox.repaint();
    }

    void setupMainFrame(String buddyName) {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(400, 250);
        setLocation(d.width/4, d.height/4);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Chat with " + buddyName);

        setVisible(true);
    }
}
