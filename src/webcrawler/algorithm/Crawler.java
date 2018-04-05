/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm;

import java.util.LinkedList;
import java.util.List;

/**
 *This class defines the general structure of the crawler.
 * @author bruno & mivian
 */
public abstract class Crawler {
    
    protected static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    
    protected List<String> papers;
    
    /**
     * Constructor method of this class.
     */
    public Crawler(){
        this.papers = new LinkedList<>();
    }
    
    /**
     * Return the USER_AGENT attribute for others classes. 
     * It is used to make the web server to think the robot is a normal web browser.
     */
    public String getUSER_AGENT(){
        return USER_AGENT;
    }
    
    /**
     * Initialize the links search. 
     * @param url Url reference to conference or journal homepage that contains all proceedings or volumes history.
     */
    public void search(String url){
        if(this instanceof ConferenceCrawler){
            ((ConferenceCrawler) this).searchProceedings(url);
        }else{
            ((JournalCrawler) this).searchVolumes(url);
        }
        searchPapers();
        System.out.println("Total de artigos encontrados: " + this.papers.size());
    }
    
    /**
     * Abstract method used to initialize the papers search. 
     */
    protected abstract void searchPapers();
    
//    public abstract void processPaper();
    
}
