/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bruno
 */
public class Spider {
    
    private static final int MAX_PAGES_TO_SEARCH = 10;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    
    private String nextUrl(){
        String nextUrl;
        do{
           nextUrl = this.pagesToVisit.remove(0);
        }while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
    
    public void search(String url, String searchWord){
        while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH){
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();
            if(this.pagesToVisit.isEmpty()){
                currentUrl = url;
                this.pagesVisited.add(url);
            }else{
                currentUrl = this.nextUrl();
            }
            leg.crawl(currentUrl);
//            leg.crawlJournal(currentUrl);
            boolean sucess = leg.searchForWord(searchWord);
            if(sucess){
                System.out.println(String.format("**Sucess** Word %s found at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
    }
    
}
