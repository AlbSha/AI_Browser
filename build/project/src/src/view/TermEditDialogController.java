package src.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.model.Term;

/*
 * Dialog to edit the details of the term
 */
public class TermEditDialogController {

	@FXML
    private TextField termNameField;
    @FXML
    private TextField termCodeField;
    @FXML
    private TextField termDescriptionField;


    private Stage dialogStage;
    private Term term;
    private boolean saveClicked = false;
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Sets the term to be edited in the dialog.
     * 
     * @param person
     */
    public void setTerm(Term term) {
        this.term = term;

        termNameField.setText(term.getTermName());
        termCodeField.setText(term.getTermCode());
        termDescriptionField.setText(term.getTermDescription());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSaveClicked() {
        return saveClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleSave() {
    	
        if (isInputValid()) {
        	
            term.setTermName(termNameField.getText());
            term.setTermCode(termCodeField.getText());
            term.setTermDescription(termDescriptionField.getText());

            saveClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
    	
        String errorMessage = "";

        if (termNameField.getText() == null || termNameField.getText().length() == 0) {
            errorMessage += "No valid term name!\n"; 
        }
        if (termCodeField.getText() == null || termCodeField.getText().length() == 0) {
            errorMessage += "No valid last name!\n"; 
        }
        if (termDescriptionField.getText() == null || termDescriptionField.getText().length() == 0) {
            errorMessage += "No valid street!\n"; 
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
