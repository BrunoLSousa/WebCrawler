/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm.journal;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webcrawler.algorithm.JournalCrawler;

/**
 *This class specify the JournalCrawler class. Its goal is to search papers link reference to TOSI journal.
 * @author bruno & mivian
 */
public class TOSI extends JournalCrawler {

    /**
     * Constructor method of this class.
     */
    public TOSI() {
        super();
    }

    /**
     * Specific implementation of the TOSI class. 
     * @param url Url reference to journal homepage that contains all volumes history.
     */
    @Override
    public boolean searchVolumes(String url) {

        Document htmlDocument;
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            htmlDocument = connection.get();

            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retried something other than HTML");
                return false;
            }

            Elements links = htmlDocument.select("div[class='level']");

            for (Element blocks : links) {
                String temp = "" + blocks.select("a").get(0).attr("abs:href");
                if (temp.contains("isnumber") && temp.contains("&punumber=32") && !temp.contains("mostRecent")) {
                    for (Element link : blocks.select("a[href]")) {
                        this.volumes.add(link.absUrl("href"));
                    }
                }
            }

            System.out.println("Total de volumes encontrados: " + volumes.size());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Specific implementation of the TOSI class. 
     */
    @Override
    protected void searchPapers() {
        Document htmlDocument;
        int cont = 1;
        float percent;
        System.out.println("Inicializando busca de artigos....");
        for (String volume : this.volumes) {
            try {
                Connection connection = Jsoup.connect(volume).userAgent(USER_AGENT);
                htmlDocument = connection.get();

//                if (!connection.response().contentType().contains("text/html")) {
//                    System.out.println("**Failure** Retried something other than HTML");
//                    return false;
//                }
                Elements blocks = htmlDocument.select("ul[class='results']");

                for (Element link : blocks.select("a[class='art-abs-url']")) {
                    this.papers.add(link.absUrl("href"));
                }
                
            } catch (IOException e) {
                return;
            }
            System.out.println(cont + "/" + this.volumes.size() + " concluído...");
            cont++;
        }
        System.out.println("Busca concluída!");
    }
    
}
