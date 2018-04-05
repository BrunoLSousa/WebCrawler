/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm;

import java.util.LinkedList;
import java.util.List;

/**
 *This class specify the Crawler class. Its goal is to search the conference papers link.
 * @author bruno & mivian
 */
public abstract class ConferenceCrawler extends Crawler{
    
    protected List<String> proceedings;
    
    /**
     * Constructor method of this class.
     */
    public ConferenceCrawler(){
        super();
        this.proceedings = new LinkedList<>();
    }
    
    /**
     * Abstract method used to initialize the search of proceedigns links. 
     * @param url Url reference to conference or journal homepage that contains all proceedings or volumes history.
     */
    public abstract boolean searchProceedings(String url);
    
}
