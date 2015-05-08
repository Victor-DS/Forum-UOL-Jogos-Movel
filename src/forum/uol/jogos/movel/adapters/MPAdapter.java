package forum.uol.jogos.movel.adapters;

import forum.uol.jogos.movel.R;
import forum.uol.jogos.movel.R.id;
import forum.uol.jogos.movel.R.layout;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MPAdapter extends ArrayAdapter<String>{
	private final Context contexto;
	
	private final String[] titulo, user;
	
	private final boolean[] novo;
		
	public MPAdapter(Context contexto, String[] tituloMP, String[] usuario, boolean[] novaMP) {
        super(contexto, R.layout.row_topicos, tituloMP);
        this.contexto = contexto;
        this.titulo = tituloMP;
        this.user = usuario;
        this.novo = novaMP;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
		View rowView = inflater.inflate(R.layout.row_topicos, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.tvTituloTopico);
		textView.setText(titulo[position]);
		textView.setTypeface(null, Typeface.BOLD_ITALIC);
		TextView tvLinhaDois = (TextView) rowView.findViewById(R.id.tvUserMaisResposta);
		tvLinhaDois.setText("Enviado por: " + user[position]);
				
		return rowView;
	}

}