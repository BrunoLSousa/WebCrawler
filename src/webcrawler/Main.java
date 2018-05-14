/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import webcrawler.algorithm.ConferenceCrawler;
import webcrawler.algorithm.JournalCrawler;
import webcrawler.algorithm.conference.IEEE;
import webcrawler.algorithm.journal.TOSI;

/**
 * Main class of this project
 *
 * @author bruno & mivian
 */
public class Main {

    /**
     * Main method.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
//        JournalCrawler journal = new TOSI();
//        journal.search("http://ieeexplore.ieee.org/xpl/RecentIssue.jsp?punumber=32", "TOSE");

        ConferenceCrawler conference = new IEEE();
        conference.search("http://ieeexplore.ieee.org/xpl/conhome.jsp?punumber=1000691", "ICSE");

//        WebCrawler crawler = new WebCrawler();
//        crawler.init(args);

//        if (args.length > 0) {
//            if (args[0].equals("-i")) {
//                for (String arg : args) {
//                    System.out.println(arg);
//                }
//            } else {
//                System.err.println("Comando errado. Use -i <diretório do arquivo>");
//            }
//        } else {
//            System.err.println("Comando errado. Use -i <diretório do arquivo>");
//        }
    }

}
