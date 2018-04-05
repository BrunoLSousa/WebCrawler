/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm;

import java.util.LinkedList;
import java.util.List;

/**
 *This class specify the Crawler class. Its goal is to search the journal papers link.
 * @author bruno & mivian
 */
public abstract class JournalCrawler extends Crawler{
    
    protected List<String> volumes;
    
    /**
     * Constructor method of this class.
     */
    public JournalCrawler(){
        super();
        this.volumes = new LinkedList<>();
    }
    
    /**
     * Abstract method used to initialize the search of volume links. 
     * @param url Url reference to conference or journal homepage that contains all proceedings or volumes history.
     */
    public abstract boolean searchVolumes(String url);
    
    
}
