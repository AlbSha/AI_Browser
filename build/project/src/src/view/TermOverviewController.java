package src.view;

import org.controlsfx.control.textfield.TextFields;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import src.MainApp;
import src.model.Term;

public class TermOverviewController {

	@FXML
	private TextField searchField;
	@FXML
	private Button searchButton;
	@FXML
	private TableView<Term> termTable;
	@FXML
	private TableColumn<Term, String> nameColumn;
	@FXML
	private TableColumn<Term, String> codeColumn;
	@FXML
	private TableColumn<Term, String> descColumn;

	@FXML
	private TextArea termNameLabel;
	@FXML
	private TextArea termCodeLabel;
	@FXML
	private TextArea termDescriptionLabel;

	// Reference to the main application.
	private MainApp mainApp;
	
	//track if the user is typing or not in the search field
	private boolean  addedBySelection = false;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Initialize the person table with the 3 columns.
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().termNameProperty());
		codeColumn.setCellValueFactory(cellData -> cellData.getValue().termCodeProperty());
		descColumn.setCellValueFactory(cellData -> cellData.getValue().termDescProperty());
		
		//clear term details
		showTermDetails(null);
		
		//listen for selection changes on the latest terms table view so to update the details
		termTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue)-> showTermDetails(newValue));
		
		//searchField.textProperty().addListener(
		//		(observable, oldValue, newValue)-> System.out.println("textfield changed from " + oldValue + " to " + newValue)//showTermDetails(newValue));
        searchButton.setDisable(!addedBySelection);   
        
        //when writing disable the 
		searchField.setOnKeyPressed(e -> {
            addedBySelection = false;
        });

        searchField.setOnKeyReleased(e -> {
            addedBySelection = true;
        });

        searchField.textProperty().addListener(e -> {
            searchButton.setDisable(!addedBySelection);   
        });

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp main) {
		mainApp = main;

		// Add observable list data to the table
		termTable.setItems(mainApp.getTermData());

		// initialize the autocomplete text field with the results
		TextFields.bindAutoCompletion(searchField, mainApp.getSearchData());
	}
	
	/*
	 * Fill all the text fields so to show the info regarding a specific term
	 * if null all is cleared
	 * 
	 * @param Term term
	 */
	private void showTermDetails(Term term) {
		
		if (term != null) {
	        // Fill the labels with info from the person object.
	        termNameLabel.setText(term.getTermName());
	        termCodeLabel.setText(term.getTermCode());
	        termDescriptionLabel.setText(term.getTermDescription());

	    } else {
	        // Person is null, remove all the text.
	        termNameLabel.setText("");
	        termCodeLabel.setText("");
	        termDescriptionLabel.setText("");
	    }
	}
	
	/**
	 * Called when the user clicks the search button.
	 */
	@FXML
	private void handleSearchTerm() {
		
		if(searchField.getText().length()>0) {
			
			for(Term t : mainApp.getTermData()) {
				if(t.getTermName().equals(searchField.getText())) {
					showTermDetails(t);
					break;
				}
			}
		}
	}
	
	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected term.
	 */
	@FXML
	private void handleEditTerm() {
	    Term selectedTerm = termTable.getSelectionModel().getSelectedItem();
	    if (selectedTerm != null) {
	        boolean saveClicked = mainApp.showTermEditDialog(selectedTerm);
	        if (saveClicked) {
	            showTermDetails(selectedTerm);
	        }

	    } else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Term Selected");
	        alert.setContentText("Please select a term in the table.");

	        alert.showAndWait();
	    }
	}
	
	/*
	 * Called when the user click on the delete button
	 */
	@FXML
	private void handleDeleteTerm() {
		int selectedIndex = termTable.getSelectionModel().getSelectedIndex();
		
		//if selection made
		if(selectedIndex>=0)
			termTable.getItems().remove(selectedIndex);
		else {
			
			// Nothing selected show an alert warning
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("No Selection");
	        alert.setHeaderText("No Term Selected");
	        alert.setContentText("Please select a term in the table.");

	        alert.showAndWait();
		}
	}
	
}
