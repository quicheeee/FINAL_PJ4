package pj4;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class Message
 * The message class represents individual messages sent between different users.
 * It takes String objects and converts the text to Message objects which can be stored.
 *
 * @author Amelia Williams, Meha Kavoori, Anish Puri, Tyler Barnett
 *
 * @version 04/10/2023
 */
// following class implements Serializabe and Comparable<Message> so that the messages can be compared
public class Message implements Serializable {
    private User sender; // represents user who sent message
    private User receiver; // represents user who recieved message
    private String message; // represents the message's text
    private String createDate; // represents the date message was sent
    private boolean disappearing; // represents whether or not the message is one that dissapears

    private User deletedFor = null;

    private static final long serialVersionUID = 2L;

    public Message(User sender, User receiver, String message, boolean disappearing) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.disappearing = disappearing;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.createDate = dtf.format(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (!getSender().equals(message1.getSender())) return false;
        if (!getReceiver().equals(message1.getReceiver())) return false;
        if (!getCreateDate().equals(message1.getCreateDate())) return false;
        return getMessage().equals(message1.getMessage());
    }

    /*public boolean related(Object o) {
        if (this == o) return false;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (!getSender().equals(message1.getSender())) return false;
        if (!getReceiver().equals(message1.getReceiver())) return false;
        if (!getCreateDate().equals(message1.getCreateDate())) return false;
        return getMessage().equals(message1.getMessage());
    }
*/
    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public boolean isDisappearing() {
        return disappearing;
    }

    public User getDeletedFor() {
        return deletedFor;
    }

    public void setDeletedFor(User deletedFor) {
        this.deletedFor = deletedFor;
    }

}
