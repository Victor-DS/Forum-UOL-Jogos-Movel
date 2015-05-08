package forum.uol.jogos.movel;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

import forum.uol.jogos.movel.util.Util;

public class Login extends Activity{

	private static final String URL_LOGIN = "https://acesso.uol.com.br/login.html";
	
	EditText eMail, senha;
	
	TextView logar;
		
	SharedPreferences SP;
		
	private Util u;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		init();
		
		if (u.getEmail() != null && u.getPassword() != null) {
			eMail.setText(u.getEmail());
			senha.setText(u.getPassword());
		}
	}
	
	private void init() {
		eMail = (EditText) findViewById(R.id.etEmail);
		senha = (EditText) findViewById(R.id.etSenha);
		logar = (TextView) findViewById(R.id.btLogar);
		logar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new logaTask().execute();
			}
			
		});
		u = new Util(Login.this);
	}
	
	public String logar(String usuario, String senha) throws Exception {
		if (usuario != null && senha != null) {
			Connection.Response res = Jsoup.connect(URL_LOGIN)
					.data("user", usuario, "pass", senha)
					.method(Method.POST)
					.execute();
			
			//TODO Pegar todos os cookies em HashMap e salvar nos SharedPreferences
			return res.cookie("CAUBR01");
		}
		else
			return null;
	}
			
	private class logaTask extends AsyncTask<String, Void, Boolean> {

		ProgressDialog pDialog;
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String cookie = logar(eMail.getText().toString(), senha.getText().toString());
				if (cookie != null) {
					u.saveUserAndPassword(eMail.getText().toString(), senha.getText().toString());
					u.saveLastCookie(cookie);
					return true;
				}
				else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				pDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Logado com sucesso!", Toast.LENGTH_LONG).show();
				finish();
			}
			else {
				pDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = ProgressDialog.show(Login.this, "", "Logando...", true);
			pDialog.show();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
}
