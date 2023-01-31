package emailproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Info {

    private StringProperty senderName;
    private StringProperty subject;
    private StringProperty attachment;
    private StringProperty date;
    private StringProperty content;

    public Info(String senderName, String subject, String date, String attachment, String content) {
        setSenderName(senderName);
        setSubject(subject);
        setAttachment(attachment);
        setDate(date);
        setContent(content);

    }

    public String getSenderName() {
        return senderNameProperty().get();
    }

    public void setSenderName(String senderName) {
        senderNameProperty().set(senderName);
    }

    public StringProperty senderNameProperty() {
        if (senderName == null) {
            senderName = new SimpleStringProperty(this, "senderName");
        }
        return senderName;
    }

    public String getSubject() {
        return subjectProperty().get();
    }

    public void setSubject(String subject) {
        subjectProperty().set(subject);
    }

    public StringProperty subjectProperty() {
        if (subject == null) {
            subject = new SimpleStringProperty(this, "subject");
        }
        return subject;
    }

    public String getAttachmentt() {
        return attachmentProperty().get();
    }

    public void setAttachment(String attachment) {
        attachmentProperty().set(attachment);
    }

    public StringProperty attachmentProperty() {
        if (attachment == null) {
            attachment = new SimpleStringProperty(this, "attachment");
        }
        return attachment;
    }

    public String getDate() {
        return dateProperty().get();
    }

    public void setDate(String date) {
        dateProperty().set(date);
    }

    public StringProperty dateProperty() {
        if (date == null) {
            date = new SimpleStringProperty(this, "date");
        }
        return date;
    }

    public String getContent() {
        return contentProperty().get();
    }

    public void setContent(String content) {
        contentProperty().set(content);
    }

    public StringProperty contentProperty() {
        if (content == null) {
            content = new SimpleStringProperty(this, "content");
        }
        return content;
    }
}
