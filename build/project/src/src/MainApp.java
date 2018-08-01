package src;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.model.Term;
import src.model.TermListWrapper;
import src.view.ProbabilityStatisticsController;
import src.view.RootLayoutController;
import src.view.TermEditDialogController;
import src.view.TermOverviewController;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	/*
	 * the data as an observable list of terms
	 */
	private ObservableLimitedList<Term> termData = new ObservableLimitedList<>(10);
	private ObservableList<String>searchData =FXCollections.observableArrayList();
	
	/*
	 * Constructor
	 */
	public MainApp() {
		
		// Add some sample data
		termData.add(new Term("Hans", "Muster", "kiwi"));
		searchData.add("Hans");
		termData.add(new Term("Ruth", "Mueller", "kiwi"));
		searchData.add("Ruth");
		termData.add(new Term("Heinz", "Kurz", "kiwi"));
		searchData.add("Heinz");
		termData.add(new Term("Cornelia", "Meier", "kiwi"));
		searchData.add("Cornelia");
		termData.add(new Term("Werner", "Meyer", "kiwi"));
		searchData.add("Werner");
		termData.add(new Term("Lydia", "Kunz", "kiwi"));
		searchData.add("Lydia");
		termData.add(new Term("Anna", "Best", "kiwi"));
		searchData.add("Anna");
		termData.add(new Term("Stefan", "Meier", "kiwi"));
		searchData.add("Stefan");
		termData.add(new Term("Martin", "Mueller", "kiwi"));
		searchData.add("Martin");
		termData.add(new Term("Martin", "Mueller", "kiwi"));
		searchData.add("Martin");
        
	}
	
	/*
	 * Returns the data as ana observable list of terms
	 */
	public ObservableList<Term> getTermData(){
		return termData;
	}
	
	/*
	 * Returns the data as ana observable list of terms
	 */
	public ObservableList<String> getSearchData(){
		return searchData;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AI Browser");
		//this.primaryStage.setMaximized(true);
		this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icons/ai.png")));
		
		initRootLayout();
		
		showTermOverview();
	}
	
	/*
	 * Initialize the root layout
	 */
	public void initRootLayout() {
		try {
			//load the root layout from the fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//show the scene which contains the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			//primaryStage.sizeToScene();
			
			// Give the controller access to the main app.
	        RootLayoutController controller = loader.getController();
	        controller.setMainApp(this);
			
			primaryStage.show();
	        primaryStage.setMinWidth(primaryStage.getWidth());
	        primaryStage.setMinHeight(primaryStage.getHeight());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// Try to load last opened term file.
	    File file = getTermFilePath();
	    if (file != null) {
	        loadTermDataFromFile(file);
	    }
	}
	
	/*
	 * Show the term overview inside the root layout
	 */
	public void showTermOverview() {
		try {
			//load term overview
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TermOverview.fxml"));
            VBox termOverview = (VBox) loader.load();

            // Set term overview into the center of root layout.
            rootLayout.setCenter(termOverview);
            
            //give the controller access to the main app
            TermOverviewController controller = loader.getController();
            controller.setMainApp(this);
            
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Returns the main page
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Opens a dialog to edit details for the specified term. If the user
	 * clicks save, the changes are saved into the provided term object and true
	 * is returned.
	 * 
	 * @param term the term object to be edited
	 * @return true if the user clicked save, false otherwise.
	 */
	public boolean showTermEditDialog(Term term) {
		
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/TermEditDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edit Term");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        TermEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setTerm(term);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return controller.isSaveClicked();
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Returns the term file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getTermFilePath() {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    String filePath = prefs.get("filePath", null);
	    if (filePath != null) {
	        return new File(filePath);
	    } else {
	        return null;
	    }
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file the file or null to remove the path
	 */
	public void setTermFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());

	        // Update the stage title.
	        primaryStage.setTitle("AI Browser - " + file.getName());
	    } else {
	        prefs.remove("filePath");

	        // Update the stage title.
	        primaryStage.setTitle("AI Browser");
	    }
	}
	
	
	/**
	 * Loads term data from the specified file. The current term data will
	 * be replaced.
	 * 
	 * @param file
	 */
	public void loadTermDataFromFile(File file) {
		
	    try {
	    	
	        JAXBContext context = JAXBContext
	                .newInstance(TermListWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Reading XML from the file and unmarshalling.
	        TermListWrapper wrapper = (TermListWrapper) um.unmarshal(file);

	        termData.clear();
	        termData.addAll(wrapper.getTerms());

	        // Save the file path to the registry.
	        setTermFilePath(file);

	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not load data");
	        alert.setContentText("Could not load data from file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

	/**
	 * Saves the current term data to the specified file.
	 * 
	 * @param file
	 */
	public void saveTermDataToFile(File file) {
		
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(TermListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        // Wrapping our term data.
	        TermListWrapper wrapper = new TermListWrapper();
	        wrapper.setTerms(termData);

	        // Marshalling and saving XML to the file.
	        m.marshal(wrapper, file);

	        // Save the file path to the registry.
	        setTermFilePath(file);
	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}
	
	/**
	 * Opens a dialog to show search term average probability on prediction statistics.
	 */
	public void showProbabilityStatistics() {
		
	    try {
	        // Load the fxml file and create a new stage for the popup.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/PredictionStatistics.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Prediction Statistics");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the persons into the controller.
	        ProbabilityStatisticsController controller = loader.getController();
	        
	        //populate the chart
	        controller.setTermData();

	        dialogStage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
