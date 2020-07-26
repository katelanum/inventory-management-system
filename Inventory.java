package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by katelanum on 4/17/20.
 * the main inventory class consisting of the lists of parts and products
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * searches for the part with an id that matches the id passed in, then returns the matching part
     * @param partId of the part being searched for
     * @return the part being searched for if one is found, otherwise returns null
     */
    public static Part lookupPart(int partId){
       for (Part currentPart : allParts){
           if (currentPart.getId() == partId)
           {
               return currentPart;
           }
       }
        return null;
    }

    /**
     * searches for the product with an id that matches the id passed in, then returns the matching product
     * @param productId of the product being searched for
     * @return the product being searched for if one is found, otherwise returns null
     */
    public static Product lookupProduct(int productId){
        for (Product currentProduct : allProducts) {
            if (currentProduct.getId() == productId) {
                return currentProduct;
            }
        }
        return null;
    }

    /**
     * searches for the part with a name that matches the name passed in, then returns any matching parts
     * @param partName of the part being searched for
     * @return a list of all parts with names that match the one being searched for, otherwise returns null
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        for (Part currentPart : allParts){
            if (currentPart.getName().equalsIgnoreCase(partName))
            {
                matchingParts.add(currentPart);
            }
        }
        if (matchingParts.isEmpty()){
            return null;
        }
        else {
            return matchingParts;
        }

    }

    /**
     * searches for the product with a name that matches the name passed in, then returns any matching products
     * @param productName of the product being searched for
     * @return a list of all product with names that match the one being searched for, otherwise returns null
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        for (Product currentProduct : allProducts){
            if (currentProduct.getName().equalsIgnoreCase(productName))
            {
                matchingProducts.add(currentProduct);
            }
        }
        if (matchingProducts.isEmpty()){
            return null;
        }
        else {
            return matchingProducts;
        }
    }

    public static void updatePart(int index, Part selectedPart){
         allParts.set(index,selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index,selectedProduct);
    }

    public static boolean deletePart(Part selectedPart){
        if (allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean deleteProduct(Product selectedProduct){
        if (allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }

    }

    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}