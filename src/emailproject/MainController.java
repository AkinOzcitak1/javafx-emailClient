package emailproject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

public class MainController implements Initializable {

    @FXML
    private Button reply;
    @FXML
    private Button composeNew;

    @FXML
    private TableColumn<Info, String> senderName;
    @FXML
    private TableColumn<Info, String> subject;
    @FXML
    private TableColumn<Info, String> date;
    @FXML
    private TableColumn<Info, String> attachment;
    private Text currentHost;

    @FXML
    private Button enterHost;
    @FXML
    private TableView<Info> table;
    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    private RadioButton imap;
    @FXML
    private RadioButton pop3;
    List<Info> infosList = new LinkedList();
    ArrayList<String> messages = new ArrayList<String>();
    ObservableList<Info> infos;
    @FXML
    private TextArea message;
    public String toReply;
    public String subjectReply;
    public int id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        composeNew.setDisable(true);
        reply.setDisable(true);

        table.setRowFactory(tv -> {
            TableRow<Info> row = new TableRow<>();
            row.setOnMouseClicked(event2 -> {
                if (event2.getClickCount() == 1 && (!row.isEmpty())) {
                    Info rowData = row.getItem();
                    message.setText(rowData.getContent());
                    toReply = rowData.getSenderName();
                    subjectReply = rowData.getSubject();
                    id = table.getSelectionModel().getSelectedIndex();
                }
            });
            return row;
        });

    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == enterHost) {
            composeNew.setDisable(false);
            reply.setDisable(false);
            enterHost.setDisable(true);

            Properties props = new Properties();
            if (imap.isSelected()) {
                props.put("mail.store.protocol", "imaps");
            } else if (pop3.isSelected()) {
                props.put("mail.store.protocol", "pop3");
            }

            Session session = Session.getInstance(props, null);

            try {
                if (imap.isSelected()) {
                    Store store = session.getStore("imaps");
                    store.connect("imap.gmail.com", username.getText(), password.getText());
                    System.out.println(store);

                    Folder inbox = store.getFolder("Inbox");
                    inbox.open(Folder.READ_ONLY);

                    System.out.println(inbox.getMessageCount());

                    for (int i = 1; i <= inbox.getMessageCount(); i++) {
                        Message msg = inbox.getMessage(i);

                        Address[] in = msg.getFrom();
                        ArrayList<String> infosArray = new ArrayList<String>();
                        for (Address address : in) {
                            infosArray.add(address.toString());
                        }

                        Object content = msg.getContent();
                        if (content instanceof String) {
                            String body = (String) content;
                            System.out.println(body);
                            messages.add(body);

                            infosList.add(new Info(infosArray.get(0), msg.getSubject(), msg.getSentDate() + "", "", body));

                        } else if (content instanceof Multipart) {

                            Multipart mp = (Multipart) content;
                            BodyPart bp = mp.getBodyPart(0);


                            infosList.add(new Info(infosArray.get(0), msg.getSubject(), msg.getSentDate() + "", "[attachment]", "Multipart Content"));

                        }

                        infos = FXCollections.observableArrayList(infosList);

                        table.setItems(infos);
                        senderName.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).senderNameProperty().getName()));
                        subject.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).subjectProperty().getName()));
                        date.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).dateProperty().getName()));
                        attachment.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).attachmentProperty().getName()));
                        table.getColumns().setAll(senderName, subject, date, attachment);

                    }

                } else if (pop3.isSelected()) {

                    Properties props3 = new Properties();
                    props3.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props3.put("mail.pop3.socketFactory.fallback", "false");
                    props3.put("mail.pop3.socketFactory.port", "995");
                    props3.put("mail.pop3.port", "995");
                    props3.put("mail.pop3.host", "pop.gmail.com");
                    props3.put("mail.pop3.user", username.getText());
                    props3.put("mail.store.protocol", "pop3");
                    props3.put("mail.pop3.ssl.protocols", "TLSv1.2");
                    Authenticator auth3 = new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username.getText(), password.getText());
                        }
                    };

                    Session session3 = Session.getDefaultInstance(props3, auth3);

                    Store store3 = session3.getStore("pop3");
                    store3.connect("pop.gmail.com", username.getText(), password.getText());

                    Folder inbox3 = store3.getFolder("Inbox");
                    inbox3.open(Folder.READ_ONLY);

                    for (int i = 1; i <= inbox3.getMessageCount(); i++) {
                        Message msg = inbox3.getMessage(i);

                        Address[] in = msg.getFrom();
                        ArrayList<String> infosArray = new ArrayList<String>();
                        for (Address address : in) {

                            infosArray.add(address.toString());

                        }

                        Object content = msg.getContent();
                        if (content instanceof String) {

                            String body = (String) content;

                            infosList.add(new Info(infosArray.get(0), msg.getSubject(), msg.getSentDate() + "", "", body));

                        } else if (content instanceof Multipart) {

                            Multipart mp = (Multipart) content;
                            BodyPart bp = mp.getBodyPart(0);

                            infosList.add(new Info(infosArray.get(0), msg.getSubject(), msg.getSentDate() + "", "[attachment]", "Multipart Content"));
                        }
                        infos = FXCollections.observableArrayList(infosList);
                        table.setItems(infos);
                        senderName.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).senderNameProperty().getName()));
                        subject.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).subjectProperty().getName()));
                        date.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).dateProperty().getName()));
                        attachment.setCellValueFactory(new PropertyValueFactory<>(infosList.get(0).attachmentProperty().getName()));
                        table.getColumns().setAll(senderName, subject, date, attachment);
                    }
                }

            } catch (Exception mex) {
                mex.printStackTrace();
            }

        } else if (event.getSource() == composeNew) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ComposeNew.fxml"));
            Parent root = loader.load();
            ComposeNewController controller = loader.getController();
            controller.setUsername(username.getText());
            controller.setPassword(password.getText());
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("MyEmailClient");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } else if (event.getSource() == reply) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Reply.fxml"));
            Parent root = loader.load();
            ReplyController controller = loader.getController();
            controller.setUsername(username.getText());
            controller.setPassword(password.getText());
            controller.setTo(toReply);
            controller.setSubject(subjectReply);
            controller.setReid(id);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("MyEmailClient");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }

    }

}
