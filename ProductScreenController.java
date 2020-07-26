package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Optional;


/**
 *  controller for product screen
 */
public class ProductScreenController{

    public Text productTitleText;
    public TextField inventoryField;
    public TextField priceField;
    public TextField maxField;
    public TextField minField;
    public TableColumn<Part, Integer> allPartsID;
    public TableColumn<Part, String> allPartsPartName;
    public TableColumn<Part, Integer> allPartsInventory;
    public TableColumn<Part, Double> allPartsPrice;
    public TableView<Part> allParts;
    public TableView<Part> productParts;
    public TableColumn<Part, Integer> productPartsId;
    public TableColumn<Part, String> productPartsName;
    public TableColumn<Part, Integer> productPartsInventory;
    public TableColumn<Part, Double> productPartsPrice;
    public TextField nameField;
    public Button saveButton;
    public TextField idField;
    public Button cancelButton;
    public TextField partSearchField;
    private Product temporaryProduct;
    private final Alert productAlert = new Alert(Alert.AlertType.WARNING);
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private int min;
    private int max;
    private double price;
    private int stock;
    private boolean validPart;
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;

    /**
     * initializes product screen
     */
    public void initialize(){
        temporaryProduct = new Product(1,"",1,1,1,1);
        String addModify = MainScreenController.getAddModify();
        productTitleText(addModify);
        allParts.setEditable(true);
        allParts.setPlaceholder(new Label("No Parts Currently in Inventory"));
        allPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        allParts.setItems(Inventory.getAllParts());
        productParts.setEditable(true);
        productParts.setPlaceholder(new Label("No Parts Currently Associated with Product"));
        productPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * changes title text at the top of the screen to reflect if adding or modifying product
     * @param addModify is used to indicate whether adding or modifying the product
     */
    public void productTitleText(String addModify)
    {
        if (addModify.equalsIgnoreCase("add")){
            productTitleText.setText("Add Product");
        }
        else {
            productTitleText.setText("Modify Product");
        }
    }

    /**
     * causes confirmation alert to trigger, if confirmed, prior actions are cancelled
     * @param actionEvent
     */
    public void cancelClick(ActionEvent actionEvent) {
        confirmationAlert.setTitle("Cancel");
        confirmationAlert.setHeaderText("You are about to cancel actions");
        confirmationAlert.setContentText("Would you like to continue?");
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if (confirmation.get() == ButtonType.OK) {
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * validates that product has a name, price, and inventory, triggers an error message if not
     * @return true if any of the input boxes are empty
     */
    private boolean inputBoxesEmpty() {
        if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || inventoryField.getText().isEmpty()) {
            productAlert.setTitle("Invalid Product");
            productAlert.setHeaderText("Products must have a name, price, and inventory level");
            productAlert.setContentText("Please fix your submitted product");
            productAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * validates that there is at least one associated part with the product, triggers an error message if not
     * @return true if the product has no associated parts
     */
    private boolean noParts() {
        if (associatedParts.isEmpty()) {
            productAlert.setTitle("Invalid Product");
            productAlert.setHeaderText("Products must have at least one associated part");
            productAlert.setContentText("Please fix your submitted product");
            productAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * validates that minimum is less than maximum, triggers an error message if not
     * @return true if minimum is not les than maximum
     */
    private boolean minNotLessMax() {
        if (min >= max) {
            productAlert.setTitle("Invalid Product");
            productAlert.setHeaderText("Minimum must be less than the maximum");
            productAlert.setContentText("Please fix your submitted product");
            productAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * validates that inventory is between the min and max, triggers an error message if not
     * @return true if the inventory is out of the bounds of the min and max
     */
    private boolean inventoryOutsideMinMax() {
        if (Integer.parseInt(inventoryField.getText()) > max ||
                Integer.parseInt(inventoryField.getText()) < min) {
            productAlert.setTitle("Invalid Product");
            productAlert.setHeaderText("Inventory must be between the minimum and maximum");
            productAlert.setContentText("Please fix your submitted product");
            productAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * validates that the price of the product is greater than the sum of the price of its parts,
     * triggers an error message if not
     * @return true if the price is less than the sum of the parts
     */
    private boolean priceLessPartPrice () {
        double partPriceSum = 0;
        for (Part currentPart : associatedParts) {
            partPriceSum += currentPart.getPrice();
        }
        if (partPriceSum > price) {
            productAlert.setTitle("Invalid Product");
            productAlert.setHeaderText("The price of the product must be at least as much as the sum of its parts");
            productAlert.setContentText("Please fix your submitted product");
            productAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * creates a new product with the information from the text fields and adds it to the inventory
     * @return true if the product was succesfully added to the inventory
     */
    private boolean addProduct () {
        if (inputBoxesEmpty()) {
            return false;
        }
        name = nameField.getText();
        if (MainScreenController.getAddModify().equalsIgnoreCase("add")){
             id = MainScreenController.getProductIDCount() + 1;
        }
        else {
            id = temporaryProduct.getId();
        }
        Product createdProduct = new Product(id,name,price,stock,min,max);
        for (Part currentPart : associatedParts) {
            createdProduct.addAssociatedPart(currentPart);
        }
        if (noParts() || minNotLessMax() || inventoryOutsideMinMax() ||
                priceLessPartPrice()) {
            return false;
        }
        Inventory.addProduct(createdProduct);
        return true;
    }

    /**
     * verifies that price, stock,min, max, and machine id are all numbers, otherwise returns an error message,
     * sets min, max, price, and stock variables, if min or max fields are empty, sets those values in the product to 0
     * @return true if price, stock, min, and max were able to be set and machine id is a number
     */
    private boolean textFieldHandling() {
        try {
            if (minField.getText().isEmpty()) {
                min = 0;
            }
            else {
                min = Integer.parseInt(minField.getText());
            }
            if (maxField.getText().isEmpty()) {
                max = 0;
            }
            else {
                max = Integer.parseInt(maxField.getText());
            }
            price = Double.parseDouble(priceField.getText());
            stock = Integer.parseInt(inventoryField.getText());
            return true;
        }
        catch (NumberFormatException e) {
            productAlert.setTitle("Invalid Product");
            productAlert.setHeaderText("Price, stock, minimum, and maximum must all be numbers");
            productAlert.setContentText("Please fix your submitted product");
            productAlert.showAndWait();
            return false;
        }
    }

    /**
     * runs the addPart function, if modifying then deletes prior copy of the product,
     * closes window afterwards
     * @param actionEvent
     */
    public void saveClick(ActionEvent actionEvent) {
        textFieldHandling();
        if (MainScreenController.getAddModify().equalsIgnoreCase("add")){
            if(addProduct()) {
                MainScreenController.setProductIDCount(id);
                Stage currentStage = (Stage) saveButton.getScene().getWindow();
                currentStage.close();
            }
        }
        else {
            if(addProduct()) {
                Inventory.deleteProduct(temporaryProduct);
                Stage currentStage = (Stage) saveButton.getScene().getWindow();
                currentStage.close();
            }
        }
    }

    /**
     * triggers a confirmation alert, when confirmed, deletes the selected part,
     * if no part is selected it triggers an alert and will not allow the action to progress
     * @param actionEvent
     */
    public void deleteClick(ActionEvent actionEvent) {
        Part selectedPart = productParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            productAlert.setTitle("Invalid Action");
            productAlert.setHeaderText("No part selected");
            productAlert.setContentText("Please select a part to delete it");
            productAlert.showAndWait();
            return;
        }
        confirmationAlert.setTitle("Delete Part");
        confirmationAlert.setHeaderText("You are about to delete the selected part");
        confirmationAlert.setContentText("Would you like to continue?");
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if(confirmation.get() == ButtonType.OK) {
            associatedParts.remove(selectedPart);
        }
        productParts.setItems(associatedParts);
        productParts.refresh();
    }

    /**
     * adds the selected part to the product, if no part is selected it triggers an alert
     * and will not allow the action to progress
     * @param actionEvent
     */
    public void addClick(ActionEvent actionEvent) {
        Part selectedPart = allParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            productAlert.setTitle("Invalid Action");
            productAlert.setHeaderText("No part selected");
            productAlert.setContentText("Please select a part to add it");
            productAlert.showAndWait();
            return;
        }
        associatedParts.add(selectedPart);
        productParts.setItems(associatedParts);
        productParts.refresh();
    }

    /**
     * if text is in the search box, it will search for the whole text regardless of case,
     * it will not do partial phrases, but will complete searches on part name or id, if text box is empty,
     * the whole part list will repopulate
     * @param actionEvent
     */
    public void searchClick(ActionEvent actionEvent) {
        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();
        String searchPart = partSearchField.getText();
        if (searchPart.isEmpty()) {
            allParts.setItems(Inventory.getAllParts());
            allParts.setPlaceholder(new Label("No Parts Currently in Inventory"));
        }
        else {
            for (Part currentPart : Inventory.getAllParts()) {
                if (searchPart.equalsIgnoreCase(currentPart.getName()) ||
                        searchPart.equalsIgnoreCase(Integer.toString(currentPart.getId()))) {
                    partSearchResults.add(currentPart);
                }
            }
            allParts.setPlaceholder(new Label("No Parts Matched Your Search"));
            allParts.setItems(partSearchResults);
        }
    }

    /**
     * populates temporary product with location of selected product, populates text boxes with information from the
     * selected product and adds the parts from the selected part to the associatedParts list
     * @param selectedProduct
     */
    public void productTransfer(Product selectedProduct) {
        temporaryProduct = selectedProduct;
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
        idField.setText(String.valueOf(temporaryProduct.getId()));
        nameField.setText(temporaryProduct.getName());
        inventoryField.setText(String.valueOf(temporaryProduct.getStock()));
        priceField.setText(String.valueOf(temporaryProduct.getPrice()));
        maxField.setText(String.valueOf(temporaryProduct.getMax()));
        minField.setText(String.valueOf(temporaryProduct.getMin()));
        productParts.setItems(associatedParts);
    }
}