package forum.uol.jogos.movel.objects;

public class Topico {

	private String titulo, url, user, respostas;
	
	public Topico(String titulo, String url, String user, String respostas) {
		this.titulo = titulo;
		this.url = url;
		this.user = user;
		this.respostas = respostas;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getRespostas() {
		return respostas;
	}

	@Override
	public String toString() {
		return titulo + " | " + user + " | " + url;
	}
}
