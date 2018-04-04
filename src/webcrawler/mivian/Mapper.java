package webcrawler.mivian;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Mapper {

	public Mapper(String URL){

		LinkedList<String> volumes = new LinkedList<>();
		LinkedList<String> articles = new LinkedList<>();
		/*getVolumes(URL, volumes);


		for (String volumeURL : volumes) {
			getArticles(volumeURL,articles);
			processArticles(articles);
		}
	
*/
		
		processArticles(articles);
	}


	private void getVolumes (String URL, LinkedList<String> volumes){

		String url = URL;
		System.out.println("Fetching "+url);
		Document doc;
		String temp;

		try {
			doc = Jsoup.connect(url).get();

			Elements links = doc.select("a[href]");

			for (Element link : links) {
				temp = ""+link.attr("abs:href");

				if (temp.contains("isnumber") && temp.contains("&punumber=32") && !temp.contains("mostRecent")){	
					volumes.add(temp);
				}
			}	

			//System.out.println("Total de volumes encontrados: "+volumes.size());

		} catch (IOException e) {

			e.printStackTrace();
		}
	}


	private void getArticles (String volumeURL, LinkedList<String> articles){

		String url = volumeURL;
		System.out.println("Fetching "+url);
		Document doc;
		String temp;


		try {
			doc = Jsoup.connect(url).get();

			Elements links = doc.select("a[href]");


			for (Element link : links) {
				temp = ""+link.attr("abs:href");

				if(temp.contains("/document/")&& temp.endsWith("/") && !articles.contains(temp)){
					articles.add(temp);
				}

			}	

			//System.out.println("Total de artigos encontrados: "+articles.size());


		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void processArticles(LinkedList<String> articles){

		Document document;
		String article = "http://ieeexplore.ieee.org/document/6112738/";
		String metadata = "";
		boolean foundMeta = false;
		

		/*for (String article : articles) {*/

		try {
			//Get Document object after parsing the html from given url.
			document = Jsoup.connect(article).get();
			//Get description from document object.
			
			Elements data = document.getElementsByTag("script");
		 	
			
			for (Element element : data) {
				
				for (DataNode node : element.dataNodes()) {
					
					if (node.getWholeData().contains("global.document.metadata")){
					  metadata = node.getWholeData();
					  foundMeta = true;
					  break;
					}
		        }
				
				if(foundMeta)
					break;
			}
			
		parser(new StringBuffer(metadata));
		
		} catch (IOException e) {
			e.printStackTrace();
		}	


		/*}*/

	}
	
	
	private void parser (StringBuffer scriptHTML){
		
		int x = scriptHTML.indexOf("global.document");
		
		String metadata = scriptHTML.substring(x,scriptHTML.length());
		
		String [] p1 = metadata.split(",");
		
		for (String string : p1) {
			System.out.println(string);
		}
		
		
	}


}
