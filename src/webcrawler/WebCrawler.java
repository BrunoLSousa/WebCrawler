/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import webcrawler.algorithm.ConferenceCrawler;
import webcrawler.algorithm.JournalCrawler;
import webcrawler.algorithm.conference.IEEEConference;
import webcrawler.algorithm.journal.IEEEJournal;

/**
 *
 * @author bruno
 */
public class WebCrawler {

    private List<String> links;

    public WebCrawler() {
        this.links = new ArrayList<>();
    }

    public void init(String args[]) {
        if (verifyArguments(args)) {
            getLinks(args[1]);
            crawl();
        } else {
            System.err.println("Comando errado. Use -i <diretório do arquivo>");
        }
    }

    private boolean verifyArguments(String args[]) {
        if (args.length > 0) {
            return (args[0].equals("-i"));
        } else {
            return false;
        }
    }

    private void getLinks(String path) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            while (br.ready()) {
                String line = br.readLine();
                this.links.add(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void crawl() {
        for (String link : this.links) {
            String[] split = link.split(",");
            if (split[2].equals("conference")) {
                ConferenceCrawler conference = new IEEEConference();
                conference.search(split[0], split[1]);
            } else {
                JournalCrawler journal = new IEEEJournal();
                journal.search(split[0], split[1]);
            }
        }
    }

}
