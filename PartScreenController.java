package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * controller for the part screen
 */
public class PartScreenController {


    public Pane partPane;
    public Text addModifyIdentifier;
    public TextField partName;
    public TextField inventoryLevel;
    public TextField price;
    public TextField maximum;
    public TextField minimum;
    public TextField machineIdCompanyName;
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public Text machineIdCompanyNameLong;
    public ToggleGroup inHouseOutsource = new ToggleGroup();
    public Button partPageExit;
    public Button partSave;
    public TextField id;
    public Part tempPart;
    private final Alert partAlert = new Alert(Alert.AlertType.WARNING);
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private int idNumber;
    private String name;
    private double priceCost;
    private int stock;
    private int min;
    private int max;

    /**
     * sets the text for if the part is being added or modified
     */
    public void addModifyMethod() {
        String addModify = MainScreenController.getAddModify();
        if (addModify.equalsIgnoreCase("add")) {
            addModifyIdentifier.setText("Add Part");
            id.setText("Auto Gen-Disabled");
        } else {
            addModifyIdentifier.setText("Modify Part");
        }
    }

    /**
     * handles the selection of the inhouse radio button, changing the text field for the machine ID and the
     * text around it to prompt users
     * @param actionEvent
     */
    public void partInhouseRadio(ActionEvent actionEvent) {
        machineIdCompanyNameLong.setText("Machine ID");
        machineIdCompanyName.setPromptText("Mach ID");
        inHouseRadio.setSelected(true);
        inHouseRadio.fire();
    }

    /**
     * handles the selection of the outsourced radio button, changing the text field for the company name and the
     * text around it to prompt users
     * @param actionEvent
     */
    public void partOutsourcedRadio(ActionEvent actionEvent) {
        machineIdCompanyNameLong.setText("Company Name");
        machineIdCompanyName.setPromptText("Comp Nm");
        outsourcedRadio.setSelected(true);
        outsourcedRadio.fire();
    }

    /**
     * handles the clicking of the cancel button, triggers a confirmation alert, if confirmed, the part screen is closed
     * @param actionEvent
     */
    public void partCancel(ActionEvent actionEvent) {
        confirmationAlert.setTitle("Cancel");
        confirmationAlert.setHeaderText("You are about to cancel actions");
        confirmationAlert.setContentText("Would you like to continue?");
        Optional<ButtonType> confirmation = confirmationAlert.showAndWait();
        if (confirmation.get() == ButtonType.OK) {
            Stage currentStage = (Stage) partPageExit.getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * checks if minimum is more than the maximum, if so then an error message is triggered
     * @return true if min is more than max
     */
    private boolean minMoreThanMax() {
        if (Integer.parseInt(minimum.getText()) >= Integer.parseInt(maximum.getText())) {
            partAlert.setTitle("Invalid Part");
            partAlert.setHeaderText("Minimum must be less than the maximum");
            partAlert.setContentText("Please fix your submitted part");
            partAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * checks if inventory is more than maximum or less than minimum, if so then an error message is triggered
     * @return true if inventory is outside the bounds of the minimum and maximum
     */
    private boolean inventoryOutsideMinAndMax() {
        if (Integer.parseInt(inventoryLevel.getText()) > Integer.parseInt(maximum.getText()) ||
                Integer.parseInt(inventoryLevel.getText()) < Integer.parseInt(minimum.getText())) {
            partAlert.setTitle("Invalid Part");
            partAlert.setHeaderText("Inventory must be between the minimum and maximum");
            partAlert.setContentText("Please fix your submitted part");
            partAlert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * runs both the minMoreThanMax and inventoryOutsideMinAndMax functions, if inputs to fields passes those, then
     * a new part is created, if simply adding a part the id is one more than the PartIDCount stored in the
     * MainScreenController, if the part is a modified part the id is set to the id of the passed in part,
     * @return true if the part is able to successfully be created
     */
    private boolean partCreator() {
        if (minMoreThanMax() || inventoryOutsideMinAndMax()) {
            return false;
        }
        if (MainScreenController.getAddModify().equalsIgnoreCase("add")) {
           idNumber = MainScreenController.getPartIDCount() + 1;
        }
        if (inHouseRadio.isSelected()) {
            InhousePart addedPart = new InhousePart(idNumber, name, priceCost, stock, min,
                    max, Integer.parseInt(machineIdCompanyName.getText()));
            Inventory.addPart(addedPart);
            return true;
        } else {
            OutsourcedPart addedPart = new OutsourcedPart(idNumber, name, priceCost, stock, min,
                    max, machineIdCompanyName.getText());
            Inventory.addPart(addedPart);
            return true;
        }
    }

    /**
     * handles the actions following the clicking of the save button, runs the validNumericalText function, creates
     * a new part by running the partCreator function, if adding a new part the partIDCount from the
     * MainScreenController would be raised, if modifying an existing part then the prior instance of the part would be
     * deleted, then the window would be closed
     * @param actionEvent
     */
    public void partSave(ActionEvent actionEvent) {
        name = partName.getText();
        if (!validNumericalText()) {
            return;
        }
        if (MainScreenController.getAddModify().equalsIgnoreCase("add")) {
            if (partCreator()) {
                MainScreenController.setPartIDCount(idNumber);
                Stage currentStage = (Stage) partSave.getScene().getWindow();
                currentStage.close();
            }
        }
        else {
            if (partCreator()) {
                Inventory.deletePart(tempPart);
                Stage currentStage = (Stage) partSave.getScene().getWindow();
                currentStage.close();
            }

        }
    }

    /**
     * verifies that all input for price, inventory, minimum, maximum, and machine id are numbers, and sets the values
     * for min, max, priceCost, and stock variables, if any input that should be numerical is not, then an error message
     * appears
     * @return true if variables have been successfully set
     */
    private boolean validNumericalText() {
        if (inHouseRadio.isSelected()) {
            try {
                if (minimum.getText().isEmpty()) {
                    min = 0;
                } else {
                    min = Integer.parseInt(minimum.getText());
                }
                if (maximum.getText().isEmpty()) {
                    max = 0;
                } else {
                    max = Integer.parseInt(maximum.getText());
                }
                priceCost = Double.parseDouble(price.getText());
                stock = Integer.parseInt(inventoryLevel.getText());
                Integer.parseInt(machineIdCompanyName.getText());
                return true;
            } catch (NumberFormatException e) {
                partAlert.setTitle("Invalid Part");
                partAlert.setHeaderText("Price, stock, minimum, maximum, and machine id must all be numbers");
                partAlert.setContentText("Please fix your submitted part");
                partAlert.showAndWait();
                return false;
            }
        }
        else {
            try {
                if (minimum.getText().isEmpty()) {
                    min = 0;
                }
                else {
                    min = Integer.parseInt(minimum.getText());
                }
                if (maximum.getText().isEmpty()) {
                    max = 0;
                }
                else {
                    max = Integer.parseInt(maximum.getText());
                }
                priceCost = Double.parseDouble(price.getText());
                stock = Integer.parseInt(inventoryLevel.getText());
                return true;
            }
            catch (NumberFormatException e) {
                partAlert.setTitle("Invalid Part");
                partAlert.setHeaderText("Price, stock, minimum, and maximum must all be numbers");
                partAlert.setContentText("Please fix your submitted part");
                partAlert.showAndWait();
                return false;
            }
        }
    }

    /**
     * sets tempPart to be a reference to the part passed in, the text boxes are all populated with information
     * from the part passed in, the idNumber variable is set to the id of the part passed in
     * @param receivedPart
     */
    public void partHandler (Part receivedPart){
        tempPart = receivedPart;
        id.setText(String.valueOf(tempPart.getId()));
        idNumber = tempPart.getId();
        partName.setText(tempPart.getName());
        inventoryLevel.setText(Integer.toString(tempPart.getStock()));
        price.setText(Double.toString(tempPart.getPrice()));
        maximum.setText(Integer.toString(tempPart.getMax()));
        minimum.setText(Integer.toString(tempPart.getMin()));
        if (tempPart instanceof OutsourcedPart) {
            machineIdCompanyName.setText(((OutsourcedPart) tempPart).getCompanyName());
            machineIdCompanyNameLong.setText("Company Name");
            outsourcedRadio.setSelected(true);
            outsourcedRadio.fire();
        } else if (tempPart instanceof InhousePart) {
            machineIdCompanyName.setText(String.valueOf(((InhousePart) tempPart).getMachineId()));
            machineIdCompanyNameLong.setText("Machine ID");
            inHouseRadio.setSelected(true);
            inHouseRadio.fire();
        }
    }

    /**
     * initializes the part screen controller
     */
    public void initialize(){
        inHouseRadio.setToggleGroup(inHouseOutsource);
        inHouseRadio.setSelected(true);
        outsourcedRadio.setToggleGroup(inHouseOutsource);
        addModifyMethod();
    }
}