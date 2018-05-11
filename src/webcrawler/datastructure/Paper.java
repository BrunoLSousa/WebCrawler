/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bruno
 */
public class Paper {
    
    private String title;
    private String publicationYear;
    private String publicationTitle;
    private List<Author> authors;
    private Metrics metrics;
    private String xploreDocumentType;
    private List<Keywords> keywords;
    private String pdfUrl;
    
    public Paper(){
        this.authors = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return publicationYear;
    }

    public void setYear(String year) {
        this.publicationYear = year;
    }

    public String getVehicle() {
        return publicationTitle;
    }

    public void setVehicle(String vehicle) {
        this.publicationTitle = vehicle;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
    public void addAuthor(Author author){
        this.authors.add(author);
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public String getType() {
        return xploreDocumentType;
    }

    public void setType(String type) {
        this.xploreDocumentType = type;
    }

    public List<Keywords> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keywords> keywords) {
        this.keywords = keywords;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
    
}
