package Client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ChatWindow extends JFrame implements ActionListener, DocumentListener, DropTargetListener {
    JEditorPane messageBox;
    JTextField messageField;
    JButton sendButton;
    String buddyName;
    Client client;
    File fileToSend;
    DropTarget dt;

    /**
     * Constructor that accepts the username of the buddy with whom you want to chat.
     * @param buddyName
     */
    ChatWindow(String buddyName, Client client) {
        Container cp;
        JPanel inputContainer;
        cp = getContentPane();

        this.buddyName = buddyName.trim();
        this.client = client;

        messageBox = new JEditorPane();
        messageBox.setContentType("text/html");
        messageBox.setEditable(false);

        dt = new DropTarget(messageBox, this);

        messageField = new JTextField(25);
        messageField.getDocument().addDocumentListener(this);

        sendButton = Client.newJButton("Send", "SEND", this);
        sendButton.setEnabled(false); //disabled until someone types in the chatbox.
        getRootPane().setDefaultButton(sendButton);

        inputContainer = new JPanel(new BorderLayout());
        inputContainer.add(messageField, BorderLayout.CENTER);
        inputContainer.add(sendButton, BorderLayout.EAST);

        cp.add(new JScrollPane(messageBox), BorderLayout.CENTER);
        cp.add(inputContainer, BorderLayout.SOUTH);
        setupMainFrame(buddyName);
    }

    void addMessage(String msg, boolean isFromMe) {
        HTMLDocument doc;
        Element rootElement;
        Element bodyElement;

        doc = (HTMLDocument) messageBox.getDocument();
        rootElement = doc.getRootElements()[0]; //only one root element exists so it will always be at index 0.
        bodyElement = rootElement.getElement(0);
        try {
            if(isFromMe)
                doc.insertBeforeEnd(bodyElement, "<div alignment = 'left' font color = 'blue'> Me: " + msg + "</div>");
            else
                doc.insertBeforeEnd(bodyElement, "<div alignment = 'left' font color = 'red'>" + buddyName + ": " + msg + "</div>");
        } catch (BadLocationException ble) {
            System.out.println("y tho");
            ble.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageBox.repaint();
    }

    void maybeToggleSendButton() {
        if(messageField.getText().trim().isEmpty())
            sendButton.setEnabled(false);
        else
            sendButton.setEnabled(true);
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

    void acceptFile(String filename, String filesize, String fromUsername) {
        String[] options = {"Accept", "Deny"};
        if(JOptionPane.showOptionDialog(this, "Accept file: " + filename + " of size: " + filesize + "?", "Buddy request!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]) == 0) {
            client.send("ACCEPT_FILE " + fromUsername);
            client.receiveFile(filename, Long.getLong(filesize));
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("SEND")) {
            addMessage(messageField.getText(), true);
            client.send("MESSAGE " + buddyName.trim() + " " + client.cts.ID.trim() + " " + messageField.getText().trim().replaceAll("\n", "<br>")); //add HTML line breaks in place of newlines
            messageField.setText(""); //clear the text field.
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        maybeToggleSendButton();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        maybeToggleSendButton();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        maybeToggleSendButton();
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }


    @Override
    public void drop(DropTargetDropEvent dtde) {
        System.out.println("Drop detected.");
        List<File> fileList;
        Transferable transferableData;

        transferableData = dtde.getTransferable();
        try {
            if(transferableData.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);


                    fileList = (java.util.List<File>) transferableData.getTransferData(DataFlavor.javaFileListFlavor);

                    if(fileList.size() == 1) {
                        fileToSend = fileList.get(0);
                        client.send("FILE_OFFER " + buddyName + " " + client.cts.ID + " " + fileToSend.getName() + " " + fileToSend.length());
                    } else {
                        JOptionPane.showMessageDialog(this, "Only one file may be sent at a time.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
