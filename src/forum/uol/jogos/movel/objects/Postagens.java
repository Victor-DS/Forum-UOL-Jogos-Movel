package forum.uol.jogos.movel.objects;

public class Postagens {
	
	private String post, URLAvatar, User, Mensagens, Cadastro;
	
	public Postagens(String Cadastro, String Mensagens, String post, String URLAvatar, String User) {
		this.Cadastro = Cadastro;
		this.Mensagens = Mensagens;
		this.post = post;
		this.URLAvatar = URLAvatar;
		this.User = User;
	}

	public String getPost() {
		return post;
	}

	public String getURLAvatar() {
		return URLAvatar;
	}

	public String getUser() {
		return User;
	}

	public String getMensagens() {
		return Mensagens;
	}

	public String getCadastro() {
		return Cadastro;
	}
}
