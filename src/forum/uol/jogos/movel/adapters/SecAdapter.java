package forum.uol.jogos.movel.adapters;

import java.net.URL;

import forum.uol.jogos.movel.R;
import forum.uol.jogos.movel.R.drawable;
import forum.uol.jogos.movel.R.id;
import forum.uol.jogos.movel.R.layout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecAdapter extends ArrayAdapter<String>{
	private final Context contexto;
	
	private final String[] secoes;
		
	URL img_url = null;

	public SecAdapter(Context contexto, String[] sec) {
        super(contexto, R.layout.row, sec);
        this.contexto = contexto;
        this.secoes = sec;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
		View rowView = inflater.inflate(R.layout.row, parent, false);
		LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.llRow);
		TextView textView = (TextView) rowView.findViewById(R.id.tvSecao);
		textView.setText(secoes[position]);
	 
		String s = secoes[position];
	 
		System.out.println(s);
	 
		if (s.equals("Geral") || s.equals("Jogos e consoles") || s.equals("Fora de tópico") || s.equals("Outros")) {
			layout.setBackgroundResource(R.drawable.fundo);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
		}
		
		return rowView;
	}

}
