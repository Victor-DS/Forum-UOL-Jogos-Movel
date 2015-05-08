package forum.uol.jogos.movel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;

import forum.uol.jogos.movel.adapters.PostAdapter;
import forum.uol.jogos.movel.adapters.PostNovoAdapter;
import forum.uol.jogos.movel.objects.Postagens;
import forum.uol.jogos.movel.util.Util;

public class PostClassicLayout extends SherlockListActivity implements OnMenuItemClickListener {

	int página, nPaginas;
	
	PostAdapter adapter;
	
	boolean a = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(titulo(getIntent().getExtras()));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		nPaginas = Util.calcularNumeroDePaginas(getIntent().getExtras().getInt("respostas"));
		
		if(getIntent().getExtras().getBoolean("ultimaPágina", false))
			página = nPaginas;
		else
			página = 1;
		
		new carregaLista().execute(url(getIntent().getExtras()));
	}
	
	public String titulo(Bundle extras) {
		return extras.getString("titulo");
	}
	
	public String url(Bundle extras) {
		return extras.getString("url");
	}
	
	private class carregaLista extends AsyncTask<String, Void, Postagens[]> {

		ProgressDialog pDialog;

		@Override
		protected Postagens[] doInBackground(String... params) {
			try {
				return listaTopico(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Postagens[] result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					adapter = new PostAdapter(PostClassicLayout.this, result);
					
					if (a)
						adapter.notifyDataSetChanged();
					setListAdapter(adapter);
					pDialog.dismiss();
					a = true;
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			else {
				pDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = ProgressDialog.show(PostClassicLayout.this, "", "Carregando...", true);
			pDialog.show();
		}
		
	}
	
	public static Postagens[] listaTopico(String URL) throws Exception {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		//menu.add("Página 1 de " + nPaginas)
			//.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		if (nPaginas > 1) {
			
			if (nPaginas > 2) {
				menu.add("<<")
					.setTitle("<<")
					.setOnMenuItemClickListener(this)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				
			}
			
			menu.add("<")
				.setTitle("<")
			.setOnMenuItemClickListener(this)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			
			menu.add(">")
			.setTitle(">")
			.setOnMenuItemClickListener(this)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			
			
			if(nPaginas > 2) {
				menu.add(">>")
					.setTitle(">>")
					.setOnMenuItemClickListener(this)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
				
				menu.add("Ir para...")
					.setTitle("Ir para...")
					.setOnMenuItemClickListener(this)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			}
						
			/*
			menu.add("Atualizar")
				.setTitle("Atualizar")
				.setOnMenuItemClickListener(this)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			*/
		}
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getTitle().toString().equals("<<")) {
			if (página != 1) {
				página = 1;
				new carregaLista().execute(url(getIntent().getExtras())+"?page="+página);
			}
			else {
				Toast.makeText(getApplicationContext(), "Você está na página 1", Toast.LENGTH_SHORT).show();
			}
		}
		else if (item.getTitle().toString().equals("<")) {
			if (página != 1) {
				página--;
				new carregaLista().execute(url(getIntent().getExtras())+"?page="+página);
			}
			else {
				Toast.makeText(getApplicationContext(), "Você está na página 1", Toast.LENGTH_SHORT).show();
			}
		}
		else if (item.getTitle().toString().equals(">")) {
			if (página < nPaginas) {
				página++;
				new carregaLista().execute(url(getIntent().getExtras())+"?page="+página);
			}
			else {
				Toast.makeText(getApplicationContext(), "Você está na última página", Toast.LENGTH_SHORT).show();
			}
		}
		else if (item.getTitle().toString().equals(">>")) {
			if (página != nPaginas) {
				página = nPaginas;
				new carregaLista().execute(url(getIntent().getExtras())+"?page="+página);
			}
			else {
				Toast.makeText(getApplicationContext(), "Você está na última página", Toast.LENGTH_SHORT).show();
			}
		}
		else if (item.getTitle().toString().equals("Ir para...")) {
			nPaginaIr();
		}
		return false;
	}
	
	public void nPaginaIr() {
		LayoutInflater infle = getLayoutInflater();
		View txtlayout = infle.inflate(R.layout.poptxt, null);
		AlertDialog.Builder adBuilder = new AlertDialog.Builder(PostClassicLayout.this);
		adBuilder.setView(txtlayout);
		adBuilder.setTitle("Página");
		final EditText txt = (EditText) txtlayout.findViewById(R.id.et1);
		txt.setHint("Insira um número de 1 a " + nPaginas);
		adBuilder.setCancelable(true);
		adBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				if (Integer.parseInt(txt.getText().toString()) <= nPaginas) {
					página = Integer.parseInt(txt.getText().toString());
					new carregaLista().execute(url(getIntent().getExtras())+"?page="+página);
				}
					
			}
		});
		adBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog adFinal = adBuilder.create();
		adFinal.show();
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
}
