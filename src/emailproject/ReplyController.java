package emailproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;

public class ReplyController implements Initializable {

    @FXML
    private Button send;
    @FXML
    private Text to;
    @FXML
    private Text subject;
    @FXML
    private TextArea message;
    String username;
    String password;

    final String emailSMTPserver = "smtp.gmail.com";
    final String emailSMTPPort = "587";
    final String mailStoreType = "pop3s";

    public int reid;

    public void setReid(int reid) {
        this.reid = reid;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTo(String to) {
        this.to.setText(to);
    }

    public void setSubject(String subject) {
        this.subject.setText(subject);
    }

    public void reply() throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", emailSMTPserver);
        props.put("mail.smtp.port", emailSMTPPort);

        try {
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };
            Session session = Session.getInstance(props, auth);

            Store mailStore = session.getStore(mailStoreType);
            mailStore.connect(emailSMTPserver, username, password);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.getMessages();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(System.in));

            for (int i = 0; i < messages.length; i++) {
                Message emailMessage = messages[i];

            }

            int emailNo = reid;

            Message emailMessage
                    = folder.getMessage(emailNo);

            Message mimeMessage = new MimeMessage(session);
            mimeMessage = (MimeMessage) emailMessage.reply(false);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setText(message.getText());
            mimeMessage.setSubject("RE: " + mimeMessage.getSubject());
            mimeMessage.addRecipient(Message.RecipientType.TO,
                    emailMessage.getFrom()[0]);

            Transport.send(mimeMessage);
            System.out.println("Email message "
                    + "replied successfully.");

            folder.close(false);
            mailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in replying email.");
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws AddressException, MessagingException {
        if (event.getSource() == send) {

            reply();
        }
    }

}
