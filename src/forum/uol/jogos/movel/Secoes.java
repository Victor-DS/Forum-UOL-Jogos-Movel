package forum.uol.jogos.movel;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;

import forum.uol.jogos.movel.adapters.SecAdapter;
import forum.uol.jogos.movel.util.Util;

public class Secoes extends SherlockListActivity {
	
	Util u;

	String secs[] = {"Geral", "Notícias", "Jogos e consoles", "DS, Wii", "PC", 
		"PlayStation, PSP", "Xbox, Xbox 360", "Museu do videogame", "Fora de tópico", "Vale Tudo", "Outros", 
		"Mensagens Particulares", "Tópico do APP", "Google Play", "Usar layout alternativo"};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
        u = new Util(Secoes.this);
        u.resetCookie();
        
        fixText();
        
        setListAdapter(new SecAdapter(Secoes.this, secs));
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i;
		if (!isNetworkAvailable())
			Toast.makeText(getApplicationContext(), "Sem conexão com a internet!", Toast.LENGTH_LONG).show();
		else {
			switch (position) {
			case 1:
				//Notícias
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "noticias_f_56");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 3:
				//DS, Wii
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "ds-wii_f_39");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 4:
				//PC
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "pc_f_40");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 5:
				//Playstation, PSP
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "playstation-psp_f_41");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 6:
				//Xbox, Xbox 360
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "xbox-xbox-360_f_43");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 7:
				//Museu do videogame
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "museu-do-videogame_f_44");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 9:
				//Vale Tudo
				i = new Intent(Secoes.this, Topicos.class);
				i.putExtra("Seção", "vale-tudo_f_57");
				i.putExtra("titulo", secs[position]);
				startActivity(i);
				break;
				
			case 11:
				//MP's
				startActivity(new Intent(Secoes.this, MP.class));
				break;
				
			case 12:
				startActivity(new Intent(Secoes.this, Post.class)
						.putExtra("url", "http://forum.jogos.uol.com.br/android-app-do-vt-link-do-google-play_t_2939072")
						.putExtra("titulo", "[Android] App do VT [+Link do Google Play]")
						.putExtra("respostas", 259));
				break;
				
			case 13:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
				break;
				
			case 14:
				u.setNewLayout(!u.isUsingNewLayout());
				
				fixText();
				
		        setListAdapter(new SecAdapter(Secoes.this, secs));
				break;
				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Login")
			.setTitle("Login")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					startActivity(new Intent(Secoes.this, Login.class));
					return false;
				}
				
			})
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			
		return super.onCreateOptionsMenu(menu);

	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Secoes.this.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void fixText() {
        if(u.isUsingNewLayout()) {
        	secs[14] = "Usar layout clássico";
        } else {
        	secs[14] = "Usar layout alternativo";
        }
	}

}
