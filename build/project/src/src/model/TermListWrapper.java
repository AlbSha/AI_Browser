package src.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of terms. This is used for saving the list of
 * terms to XML.
 */
@XmlRootElement(name = "terms")
public class TermListWrapper {

	private List<Term> terms;

	@XmlElement(name = "term")
	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}
}
