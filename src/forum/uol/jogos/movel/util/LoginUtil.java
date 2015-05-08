package forum.uol.jogos.movel.util;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import android.util.Log;
import forum.uol.jogos.movel.objects.Biscoito;

public class LoginUtil {
	
	private static final String URL_LOGIN = "http://forum.jogos.uol.com.br/";

	//private static final String URL_LOGIN = "https://acesso.uol.com.br/login.html";
	
	public static Biscoito loginCookies(String usuario, String senha) throws IOException {
		if (usuario != null && senha != null) {
			Connection.Response res = Jsoup.connect(URL_LOGIN)
					.data("user", usuario, "pass", senha)
					.method(Method.POST)
					.timeout(10*1000)
					.execute();
			
			Log.i("LoginUtil", "Is cookies empty? " + res.cookies().isEmpty());
			return new Biscoito(res.cookies(), 
					res.cookie("JSESSIONID"));
		} else return null;
	}

}
