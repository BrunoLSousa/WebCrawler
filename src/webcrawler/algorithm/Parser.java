/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.algorithm;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    private static final int NUM_AUTHOR = 10;

    public Parser() {

    }

    public void parse(List<String> papersLink, String acronym) {
        List<Paper> papers = new ArrayList<>();
        Gson gson = new Gson();
        int cont = 1;
        for (String link : papersLink) {
            System.out.println("Iniciando parsing " + cont + "/" + papersLink.size() + " --- " + link);
            String metadata = mineMetadata(link);
            Paper paper = gson.fromJson(metadata, Paper.class);
            papers.add(paper);
            cont++;
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

        BufferedWriter writer;
        String auxFirstAffiliation = "";
        int i;
        try {
            System.out.println("Gerando o arquivo csv...");
            writer = new BufferedWriter(new FileWriter("papers_" + acronym.toLowerCase() + ".csv"));
            writer.write("title;authors;affiliations;publication;year;paper_type;citation;download;"
                    + "keywords_ieee;keywords_controlled;keywords_non_controlled;keywords_author;url;acronym");

            for (Paper paper : papers) {
                writer.newLine();
                String title = (paper.getTitle() != null) ? paper.getTitle().replace("\"", "'") : "";
                writer.write("\"" + title + "\";");

                if (paper.getAuthors().size() > 0) {

                    auxFirstAffiliation = (paper.getAuthors().get(0).getAffiliation() != null) ? paper.getAuthors().get(0).getAffiliation() : "";
                    String names = "";
                    String affiliations = "";
                    boolean first = true;
                    for (Author author : paper.getAuthors()) {
                        if (first) {
                            names += author.getName();
                            affiliations += (author.getAffiliation() != null) ? author.getAffiliation() : auxFirstAffiliation;
                            first = false;
                        } else {
                            names += "|" + author.getName();
                            affiliations += (author.getAffiliation() != null) ? "|" + author.getAffiliation() : "|" + auxFirstAffiliation;
                        }
                    }

                    writer.write("\"" + names.replace("\"", "\'") + "\";");
                    writer.write("\"" + affiliations.replace("\"", "\'") + "\";");
                } else {
                    writer.write(" ;");
                    writer.write(" ;");
                }
                writer.write("\"" + paper.getVehicle().replace("\"", "\'") + "\";");
                writer.write("\"" + paper.getYear() + "\";");
                writer.write("\"" + paper.getType().replace("\"", "\'") + "\";");

                if (paper.getMetrics() != null) {
                    writer.write("\"" + paper.getMetrics().getCitation() + "\";");
                    writer.write("\"" + paper.getMetrics().getDownloads() + "\";");
                } else {
                    writer.write(" ;");
                    writer.write(" ;");
                }

                String keywordIEEE = "";
                String keywordControlled = "";
                String keywordNonControlled = "";
                String keywordAuthor = "";
                if (paper.getKeywords() != null) {
                    for (Keywords keyword : paper.getKeywords()) {
                        boolean flag = true;
                        String kw = "";
                        for (String kwd : keyword.getKeywords()) {
                            if (flag) {
                                kw += kwd;
                                flag = false;
                            } else {
                                kw += "|" + kwd;
                            }
                        }
                        if(keyword.getType().equals("IEEE Keywords")){
                            keywordIEEE = kw;
                        }else if(keyword.getType().equals("INSPEC: Controlled Indexing")){
                            keywordControlled = kw;
                        }else if(keyword.getType().equals("INSPEC: Non-Controlled Indexing")){
                            keywordNonControlled = kw;
                        }else{
                            keywordAuthor = kw;
                        }
                    }
                    writer.write("\"" + keywordIEEE.replace("\"", "\'") + "\";");
                    writer.write("\"" + keywordControlled.replace("\"", "\'") + "\";");
                    writer.write("\"" + keywordNonControlled.replace("\"", "\'") + "\";");
                    writer.write("\"" + keywordAuthor.replace("\"", "\'") + "\";");
                } else {
                    writer.write(" ;");
                    writer.write(" ;");
                    writer.write(" ;");
                    writer.write(" ;");
                }

                writer.write("\"https://ieeexplore.ieee.org" + paper.getPdfUrl() + "\";");
                writer.write("\"" + acronym + "\"");

            }
            writer.close();
            System.out.println("Arquivo gerado com sucesso!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            connection = Jsoup.connect(url).timeout(0).userAgent(USER_AGENT);
            return connection.get();
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
