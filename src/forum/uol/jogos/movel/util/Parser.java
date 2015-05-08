package forum.uol.jogos.movel.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.util.Log;

import forum.uol.jogos.movel.objects.Postagens;
import forum.uol.jogos.movel.objects.Topico;

public class Parser {
	
	private static final int N_TOPICOS = 34;

	public static Topico[] getTopicos(String URL) {
		Topico[] t = new Topico[N_TOPICOS];
		
		Document doc = null;
		
		try {
			doc = Jsoup.connect(URL).userAgent("Mozilla").timeout(10*1000).get();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("TAMANHO: " + doc.select("td > span.respostas").size());
		
		for (int i = 0; i < 34; i++) {
			t[i] = new Topico(doc.select("div.topicos").get(i).getElementsByTag("a").attr("abs:title")
					.replace("http://forum.jogos.uol.com.br/", ""),	doc.select("div.topicos").get(i)
					.getElementsByTag("a").attr("abs:href"), doc.select("td > span.autor > a").get(i)
					.text(), doc.select("td > span.respostas").get(i).text());
			
			Log.i("Parser", t[i].toString());
		}
		
		return t;
	}
	
	public static Postagens[] getPosts(String URL) throws IOException {
		Document doc;
		
		doc = Jsoup.connect(URL).timeout(10*1000).userAgent("Mozilla").get();
		
		int n = doc.select("#avatarImg").size();
		
		Postagens[] p = new Postagens[n];
		
		for (int i = 0; i < n; i++) {
			p[i] = new Postagens(doc.select("span.data-cadastro > b").get(i).text(), 
					doc.select("p.descricao > b").get(i).text(), 
					doc.select("div.texto").get(i).outerHtml(), 
					doc.select("#avatarImg").get(i).attr("abs:src"), 
					doc.select("p.userNickname > a").get(i).text());
		}
		
		return p;
	}
}
