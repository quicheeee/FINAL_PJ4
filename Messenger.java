package pj4;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;

/**
 * Messenger Class
 * <p>
 * This class represents a Messenger object. The class is used to write, edit
 * and search messages in messager.ser file.
 *
 * @version 04/10/2023
 * @author Amelia Williams, Meha Kavoori, Anish Puri, Tyler Barnett
 */
public class Messenger {
    private static final String FILENAME = "messages.ser"; // filename for file that holds messages
    private static ArrayList<Conversation> conversations = null; // arrList of conversations

    // method scans through contents of conversations ArrayList and looks for when they aren't null
    // where they are not null, message objects from the messages arrList will be written to the messages.ser file
    public static ArrayList<Conversation> getConversations() {
        if (Messenger.conversations != null)
            return Messenger.conversations;

        ArrayList<Conversation> temp = new ArrayList<Conversation>();

        try {
            File f = new File(FILENAME);
            FileInputStream fis;
            ObjectInputStream ois;

            try {
                fis = new FileInputStream(f);
                ois = new ObjectInputStream(fis);

                while (true) {
                    Conversation conversation = (Conversation) ois.readObject();
                    temp.add(conversation);
                }
            } catch (EOFException ex) {
                System.out.println("File processed.");
            } catch (FileNotFoundException ex2) {
                System.out.println("Message Storage File Created");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Messenger.conversations = temp;
        return Messenger.conversations;
    }

    // writes additional message to a conversation in the message.ser file
    public static void sendNewMessage(User sender, User receiver, String message, Boolean disappearing,
                                      Customer customer, Store store) {
        Seller seller = store.getSeller();

        Conversation conv = findConversation(customer, seller, store);
        if (conv == null) {
            conv = new Conversation(customer, seller, store);
            ArrayList<Conversation> temp = Messenger.getConversations();
            temp.add(conv);
        }
        conv.addMessage(sender, receiver, message, disappearing);

        Messenger.writeMessages();
    }

    //This method identifies and returns a conversation given its customer, seller, and store.
    private static Conversation findConversation(Customer customer, Seller seller, Store store) {
        ArrayList<Conversation> temp = Messenger.getConversations();
        for (Conversation conv : temp) {
            if (conv.equals(new Conversation(customer, seller, store)))
                return conv;
        }
        return null;
    }

    // writes conversations from conversations array to messages.ser file
    public static void writeMessages() {
        try {
            File f = new File(Messenger.FILENAME);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (Conversation conversation : Messenger.conversations) {
                oos.writeObject(conversation);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns list of conversations with an inputted user involved
    public static ArrayList<Conversation> getConversationsForUser(User u) {
        ArrayList<Conversation> temp = Messenger.getConversations();
        ArrayList<Conversation> results = new ArrayList<Conversation>();

        for (Conversation conversation : temp) {
            if (conversation.getCustomer().equals(u) || conversation.getSeller().equals(u)) {
                User other;
                if (conversation.getCustomer().equals(u))
                    other = conversation.getCustomer();
                else
                    other = conversation.getSeller();

                if (!(other.getBlockedUsers().contains(u) || u.getBlockedUsers().contains(other)))
                    results.add(conversation);
            }
        }

        Comparator<Conversation> comparatorCustomer = new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                if (o1.isReadCustomer() == o2.isReadCustomer())
                    return 0;
                if (o1.isReadCustomer() && !o2.isReadCustomer())
                    return 1;
                else
                    return -1;
            }
        };
        Comparator<Conversation> comparatorSeller = new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                if (o1.isReadSeller() == o2.isReadSeller())
                    return 0;
                if (o1.isReadSeller() && !o2.isReadSeller())
                    return 1;
                else
                    return -1;
            }
        };

        if (u instanceof Customer)
            Collections.sort(results, comparatorCustomer);
        else
            Collections.sort(results, comparatorSeller);

        return results;
    }

    //this method returns an array list with messages in a conversation for a given user
    public static ArrayList<Message> getMessagesForUser(Conversation conversation, User user) {
        ArrayList<Message> temp = conversation.getMessagesForUser(user);
        writeMessages(); //to write the read flags
        return temp;
    }

    //this method edits a message in a conversation to the revised content for the user
    public static void editMessage(Conversation conversation, Message m, String content, User user) {
        conversation.updateMessage(m, content, user);
        writeMessages();
    }

    //this method deletes the message in a conversation for a given user
    public static void deleteMessage(Conversation conversation, Message m, User current) {
        conversation.deleteMessage(m, current);
        writeMessages();
    }

    //deletes a conversation for a given user
    public static void deleteConversationsForUser(User user) {
        ArrayList<Conversation> temp = Messenger.getConversations();
        ArrayList<Conversation> removeList = new ArrayList<Conversation>();

        for (Conversation m : temp) {
            if (m.getCustomer().equals(user) || m.getSeller().equals(user))
                removeList.add(m);
        }

        for (Conversation m : removeList) {
            temp.remove(m);
        }
        writeMessages();
    }

    //this method checks if there is ann unread message for the user, called when logged in
    public static boolean existsUnreadMessagesForUser(User user) {
        ArrayList<Conversation> temp = Messenger.getConversationsForUser(user);
        for (Conversation conversation : temp) {
            if (user instanceof Customer) {
                if (!conversation.isReadCustomer())
                    return true;
            }
            if (user instanceof Seller) {
                if (!conversation.isReadSeller())
                    return true;
            }
        }
        return false;
    }

    //this method adds a message to a conversation when a user sends a new message
    public static void addMessageToConversation(Conversation conversation, User sender, String message,
                                                boolean disappearing) {
        User receiver;
        if (sender instanceof Customer)
            receiver = conversation.getSeller();
        else
            receiver = conversation.getCustomer();

        conversation.addMessage(sender, receiver, message, disappearing);
        Messenger.writeMessages();
    }

}

