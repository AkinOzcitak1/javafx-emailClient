package emailproject;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class ComposeNewController implements Initializable {

    @FXML
    private Button send;
    @FXML
    private TextArea message;
    @FXML
    private TextField to;
    @FXML
    private TextField subject;
    String username;
    String password;
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == send) {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.from", username); //javaadvanced52@gmail.com   ruskzisipowbcmad
            Authenticator a = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };

            Session session = Session.getDefaultInstance(props, a);

            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom();
                msg.setRecipients(Message.RecipientType.TO, to.getText());
                msg.setSubject(subject.getText());
                msg.setSentDate(new Date());
                msg.setText(message.getText());
                Transport.send(msg);

            } catch (MessagingException mex) {
                System.out.println("Send failed, exception: " + mex);
            }

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
