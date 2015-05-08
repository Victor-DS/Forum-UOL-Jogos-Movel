package forum.uol.jogos.movel.adapters;

import forum.uol.jogos.movel.R;
import forum.uol.jogos.movel.objects.Topico;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopicosAdapter extends BaseAdapter {
	private final Context contexto;
	
	private final Topico[] topicos;
		
	public TopicosAdapter(Context contexto, Topico[] topicos) {
        this.contexto = contexto;
        this.topicos = topicos;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
		View rowView = inflater.inflate(R.layout.row_topicos, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.tvTituloTopico);
		textView.setText(topicos[position].getTitulo());
		TextView tvLinhaDois = (TextView) rowView.findViewById(R.id.tvUserMaisResposta);
		tvLinhaDois.setText(topicos[position].getUser() + " - " + 
				topicos[position].getRespostas() + " respostas");
				 		
		return rowView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topicos.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
