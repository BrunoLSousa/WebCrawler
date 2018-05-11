/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import webcrawler.datastructure.Author;
import webcrawler.datastructure.Keywords;
import webcrawler.datastructure.Paper;

/**
 *
 * @author bruno
 */
public class Parser {

    protected static final String USER_AGENT
            = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    public Parser() {

    }

    public void parse(List<String> papersLink, String acronym) {
        List<Paper> papers = new ArrayList<>();
        Gson gson = new Gson();
        int cont = 1;
        for (String link : papersLink) {
            System.out.println("Iniciando parsing " + cont + "/" + papers.size() + " --- " + link);
            String metadata = mineMetadata(link);
            Paper paper = gson.fromJson(metadata, Paper.class);
            papers.add(paper);
        }
        explortCSVData(papers, acronym);
    }

    private String mineMetadata(String link) {
        Document htmlDocumet = connectURL(link);
        String scriptHTML = htmlDocumet.html();
        int x = scriptHTML.indexOf("global.document.metadata");
        String metadata = scriptHTML.substring(x, scriptHTML.length());
        return metadata.split("global.document.metadata=")[1].split("};")[0] + "}";
    }

    private void explortCSVData(List<Paper> papers, String acronym) {
        for (Paper paper : papers) {
            System.out.println("Title: " + paper.getTitle());
            System.out.println("Authors:");
            if (paper.getAuthors()!= null) {
                for (Author author : paper.getAuthors()) {
                    System.out.println("        name: " + author.getName());
                    System.out.println("        affiliation: " + author.getAffiliation());
                }
            }
            System.out.println("Year: " + paper.getYear());
            System.out.println("Publication: " + paper.getVehicle());
            System.out.println("Paper Type:" + paper.getType());
            if(paper.getMetrics() != null){
                System.out.println("Citation: " + paper.getMetrics().getCitation());
                System.out.println("Download: " + paper.getMetrics().getDownloads());
            }else{
                System.out.println("Citation: ");
                System.out.println("Download: ");
            }
            System.out.println("Keywords:");
            if (paper.getKeywords() != null) {
                for (Keywords keyword : paper.getKeywords()) {
                    System.out.print("         " + keyword.getType() + ": ");
                    for (String kwd : keyword.getKeywords()) {
                        System.out.print(kwd + "; ");
                    }
                    System.out.println();
                }
            }
            System.out.println("Paper url: https://ieeexplore.ieee.org" + paper.getPdfUrl());
            System.out.println("Acronym: " + acronym);
        }
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
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
