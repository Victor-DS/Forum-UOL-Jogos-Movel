package forum.uol.jogos.movel;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

import forum.uol.jogos.movel.util.LoginUtil;
import forum.uol.jogos.movel.util.PostUtil;
import forum.uol.jogos.movel.util.Util;

public class CriarTopico extends SherlockActivity implements OnClickListener {

	private EditText assunto, texto;
	
	private TextView enviar;
	
	private Util u;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("Novo tópico");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_criar_topico);
		
		init();
		
		if(u.getLatestCookie() == null)
			startActivity(new Intent(CriarTopico.this, Login.class));
		
	}
	
	private void init() {
		assunto = (EditText) findViewById(R.id.etAssunto);
		texto = (EditText) findViewById(R.id.etTexto);
		enviar = (TextView) findViewById(R.id.tvEnviar);
		enviar.setOnClickListener(this);
		u = new Util(CriarTopico.this);
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

	@Override
	protected void onResume() {
		super.onResume();
		if(u.getLatestCookie() == null)
			finish();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tvEnviar:
			//TODO Salvar hora que foi postado para verificar anti-flood.
			/*
			if(PostUtil.novoTopico(assunto.getText().toString(), 
					texto.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Postado com sucesso", 
						Toast.LENGTH_SHORT).show();
				u.setLastTimePosted();
			}
			*/
			new MagicTask().execute();

			//else
				//Toast.makeText(getApplicationContext(), "Erro ao postar", 
						//Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;			
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private class MagicTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(pDialog.isShowing())
				pDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = ProgressDialog.show(CriarTopico.this, "", "Postando...", true);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				PostUtil.novoTopico(assunto.getText().toString(), 
						texto.getText().toString(), 
						LoginUtil.loginCookies(u.getEmail(), u.getPassword()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}

}
