package forum.uol.jogos.movel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;

public class Web extends SherlockActivity implements OnMenuItemClickListener {
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS); 
        WebView visual = new WebView(this);
        setContentView(visual);
        final Activity atv = this;
        visual.getSettings().setJavaScriptEnabled(true);
        visual.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				atv.setTitle("Carregando... " + Integer.toString(newProgress) + "%");
				atv.setProgress(newProgress * 100);
				if (newProgress == 100)
					atv.setTitle("Comentários");
			}
        	
        });
        visual.loadUrl(getIntent().getExtras().getString("url"));
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
				menu.add("Abrir no navegador")
					.setTitle("Abrir no navegador")
					.setOnMenuItemClickListener(this)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
				return super.onCreateOptionsMenu(menu);

			}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle().toString().equals("Abrir no navegador")) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getExtras().getString("url"))));
		}
		return false;
	}
}
