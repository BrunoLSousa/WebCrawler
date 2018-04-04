/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

/**
 *
 * @author bruno
 */
public class SpiderTest {
    
    public static void main(String[] args){
        Spider spider = new Spider();
//        spider.search("http://arstecnhica.com/", "computer"); 
//        spider.search("https://dl-acm-org.ez27.periodicos.capes.gov.br/citation.cfm?id=3101463", "computer"); 
        spider.search("http://ieeexplore.ieee.org.ez27.periodicos.capes.gov.br/document/7961537", "computer"); 
//        spider.search("http://ieeexplore.ieee.org/xpl/RecentIssue.jsp?punumber=32", "computer"); 
   }    
    
}
