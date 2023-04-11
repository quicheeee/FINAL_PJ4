package pj4;
import java.io.*;
import java.util.*;
/**
 * User
 *
 * This class creates a basic user for the application, with a name, email, and password associated.
 * It writes the users to file for storage after the program is terminated.
 *
 * @author Amelia Williams, Meha Kavoori, Anish Puri, Tyler Barnett
 *
 * @version 04/10/2023
 *
 */
public class User implements Serializable {
    private String name; //the name of the user
    private String email; //the email address of the user
    private String password; //the password of the user

    private ArrayList<User> blockedUsers; //this is an arrayList of blocked Users
    private ArrayList<String> filters; //this is an arrayList that stores the filters

    private static ArrayList<User> allUsers = null; //this is an arrayList of all users
    private static final long serialVersionUID = -5554757084812506737L;

    //Constructor to create a user given their name, email, and password
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        blockedUsers = new ArrayList<User>();
        filters = new ArrayList<String>();
    }

    //returns the User's email
    public String getEmail() {
        return email;
    }

    //sets the email field to the inputted string
    public void setEmail(String email) {
        this.email = email;
    }

    //gets the User's password
    public String getPassword() {
        return password;
    }

    //sets the User's password
    public void setPassword(String password) {
        this.password = password;
    }

    //returns the User's name
    public String getName() {
        return name;
    }

    //returns an ArrayList of Users blocked by the given User
    public ArrayList<User> getBlockedUsers(){
        return blockedUsers;
    }

    //sets the stored ArrayList of Users blocked by the given User to the inputted ArrayList
    public void setBlockedUsers(ArrayList<User> blockedUsers){
        this.blockedUsers = blockedUsers;
    }

    //this method is used to verify login information when a user signs into the application
    public static User signIn (String email, String password) {
        ArrayList<User> users = getUsers();
        for(User u: users) {
            if(u.getEmail().equals(email) && u.getPassword().equals(password)){
                return u;
            }
            //else if(u.getEmail().equals(email)) {
            //    return -1;
            //}
        }
        return null;
    }

    //a static method used to identify a User object from the associated email address
    public static User findUserWithEmail(String email){
        try{
            ArrayList<User> users = getUsers();

            for(User u: users) {
                if(u.getEmail().equals(email)) {
                    return u;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //a static method that returns an arraylist of sellers that are not blocked
    //whose email address contains a search term inputted by the given sender
    public static ArrayList<User> searchSellerByUser(String search, User sender) {
        ArrayList<User> users = getUsers();
        ArrayList<User> results = new ArrayList<User>();

        for(User u: users) {
            if (u instanceof Seller) {
                if ((!(sender.getBlockedUsers().contains(u) || u.getBlockedUsers().contains(sender)))
                    && (u.getEmail().indexOf(search) != -1))
                    results.add(u);
            }
        }
        return results;
    }

    //a static method that returns an arraylist of customers that are not blocked
    //whose email address contains a search term inputted by the given sender
    public static ArrayList<User> searchCustomerByUser(String search, User sender) {
        ArrayList<User> users = getUsers();
        ArrayList<User> results = new ArrayList<User>();

        for(User u: users) {
            if (u instanceof Customer) {
                if ((!(sender.getBlockedUsers().contains(u) || u.getBlockedUsers().contains(sender)))
                        && (u.getEmail().indexOf(search) != -1))
                    results.add(u);
            }
        }
        return results;
    }

    //a static method that returns an arraylist of customers that are not blocked by the given sender
    public static ArrayList<User> getCustomersByUser(User sender) {
        ArrayList<User> users = getUsers();
        ArrayList<User> results = new ArrayList<User>();

        for(User u: users) {
            if (u instanceof Customer) {
                if (!(sender.getBlockedUsers().contains(u) || u.getBlockedUsers().contains(sender)))
                    results.add(u);
            }
        }
        return results;
    }

    //creates a new user when the appropriate fields are inputted and creates a customer or seller object
    //and then writes the user to file for storage of information
    public static boolean newUser(String name, String emailAddress, String password, String storeName, int userType) {
        ArrayList<User> users = User.getUsers();
        for(User u: users) {
            if(u.getEmail().equals(emailAddress)){
                System.out.println("Email Taken");
                return false;
            }
        }

        User u;
        if(userType == 1) {
            //Customer Account
            u = new Customer(name, emailAddress, password);
            System.out.println("Customer Created");
        } else if (userType == 2) {
            //Seller Account
            u = new Seller(name, emailAddress, password);
            ((Seller) u).addStore(storeName);
            System.out.println("Seller Created");
        } else {
            return false;
        }

        User.allUsers.add(u);
        User.writeUsers(User.allUsers);
        return true;
    }

    public static void addNewStore(Seller seller, String storeName) {
        seller.addStore(storeName);
        User.writeUsers(User.getUsers());
    }

    //given an ArrayList of Users, this method will write them to the file which stores them ("accounts.ser")
    private static void writeUsers(ArrayList<User> users){
        try{
            File f = new File("accounts.ser");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(User u1: users) {
                oos.writeObject(u1);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //this method returns an ArrayList of Users with all of the users in the file "accounts.ser"
    public static ArrayList<User> getUsers() {
        if (User.allUsers != null)
            return  User.allUsers;

        ArrayList<User> users = new ArrayList<>();
        try {
            File f = new File("accounts.ser");
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois;

            try {
                ois = new ObjectInputStream(fis);

                while (true) {
                    User u = (User) ois.readObject();
                    users.add(u);
                }

            } catch (EOFException ex) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        User.allUsers = users;
        return User.allUsers;
    }

    //this method blocks an inputted user
    public void block(User user) {
        this.blockedUsers.add(user);
        writeUsers(User.getUsers());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof User){
            User u = (User) o;
            if(u.getEmail().equals(email) && u.getPassword().equals(password) ) {
                return true;
            }
        }
        return false;
    }

    //this method deletes a user, removing them from the file "accounts.ser"
    public static void deleteUser(User u) {
        Messenger.deleteConversationsForUser(u);

        ArrayList<User> temp = User.getUsers();
        temp.remove(u);
        writeUsers(temp);
    }

    //this method gets the filters that have been applied for the given user
    public ArrayList<String> getFilters() {
        if (filters == null)
            filters = new ArrayList<String>();
        return filters;
    }

    //this method adds a filter to the user
    public void addFilter(String original, String replacement) {
        if((original == null) || (replacement == null))
            throw new IllegalArgumentException("Invalid filter strings");

        filters.add(original);
        filters.add(replacement);
        User.writeUsers(User.getUsers());
    }

    //this method applies the filters that a user has applied
    public String applyFilters(String message) {
        if (filters == null)
            return message;

        for (int i = 0; i < filters.size(); i += 2) {
            message = message.replace(filters.get(i), filters.get(i+1));
        }
        return message;
    }

}
