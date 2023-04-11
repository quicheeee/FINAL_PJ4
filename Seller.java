package pj4;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Seller class
 *
 * The Seller class represents a seller object which is a type of user that interacts with
 * users of its type and customers. An intance of the class is created with attributes name,
 * email and password.
 *
 * @author Amelia Williams, Meha Kavoori, Anish Puri, Tyler Barnett
 * @version 4/10/2023
 *
 */
public class Seller extends User
{
    private Customer[] customerList; // Seller's array of Customers

    private ArrayList<Store> listOfStores; // ArrayList of Seller's stores

    private static final long serialVersionUID = 6387730094462016452L;

    // the constructor uses super for some of its fields because it extends User
    // User already contains some of these fields
    public Seller(String name, String email, String password) {
        super(name, email, password);
        listOfStores = new ArrayList<Store>();
    }

    // following method allows for a seller to add a store using a String of the store name
    public void addStore(String storeName) throws IllegalArgumentException {
        ArrayList<Store> allStores = Store.getAllStores();

        for (Store temp : allStores) {
            if (temp.getStoreName().equals(storeName))
                throw new IllegalArgumentException("Duplicate Store Name");
        }

        Store st = new Store(this, storeName);
        listOfStores.add(st);
    }

    // following method sets the customer list as a list
    public void setCustomerList(Customer[] customerList) {
        this.customerList = customerList;
    }

    // following method gets an array list of all the stores that exist
    public ArrayList<Store> getListOfStores() {
        return listOfStores;
    }

    public void addCustomer(Customer c) {
        ArrayList<Customer> arr = new ArrayList<>();
        for (int i = 0; i < customerList.length; i++) {
            arr.set(i, this.customerList[i]);
        }

        arr.add(c);

        for (int i = 0; i < arr.size(); i++) {
            this.customerList[i] = arr.get(i);
        }
    }


    public Customer getCustomer(int index){
        File f = new File("accounts.ser");
        int count = 1;
/*
        for(User u: this.listUsers(f, this.getNumCreated())){
            if(u instanceof Customer) {
                if(!this.getBlockedUsers().contains(u)) {
                    if(!this.getBlockedBy().contains(u)) {
                        if(!u.getInvisibleTo().contains(this)){
                            if (count == index) {
                                return (Customer) u;
                            }
                            count++;
                        }
                    }
                }
            }
        }
*/
        return null;
    }

    /*public String searchCustomer(int customerID) {
        String customerEmail = "Customer Not Found";
        for (int i = 0; i < customerList.length; i++) {
            if (customerList[i].getCustomerID() == (customerID)) {
                customerEmail = customerList[i].getEmail();
            }
        }
        return customerEmail;
    }

     */
    public void displayCustomers(){
        File f = new File("accounts.ser");
        int count = 1;
        for(User u: getUsers()){
            if(u instanceof Customer){
                if(!this.getBlockedUsers().contains(u)) {
/*
                    if(!this.getBlockedBy().contains(u)) {
                        if(!u.getInvisibleTo().contains(this)) {
                            System.out.println(count++ + ". " + u.getEmail());
                        }
                    }
*/
                }
            }
        }
    }

    public boolean equals(Object o){
        if(o instanceof Seller) {
            Seller seller = (Seller) o;
            if(seller.getEmail().equals(this.getEmail()) && seller.getPassword().equals(this.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean canMessage(Object o) {
        if (o instanceof Seller || o instanceof Customer) {
            return true;
        }
        return false;
    }


    // add equals
//        public Message newMessage() //in progress
//        public Message newMessage() //in progress


    }
