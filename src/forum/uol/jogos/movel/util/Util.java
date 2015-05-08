package forum.uol.jogos.movel.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Util {
	
	private final String SHARED_PREFERENCES_NAME = "UOLJogosMovel";
		
	private SharedPreferences s;
	
	public Util(Context c) {
		s = c.getSharedPreferences(SHARED_PREFERENCES_NAME, c.MODE_PRIVATE);
	}
	
	public boolean isUsingNewLayout() {
		return s.getBoolean("newLayout", false);
	}
	
	public void setNewLayout(boolean b) {
		s.edit().putBoolean("newLayout", b).commit();
	}
	
	public int getSecondsSinceLastPost() {
		return 0;
	}
	
	public void setLastTimePosted() {
		//TODO
	}
	
	public static int calcularNumeroDePaginas(int posts) {
		if (posts >= 20)
			return (posts/20) + 1;
		else
			return 1;
	}
	
	public void saveUserAndPassword(String email, String password) {
		s.edit().putString("user", email).putString("pass", password).commit();
	}
	
	public String getEmail() {
		return s.getString("user", null);
	}
	
	public String getPassword() {
		return s.getString("pass", null);
	}
	
	public String getLatestCookie() {
		return s.getString("lastSavedCookie", null);
	}
	
	public void saveLastCookie(String cookie) {
		s.edit().putString("lastSavedCookie", cookie).commit();
	}
	
	public void resetCookie() {
		s.edit().putString("lastSavedCookie", null).commit();
	}
	
}
