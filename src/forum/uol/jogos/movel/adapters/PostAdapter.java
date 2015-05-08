package forum.uol.jogos.movel.adapters;

import java.net.URL;
import org.jsoup.Jsoup;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import forum.uol.jogos.movel.R;
import forum.uol.jogos.movel.objects.Postagens;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostAdapter extends BaseAdapter {
	
	private final Context contexto;
	
	private final Postagens[] posts;
		
	URL img_url = null;
	
    DisplayImageOptions options;
    
    private ImageLoader iLoader;

	public PostAdapter(Context contexto, Postagens[] posts) {
        this.contexto = contexto;
        this.posts = posts;
        options = new DisplayImageOptions.Builder()
    		.showImageOnFail(R.drawable.ic_error)
    		.showImageOnLoading(R.drawable.ic_loading)
    		.cacheInMemory(true)
    		.cacheOnDisc(true)
    		.build();
        
		iLoader = ImageLoader.getInstance();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(contexto)
    		.build();
		iLoader.init(config);
		
	}

	public View getView(int position, View convertView, ViewGroup parent){
		View rowView = convertView;
		
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) contexto
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
			rowView = inflater.inflate(R.layout.row_post, parent, false);						
		}
		
		TextView tvUser = (TextView) rowView.findViewById(R.id.tvUsuário);
		tvUser.setText(posts[position].getUser());
		
		TextView tvMsgs = (TextView) rowView.findViewById(R.id.tvMensagens);
		tvMsgs.setText("Mensagens: " + posts[position].getMensagens());
		
		TextView tvData = (TextView) rowView.findViewById(R.id.tvCadastro);
		tvData.setText("Cadastro: " + posts[position].getCadastro());
				
		WebView wvPost = (WebView) rowView.findViewById(R.id.wPost);
		wvPost.getSettings().setJavaScriptEnabled(true);
		wvPost.setWebChromeClient(new WebChromeClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        if(url != null && url.startsWith("http://forum.jogos.uol.com.br/")) {
		            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))
		            .putExtra("url", url)
		            .putExtra("titulo", "Tópico")
		            .putExtra("respostas", 2));
		        	return true;
		        }
		        else if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
		            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		            return true;
		        }
		        else {
		            return false;
		        }
		    }
		});
		
		if (!posts[position].getPost().contains("value=\"http://www.youtube.com")) {
			wvPost.loadDataWithBaseURL(null, sHTML(posts[position].getPost()), "text/html", "utf-8",null);
		} else {
			wvPost.loadDataWithBaseURL(null, sHTML(uTube(posts[position].getPost())), "text/html", "utf-8",null);
		}
		
		
		try {
			ImageView ivAvatar = (ImageView) rowView.findViewById(R.id.ivAvatar);
	        ImageLoader.getInstance().displayImage(posts[position].getURLAvatar(), ivAvatar, options);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return rowView;
	}
	
	public String sHTML(String html) {
		return "<style>img { max-width: 100%;  height: auto; }\n.quote{background-color:#FFF;border:1px solid #ee9344 !important;padding-left:3px !important;width:100% !important}.quote .quote{border:1px solid #ffd8a1 !important;border-style:dotted none !important}</style>\n" + html;
	}
	
	public String uTube(String html) {
		String ID = Jsoup.parse(html)
				.select("center > object > param")
				.attr("abs:value")
				.replace("http://www.youtube.com/v/", "")
				.replace("&hl=en&fs=1", "");
		return html.replace(
				html.substring(html.indexOf("<center>"), html.lastIndexOf("</center>")), 
				"<center><a href=\"https://www.youtube.com/watch?v="+ID+"\"><img src=\"http://img.youtube.com/vi/"+ID+"/0.jpg\"></a></center>");
	}

	@Override
	public int getCount() {
		return posts.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
		
}
