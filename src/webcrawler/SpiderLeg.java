/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author bruno
 */
public class SpiderLeg {
    
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocumet;    
    
    public boolean crawl(String url){
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocumet = htmlDocument;
            
            if(connection.response().statusCode() == 200){
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html")){
                System.out.println("**Failure** Retried something other than HTML");
                return false;
            }
            
//            System.out.println("Received web page at " + url);
            
//            Elements linksOnPage = htmlDocument.select("a[href]");
            Elements linksOnPage = htmlDocument.select("script");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage){
//                this.links.add(link.absUrl("href"));
//                System.out.println(link.attr("content"));// absUrl("content"));
                if(link.toString().contains("global.document.metadata")){
                    String[] values = link.toString().split("global.document.metadata=\\{");
                    String[] values2 = values[1].split("};");
                    String[] values3 = values2[0].split(":\\{");
                    for(String string : values3){
                        System.out.println(string);
                    }
//                    System.out.println(values2[0]);
                }
            } 
           return true;
        } catch (IOException ioe) {
//            System.out.println("Error in out HTTP request " + ioe);
            return false;
        }
    }
    
    
    public boolean crawlJournal(String url){
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocumet = htmlDocument;
            
            if(connection.response().statusCode() == 200){
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html")){
                System.out.println("**Failure** Retried something other than HTML");
                return false;
            }
            
//            System.out.println("Received web page at " + url);
            
//            Elements linksOnPage = htmlDocument.select("a[href]");
            Elements linksOnPage = htmlDocument.select("div[class='level']");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage){
                if(link.select("a").get(0).toString().contains("Vol")){
                  for(Element link2 : link.select("a[href]")){
//                      System.out.println(link2.toString());
                      this.links.add(link2.absUrl("href"));
                      System.out.println(link2.absUrl("href"));
                  }
                }
//                this.links.add(link.absUrl("href"));
//                System.out.println(link.attr("content"));// absUrl("content"));
            } 
           return true;
        } catch (IOException ioe) {
//            System.out.println("Error in out HTTP request " + ioe);
            return false;
        }
    }
    
    public boolean searchForWord(String searchWord){
        if(this.htmlDocumet == null){
            System.out.println("ERROR! Call craw() before performing analysis on the document");
            return false;
        }
        System.out.println("Searchig for the word " + searchWord + "...");
        String bodyText = this.htmlDocumet.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }
    
    public List<String> getLinks(){
        return this.links;
    }
    
}
