package forum.uol.jogos.movel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import forum.uol.jogos.movel.util.Util;

public class LerMP extends SherlockActivity{

	ImageView avatar;
	
	TextView usuario, mensagens, cadastro;
	
	WebView mp;
	
    DisplayImageOptions options;
        
    String cookie;
    
    private Util u;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.row_post);
		
		bts();

		setInfo(getIntent().getExtras());
		
		if(u.getLatestCookie() != null)
			new carregaLista().execute();
		else startActivity(new Intent(LerMP.this, Login.class));
	}
	
	public void bts() {
		avatar = (ImageView) findViewById(R.id.ivAvatar);
		usuario = (TextView) findViewById(R.id.tvUsuário);
		mensagens = (TextView) findViewById(R.id.tvMensagens);
		mensagens.setVisibility(View.GONE);
		cadastro = (TextView) findViewById(R.id.tvCadastro);
		cadastro.setVisibility(View.GONE);
		mp = (WebView) findViewById(R.id.wPost);
        options = new DisplayImageOptions.Builder()
    		.showImageOnFail(R.drawable.ic_error)
    		.showImageOnLoading(R.drawable.ic_loading)
    		.cacheInMemory(true)
    		.cacheOnDisc(true)
    		.build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(LerMP.this)
    		.build();
        ImageLoader.getInstance().init(config);
        
        u = new Util(LerMP.this);
	}
	
	public void setInfo(Bundle extras) {
		getSupportActionBar().setTitle(extras.getString("titulo"));
		usuario.setText(extras.getString("user"));
	}
	
	public static String[] mp(String cookie, Bundle extras) throws Exception {
		Document doc = Jsoup.connect(extras.getString("url"))
				.timeout(10*1000)
				.cookie("CAUBR01", cookie)
				.get();
		
		String mp = doc.select("div.texto").first().outerHtml().toString();
		String avatar = doc.select("#avatarImg").get(1).attr("abs:src").toString();
		
		return new String[] {sHTML(mp), avatar};
	}
	
	private class carregaLista extends AsyncTask<String, Void, String[]> {

		ProgressDialog pDialog;

		@Override
		protected String[] doInBackground(String... params) {
			try {
				cookie = u.getLatestCookie();
				return mp(cookie, getIntent().getExtras());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
			        ImageLoader.getInstance().displayImage(result[1], avatar, options);
					mp.getSettings().setJavaScriptEnabled(true);
					mp.setWebChromeClient(new WebChromeClient() {
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
					        if(url != null && url.startsWith("http://forum.jogos.uol.com.br/")) {
					            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))
					            .putExtra("url", url)
					            .putExtra("titulo", "Tópico")
					            .putExtra("respostas", 2));
					        	return true;
					        }
					        else if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
					        	startActivity(new Intent(LerMP.this, Web.class).putExtra("url", url));
					            return true;
					        }
					        else {
					            return false;
					        }
					    }
					});
					
					mp.loadDataWithBaseURL(null, result[0], "text/html", "utf-8",null);
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
			pDialog = ProgressDialog.show(LerMP.this, "", "Carregando...", true);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		if(u.getLatestCookie() == null)
			finish();
	}

	public static String sHTML(String html) {
		return "<style>img { max-width: 100%;  height: auto; }\n.quote{background-color:#FFF;border:1px solid #ee9344 !important;padding-left:3px !important;width:100% !important}.quote .quote{border:1px solid #ffd8a1 !important;border-style:dotted none !important}</style>\n" + html;
	}

}
