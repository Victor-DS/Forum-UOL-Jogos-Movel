package forum.uol.jogos.movel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.google.analytics.tracking.android.EasyTracker;

import forum.uol.jogos.movel.adapters.MPAdapter;
import forum.uol.jogos.movel.util.Util;

public class MP extends SherlockListActivity {

	static String[] link, user, titulo;
	
	String cookie;
	
	private Util u;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("Mensagens Particulares");
		
		u = new Util(MP.this);
		
		if(u.getLatestCookie() == null)
			startActivity(new Intent(MP.this, Login.class));
		
		try {
			cookie = u.getLatestCookie();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Dalton!", Toast.LENGTH_SHORT).show();
		}
		
		new carregaLista().execute(cookie);
	}
	
	public static Object[] mp(String cookie) throws Exception {
		Document doc = Jsoup.connect("http://forum.jogos.uol.com.br/inbox.jbb")
				.timeout(10*1000)
				//.userAgent("Mozilla/5.0")
				.cookie("CAUBR01", cookie)
				.get();
		
		int s = doc.select("a.topictitle").size();
		
		titulo = new String[s];
		user = new String[s];
		link = new String[s];
		boolean[] novo = new boolean[s];
		
		for (int i = 0; i < s; i++) {
			titulo[i] = doc.select("a.topictitle").get(i).text().toString();
			user[i] = doc.select("span.name").get(i).text().toString();
			link[i] = doc.select("a.topictitle").get(i).attr("abs:href").toString();
			novo[i] = novaMensagem(doc.select("img").get(i+6).attr("abs:class").toString());
		}
		return new Object[] {titulo, user, novo};
	}
	
	public static boolean novaMensagem(String n) {
		if (n.equals("master-sprite sprite-folder-new"))
			return true;
		else
			return false;
	}
	
	private class carregaLista extends AsyncTask<String, Void, Object[]> {

		ProgressDialog pDialog;

		@Override
		protected Object[] doInBackground(String... params) {
			try {
				return mp(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Object[] result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					setListAdapter(new MPAdapter(MP.this, (String[]) result[0], (String[]) result[1], (boolean[]) result[2]));
					pDialog.dismiss();
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
			pDialog = ProgressDialog.show(MP.this, "", "Carregando...", true);
			pDialog.show();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(MP.this, LerMP.class);
		i.putExtra("url", link[position]);
		i.putExtra("user", user[position]);
		i.putExtra("titulo", titulo[position]);
		startActivity(i);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(u.getLatestCookie() == null)
			finish();
	}

}
