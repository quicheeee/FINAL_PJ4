package pj4;

import java.io.*;
import java.util.*;

public class Store implements Serializable {
    private Seller seller;
    private String storeName;

    private static final long serialVersionUID = -664171850626483574L;


    public static ArrayList<Store> getAllStores() {
        ArrayList<Store> all = new ArrayList<Store>();
        for (User u : User.getUsers()) {
            if (u instanceof Seller) {
                all.addAll(((Seller) u).getListOfStores());
            }
        }
        return all;
    }

    public static ArrayList<Store> getAllStoresForUser (User u) {
        ArrayList<Store> all = new ArrayList<Store>();

        for (User temp : User.getUsers()) {
            if (temp instanceof Seller) {
                if (!(temp.getBlockedUsers().contains(u) || u.getBlockedUsers().contains(temp)))
                    all.addAll(((Seller) temp).getListOfStores());
            }
        }
        return all;
    }

    public Store(Seller seller, String storeName) {

        this.seller = seller;
        this.storeName = storeName;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        return getStoreName().equals(store.getStoreName());
    }

    public Seller getSeller() {
        return seller;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public static boolean newStore(String name, Seller seller) {
        File list = new File("stores.ser");
        ArrayList<Store> stores = Store.listStores(list, Store.getNumStoresCreated());
        for(Store s: stores) {
            if(s.getStoreName().equals(name)){
                System.out.println("Store Name Taken");
                return false;
            }
        }
        Store store = new Store(seller, name);
        stores.add(store);
        Store.setNumStoresCreated(Store.getNumStoresCreated() + 1);
        Store.writeStores(stores, list);
        return true;
    }

    public static ArrayList<Store> listStores(File f, int fileLength) {
        ArrayList<Store> stores = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            for (int i = 0; i < fileLength; i++){
                Store st = (Store) ois.readObject();
                stores.add(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stores;
    }

    public static int getNumStoresCreated(){
        File f = new File("numStoresCreated.txt");
        try{
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            return Integer.parseInt(bfr.readLine());
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setNumStoresCreated(int i){
        File f = new File ("numStoresCreated.txt");
        try{
            FileWriter fw = new FileWriter(f);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(i);
            pw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeStores(ArrayList<Store> stores, File f){
        try{
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(Store s1: stores) {
                oos.writeObject(s1);
            }
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
