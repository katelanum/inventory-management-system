package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;


/**
 * main screen controller
 */
public class MainScreenController extends Application{



    private static String addModify = "";
    public TableView<Part> partView;
    public Button exit;
    public TableColumn<Part, Integer> partId;
    public TableColumn<Part, String> partName;
    public TableColumn<Part, Integer> partInventoryLevel;
    public TableColumn<Part, Double> partCost;
    public TextField partSearchField;
    public TextField productSearchField;
    public TableView<Product> productView;
    public TableColumn<Product, Integer> productId;
    public TableColumn<Product, String> productName;
    public TableColumn<Product, Integer> productInventoryLevel;
    public TableColumn<Product, Double> productCost;
    private static int partIDCount = 0;
    private static int productIDCount = 0;
    private final Alert mainScreenAlert = new Alert(Alert.AlertType.WARNING);
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    /**
     * searches for any parts that match the text input into the partSearchField text field, then the partView is
     * populated with results, if the search box is empty then the partView is populated with all parts
     * @param actionEvent clicking the search button next to the partSearchField
     */
    public void partSearch(ActionEvent actionEvent) {
        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();
        String searchPartString = partSearchField.getText();
        if (searchPartString.isEmpty()) {
            partView.setItems(Inventory.getAllParts());
            partView.setPlaceholder(new Label("No Parts Currently in Inventory"));
        }
        else {
            for (Part currentPart : Inventory.getAllParts()){
                if (searchPartString.equalsIgnoreCase(currentPart.getName()) ||
                        searchPartString.equalsIgnoreCase(Integer.toString(currentPart.getId()))){
                    partSearchResults.add(currentPart);
                }
            }
            partView.setPlaceholder(new Label("No Parts Matched Your Search"));
            partView.setItems(partSearchResults);
        }
    }

    /**
     * searches for any products that match the text input into the productSearchField text field, then the
     * productView is populated with results, if the search box is empty then the productView is populated with
     * all products
     * @param actionEvent clicking the search button next to the productSearchField
     */
    public void productSearch(ActionEvent actionEvent) {
        ObservableList<Product> productSearchResults = FXCollections.observableArrayList();
        String searchProduct = productSearchField.getText();
        if (searchProduct.isEmpty()) {
            productView.setItems(Inventory.getAllProducts());
            productView.setPlaceholder(new Label("No Products Currently in Inventory"));
        }
        else {
            for (Product currentProduct : Inventory.getAllProducts()) {
                if (searchProduct.equalsIgnoreCase(currentProduct.getName()) ||
                        searchProduct.equalsIgnoreCase(Integer.toString(currentProduct.getId()))) {
                    productSearchResults.add(currentProduct);
                }
            }
            productView.setPlaceholder(new Label("No Products Matched Your Search"));
            productView.setItems(productSearchResults);
        }
    }

    /**
     * deletes the part that is selected in the partView
     * @param actionEvent clicking the delete button under the partView
     */
    public void partDelete(ActionEvent actionEvent) {
        confirmationAlert.setTitle("Delete Part");
        confirmationAlert.setHeaderText("You are about to delete the selected part");
        confirmationAlert.setContentText("Would you like to continue?");
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if(confirmation.get() == ButtonType.OK) {
            Part selectedPart = partView.getSelectionModel().getSelectedItem();
            Inventory.getAllParts().remove(selectedPart);
        }
    }

    /**
     * deletes the product that is selected in the productView
     * @param actionEvent clicking the delete button under productView
     */
    public void productDelete(ActionEvent actionEvent) {
        confirmationAlert.setTitle("Delete Part");
        confirmationAlert.setHeaderText("You are about to delete the selected product");
        confirmationAlert.setContentText("Would you like to continue?");
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if(confirmation.get() == ButtonType.OK) {
            Product selectedProduct = productView.getSelectionModel().getSelectedItem();
            Inventory.getAllProducts().remove(selectedProduct);
        }
    }

    /**
     * pulls up the modify part screen for the part selected on the partView
     * @param actionEvent clicking the modify button under partView
     * @throws IOException if there is no selected part
     */
    public void partModify(ActionEvent actionEvent) throws IOException{
        Part selectedPart = partView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            mainScreenAlert.setTitle("Invalid Action");
            mainScreenAlert.setHeaderText("No part selected");
            mainScreenAlert.setContentText("Please select a part to modify it");
            mainScreenAlert.showAndWait();
            return;
        }
        Stage secondaryStage;
        addModify = "modify";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PartScreen.fxml"));
        secondaryStage = loader.load();
        secondaryStage.show();
        PartScreenController partController = loader.getController();
        partController.partHandler(selectedPart);
        partView.refresh();
    }

    /**
     * pulls up the modify product screen for the product selected on the productView
     * @param actionEvent clicking the modify button under productView
     * @throws IOException if there is no selected product
     */
    public void productModify(ActionEvent actionEvent) throws IOException{
        Product selectedProduct = productView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            mainScreenAlert.setTitle("Invalid Action");
            mainScreenAlert.setHeaderText("No product selected");
            mainScreenAlert.setContentText("Please select a product to modify it");
            mainScreenAlert.showAndWait();
            return;
        }
        Stage secondaryStage;
        addModify = "modify";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductScreen.fxml"));
        secondaryStage = loader.load();
        secondaryStage.show();
        ProductScreenController productController = loader.getController();

        productController.productTransfer(selectedProduct);
        productView.refresh();
    }

    /**
     * pulls up the add part screen
     * @param actionEvent clicking the add button under the partView
     * @throws IOException
     */
    public void partAdd(ActionEvent actionEvent) throws IOException {
        addModify = "add";
        Stage loader = FXMLLoader.load(getClass().getResource("PartScreen.fxml"));
        loader.show();
    }

    /**
     * pulls up the add product screen
     * @param actionEvent clicking the add button under the productView
     * @throws IOException
     */
    public void productAdd(ActionEvent actionEvent) throws IOException{
        addModify = "add";
        Stage loader = FXMLLoader.load(getClass().getResource("ProductScreen.fxml"));
        loader.show();
    }

    public void mainScreenExit(ActionEvent actionEvent) throws IOException{
        Stage currentStage = (Stage) exit.getScene().getWindow();
        currentStage.close();
    }

    /**
     * starts the main screen
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage loader = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage = loader;
        primaryStage.show();
    }

    /**
     * initializes the main controller
     */
    public void initialize(){
        partView.setEditable(true);
        partView.setPlaceholder(new Label("No Parts Currently in Inventory"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partView.setItems(Inventory.getAllParts());
        productView.setEditable(true);
        productView.setPlaceholder(new Label("No Products Currently in Inventory"));
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        productView.setItems(Inventory.getAllProducts());
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static String getAddModify() {
        return addModify;
    }

    public void setAddModify(String addModify) {
        this.addModify = addModify;
    }

    public static int getPartIDCount() {
        return partIDCount;
    }

    public static void setPartIDCount(int partIDCount) {
        MainScreenController.partIDCount = partIDCount;
    }

    public static int getProductIDCount() {
        return productIDCount;
    }

    public static void setProductIDCount(int productIDCount) {
        MainScreenController.productIDCount = productIDCount;
    }
}