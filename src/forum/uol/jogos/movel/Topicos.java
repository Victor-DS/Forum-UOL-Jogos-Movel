package forum.uol.jogos.movel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import forum.uol.jogos.movel.adapters.TopicosAdapter;
import forum.uol.jogos.movel.objects.Topico;
import forum.uol.jogos.movel.util.Parser;
import forum.uol.jogos.movel.util.Util;

public class Topicos extends SherlockActivity implements OnMenuItemClickListener {

	Topico[] t;
		
	TopicosAdapter tAdapter;
	
	PullToRefreshListView lvTopicos;
	
	boolean exibePD = true;
	
	private Util u;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(titulo(getIntent().getExtras()));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_topicos);
		getSupportActionBar().setTitle(titulo(getIntent().getExtras()));
		
		u = new Util(Topicos.this);
				
		lvTopicos = (PullToRefreshListView) findViewById(R.id.lvTopicos);
		lvTopicos.setShowLastUpdatedText(true);
		lvTopicos.addHeaderView(((LayoutInflater) 
				Topicos.this.getSystemService(Topicos.this.LAYOUT_INFLATER_SERVICE))
        		.inflate(R.layout.header_ad, null, false));
		lvTopicos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					Intent i;
					if(u.isUsingNewLayout())
						i = new Intent(Topicos.this, Post.class);
					else
						i = new Intent(Topicos.this, PostClassicLayout.class);
					i.putExtra("url", t[arg2].getUrl());
					i.putExtra("titulo", t[arg2].getTitulo());
					i.putExtra("respostas", Integer.parseInt(t[arg2].getRespostas().replaceAll("\\.", "")));
					startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		lvTopicos.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new carregaLista().execute();
			}
			
		});
		
		startAd();
		
		//Carrega pela primeira vez:
		new carregaLista().execute();
	}
	
	private void startAd() {
		AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}
	
	public String titulo(Bundle extras) {
		return extras.getString("titulo");
	}

	public String secaoURL(Bundle extras) {
		return "http://forum.jogos.uol.com.br/"+extras.getString("Seção");
	}
	
	private class carregaLista extends AsyncTask<String, Void, Topico[]> {

		ProgressDialog pDialog;

		@Override
		protected Topico[] doInBackground(String... params) {
			try {
				return Parser.getTopicos(secaoURL(getIntent().getExtras()));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Topico[] result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					t = result;
					tAdapter = new TopicosAdapter(Topicos.this, result);
					lvTopicos.setAdapter(tAdapter);
					if (exibePD)
						pDialog.dismiss();
					exibePD = false;
					lvTopicos.onRefreshComplete();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				if (exibePD)
					pDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (exibePD) {
				pDialog = ProgressDialog.show(Topicos.this, "", "Carregando...", true);
				pDialog.show();
			}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add("Criar Novo Tópico")
			.setTitle("Criar Novo Tópico")
			.setOnMenuItemClickListener(this)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		startActivity(new Intent(Topicos.this, CriarTopico.class));
		return false;
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
