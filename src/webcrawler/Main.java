/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

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

        WebCrawler crawler = new WebCrawler();
        crawler.init(args);

    }

}
