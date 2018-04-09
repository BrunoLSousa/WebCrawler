/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm.conference;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webcrawler.algorithm.ConferenceCrawler;

/**
 *This class specify the ConferenceCrawler class. Its goal is to search papers link reference to IEEE journal.
 * @author bruno & mivian
 */
public class IEEE extends ConferenceCrawler {

    private int totatLinks;

    /**
     * Constructor method of this class.
     */
    public IEEE(){
        super();
    }
    
    /**
     * Specific implementation of the IEEE class. 
     * @param url Url reference to conference homepage that contains all proceedings history.
     */
    @Override
    public boolean searchProceedings(String url) {
        Document htmlDocument;
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
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
            try {
                Connection connection = Jsoup.connect(proceeding).userAgent(USER_AGENT);
                htmlDocument = connection.get();
                
                int pages = calculatePagination(htmlDocument);
                String url = getFilter(htmlDocument, proceeding);
                
                for(int i = 1; i <= pages; i++){
                    Elements blocks = htmlDocument.select("ul[class='results']");
                    
                    for (Element link : blocks.select("a[class='art-abs-url']")) {
                        this.papers.add(link.absUrl("href"));
                    }
                    
                    if((i + 1) <= pages){
                        connection = Jsoup.connect(url + (i+1)).userAgent(USER_AGENT);
                        htmlDocument = connection.get();
                    }
                    
                }
            } catch (IOException e) {
                return;
            }
            System.out.println(cont + "/" + this.proceedings.size() + " concluído...");
            cont++;
        }
        System.out.println("Busca concluída!");
    }

    /**
     * Calculate the amount of pages existing at a specific proceeding based on the pagination size to get all links. 
     * @param htmlDocument Document of first page already open to obtain information about pages number.
     */
    private int calculatePagination(Document htmlDocument) {
        Elements bolds = htmlDocument.select("div[class='results-display']> b");
        String[] pagination = bolds.get(0).text().split("- ");
        int limit = Integer.parseInt(pagination[1]);
        int totalLinks = Integer.parseInt(bolds.get(1).text());
        return ((totalLinks % limit) == 0) ? totalLinks / limit : (totalLinks / limit) + 1;
    }
    
    /**
     * Calculate the amount of pages existing at a specific proceeding based on the pagination size to get all links. 
     * @param htmlDocument Document of first page already open to obtain information about url filter.
     * @param htmlDocument Home page proceeding link.
     */
    private String getFilter(Document htmlDocument, String proceeding){
        Element oqs = htmlDocument.getElementById("oqs");
        String url = proceeding + "&" + oqs.val() + "&pageNumber=";
        return url;
    }
}
