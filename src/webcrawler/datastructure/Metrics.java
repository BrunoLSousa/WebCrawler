/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.datastructure;

/**
 *
 * @author bruno
 */
public class Metrics {
    
    private int citationCountPaper;
    private int totalDownloads;

    public Metrics() {
        
    }

    public int getCitation() {
        return citationCountPaper;
    }

    public void setCitation(int citation) {
        this.citationCountPaper = citation;
    }

    public int getDownloads() {
        return totalDownloads;
    }

    public void setDownloads(int downloads) {
        this.totalDownloads = downloads;
    }
    
}
