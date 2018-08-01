package src.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * Model class for a specific term
 */
public class Term {

	private StringProperty termName;
	private SimpleStringProperty termCode;
	private SimpleStringProperty termDescription;
	
	/**
     * Default constructor.
     */
    public Term() {
        this(null, null, null);
    }
    
	/*
	 * constructor
	 * @param name
	 * @param code
	 * @param desc
	 */
	public Term(String name, String code, String desc) {
		this.termName = new SimpleStringProperty(name);
		this.termCode = new SimpleStringProperty(code);
		this.termDescription = new SimpleStringProperty(desc);
	}

	/**
	 * @return the termName
	 */
	public String getTermName() {
		return termName.get();
	}

	/**
	 * @param termName the termName to set
	 */
	public void setTermName(String termName) {
		this.termName.set(termName);
	}

	/**
	 * @return the termCode
	 */
	public String getTermCode() {
		return termCode.get();
	}

	/**
	 * @param termCode the termCode to set
	 */
	public void setTermCode(String termCode) {
		this.termCode.set(termCode);
	}

	/**
	 * @return the termDescription
	 */
	public String getTermDescription() {
		return termDescription.get();
	}

	/**
	 * @param termDescription the termDescription to set
	 */
	public void setTermDescription(String termDescription) {
		this.termDescription.set(termDescription);
	}

	public StringProperty termNameProperty() {
		return termName;
	}
	
	public StringProperty termCodeProperty() {
		return termCode;
	}
	
	public StringProperty termDescProperty() {
		return termDescription;
	}

}
