package forum.uol.jogos.movel.objects;

import java.util.Map;

public class Biscoito {

	private Map<String, String> cookies;
	
	private String jSessionId;

	public Biscoito(Map<String, String> cookies, String jSessionId) {
		super();
		this.cookies = cookies;
		this.jSessionId = jSessionId;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public String getjSessionId() {
		return jSessionId;
	}

	public void setjSessionId(String jSessionId) {
		this.jSessionId = jSessionId;
	}
}
