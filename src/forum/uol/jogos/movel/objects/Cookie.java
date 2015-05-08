package forum.uol.jogos.movel.objects;

public class Cookie {
	
	private String ScriptSessionID, httpSessionID, CAUBR01;

	public Cookie() {
		super();
	}

	public String getScriptSessionID() {
		return ScriptSessionID;
	}

	public void setScriptSessionID(String scriptSessionID) {
		ScriptSessionID = scriptSessionID;
	}

	public String getHttpSessionID() {
		return httpSessionID;
	}

	public void setHttpSessionID(String httpSessionID) {
		this.httpSessionID = httpSessionID;
	}

	public String getCAUBR01() {
		return CAUBR01;
	}

	public void setCAUBR01(String cAUBR01) {
		CAUBR01 = cAUBR01;
	}

}
