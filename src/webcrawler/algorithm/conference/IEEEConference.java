/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm.conference;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webcrawler.algorithm.ConferenceCrawler;

/**
 * This class specify the ConferenceCrawler class. Its goal is to search papers
 * link reference to IEEE journal.
 *
 * @author bruno & mivian
 */
public class IEEEConference extends ConferenceCrawler {

    private int totatLinks;

    /**
     * Constructor method of this class.
     */
    public IEEEConference() {
        super();
    }

    /**
     * Specific implementation of the IEEE class.
     *
     * @param url Url reference to conference homepage that contains all
     * proceedings history.
     */
    @Override
    public boolean searchProceedings(String url) {
        Document htmlDocument;
        try {
            Connection connection = Jsoup.connect(url).timeout(0).userAgent(USER_AGENT);
            htmlDocument = connection.get();

            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retried something other than HTML");
                return false;
            }

            Elements div = htmlDocument.select("div[class='header']");
            Elements links = div.select("li > a");

            for (Element link : links) {
                this.proceedings.add(link.absUrl("href"));
            }

            System.out.println("Total de proceedings encontrados: " + proceedings.size());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Specific implementation of the IEEE class.
     */
    @Override
    protected void searchPapers() {
        Document htmlDocument;
        int cont = 1;
        float percent;
        System.out.println("Inicializando busca de artigos....");
        for (String proceeding : this.proceedings) {
            htmlDocument = connectURL(proceeding);
            Set<String> volumes = mineVolumes(htmlDocument);
            do {
                int pages = calculatePagination(htmlDocument);
                String url = getFilter(htmlDocument);
                
                for (int i = 1; i <= pages; i++) {
                    Elements blocks = htmlDocument.select("ul[class='results']");
                    
                    for (Element content : blocks.select("li")) {
//                        if(!content.select("a[class='art-abs-url']").attr("href").isEmpty() && !content.select("div[class='authors']").text().isEmpty()){
                    if(!content.select("a[class='art-abs-url']").attr("href").isEmpty() && content.select("div[class='controls']").text().contains("Abstract")){
                            this.papers.add(content.select("a[class='art-abs-url']").get(0).absUrl("href"));
                        }
                    }
                    
                    htmlDocument = ((i + 1) <= pages) ? connectURL(url + (i + 1)) : htmlDocument;
                    
                }
                volumes.remove(proceeding);
                proceeding = (!volumes.isEmpty()) ? volumes.iterator().next() : proceeding;
                htmlDocument = (!volumes.isEmpty()) ? connectURL(proceeding) : htmlDocument;
            } while (!volumes.isEmpty());
            System.out.println(cont + "/" + this.proceedings.size() + " concluído...");
            cont++;
        }
        System.out.println("Busca concluída!");
    }

    /**
     * Calculate the amount of pages existing at a specific proceeding based on
     * the pagination size to get all links.
     *
     * @param htmlDocument Document of first page already open to obtain
     * information about pages number.
     */
    private int calculatePagination(Document htmlDocument) {
        Elements bolds = htmlDocument.select("div[class='results-display'] > b");
        String[] pagination = bolds.get(0).text().split("- ");
        int limit = Integer.parseInt(pagination[1]);
        int totalLinks = Integer.parseInt(bolds.get(1).text());
        return ((totalLinks % limit) == 0) ? totalLinks / limit : (totalLinks / limit) + 1;
    }

    /**
     * Calculate the amount of pages existing at a specific proceeding based on
     * the pagination size to get all links.
     *
     * @param htmlDocument Home page proceeding link.
     */
    private String getFilter(Document htmlDocument) {
        Element oqs = htmlDocument.getElementById("oqs");
        String url = htmlDocument.location() + "&" + oqs.val() + "&pageNumber=";
        return url;
    }
    
    /**
     * Returns the html content of a url.
     *
     * 
     * @param htmlDocument Home page proceeding link.
     */
    private Document connectURL(String url){
        Connection connection;
        try {
            connection = Jsoup.connect(url).timeout(0).userAgent(USER_AGENT);
            return connection.get();
        } catch (IOException ex) {
            Logger.getLogger(IEEEConference.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }  
    
    /**
     * Mines the volumes existing at a specific proceeding.
     *
     * 
     * @param htmlDocument Home page proceeding link.
     */
    private Set<String> mineVolumes(Document htmlDocument){
        Set<String> urlVolumes = new LinkedHashSet<>();
        Elements blocks = htmlDocument.select("select[id='sel_is_number'] > option");
        if(!blocks.isEmpty()){
            String currentNumber = htmlDocument.location().split("=")[1];
            String proceeding = htmlDocument.location().replace("punumber", "isnumber");
            for(Element option: blocks){
                if(!option.attr("selected").contains("true")){
                    urlVolumes.add(proceeding.replace(currentNumber, option.attr("value")));
                }
            }
        }
        return urlVolumes;
    }
    
}
