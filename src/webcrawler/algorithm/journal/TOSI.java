/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm.journal;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webcrawler.algorithm.JournalCrawler;
import webcrawler.algorithm.conference.IEEE;

/**
 * This class specify the JournalCrawler class. Its goal is to search papers
 * link reference to TOSI journal.
 *
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
     *
     * @param url Url reference to journal homepage that contains all volumes
     * history.
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

            Element block = htmlDocument.select("div[id='past-issues'] > div[class='volumes'] > div[class='level']").get(0);
            for (Element link : block.select("a[href]")) {
                this.volumes.add(link.absUrl("href"));
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
//        for (String volume : this.volumes) {
        int index = 0;
        while (index < this.volumes.size()) {
            String volume = this.volumes.get(index);
            System.out.println("Volume Iniciado - " + volume);
            htmlDocument = connectURL(volume);
            int pages = calculatePagination(htmlDocument);
            if (pages > 0) {
                String url = getFilter(htmlDocument);

                for (int i = 1; i <= pages; i++) {
                    Elements blocks = htmlDocument.select("ul[class='results']");

                    for (Element content : blocks.select("li")) {
                        if (!content.select("a[class='art-abs-url']").attr("href").isEmpty() && content.select("div[class='controls']").text().contains("Abstract")) {
                            this.papers.add(content.select("a[class='art-abs-url']").get(0).absUrl("href"));
                        }
                    }

                    htmlDocument = ((i + 1) <= pages) ? connectURL(url + (i + 1)) : htmlDocument;

                }

                System.out.println(cont + "/" + this.volumes.size() + " concluído...");
                cont++;
                index++;
            }
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
        if (bolds.size() > 0) {
            String[] pagination = bolds.get(0).text().split("- ");
            int limit = Integer.parseInt(pagination[1]);
            int totalLinks = Integer.parseInt(bolds.get(1).text());
            return ((totalLinks % limit) == 0) ? totalLinks / limit : (totalLinks / limit) + 1;
        }
        return 0;
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
    private Document connectURL(String url) {
        Connection connection;
        try {
            connection = Jsoup.connect(url).userAgent(USER_AGENT);
            return connection.get();
        } catch (IOException ex) {
            Logger.getLogger(IEEE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
