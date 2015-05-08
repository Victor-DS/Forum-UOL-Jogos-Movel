package forum.uol.jogos.movel.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import android.util.Log;
import forum.uol.jogos.movel.objects.Biscoito;
import forum.uol.jogos.movel.objects.Cookie;

public class PostUtil {
	
	private static final String URL = "http://forum.jogos.uol.com.br/dwr/call/plaincall/PostFunctions.insertTopic.dwr";

	public static boolean criarTopico(String assunto, String texto, 
			String cookie) {
		return true;
	}
	
	public static boolean novoTopico(String assunto, String texto, Biscoito bolacha) {
		try {
			Connection.Response res = Jsoup.connect(formURL(assunto, texto, bolacha.getjSessionId()))
					.method(Method.POST)
					.timeout(10*1000)
					.cookies(bolacha.getCookies())
					.execute();
			
			Log.i("PostUtil", "Response: " + res.statusCode());
			Log.i("PostUtil", res.body());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private static String formURL(String assunto, String texto, String jSessionId) {
		Log.i("PostUtil", "Assunto: " + assunto + " | Texto: " + texto + " | JSESSIONID: " + jSessionId);
		return URL+"?callCount=1&page=/new_topic.jbb?forum.id=57&httpSessionId="+jSessionId+
				"&scriptSessionId=0E90AE728F3C66CC120C2DA341E318F7778&c0-scriptName=PostFunctions" +
				"&c0-methodName=insertTopic&c0-id=0&c0-param0=number:57&c0-param1=string:"+assunto+
				"&c0-param2=string:"+texto+"&batchId=0";
	}
	
	public static boolean postar(String post, Cookie c, String URL) {
		return true;
	}
}
