package forum.uol.jogos.movel.adapters;

import java.net.URL;

import org.jsoup.Jsoup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import forum.uol.jogos.movel.R;
import forum.uol.jogos.movel.objects.Postagens;

public class PostNovoAdapter extends BaseAdapter {
	
	private final Context contexto;
	
	private final Postagens[] posts;
		
	URL img_url = null;
	
    DisplayImageOptions options;
    
    private ImageLoader iLoader;

	public PostNovoAdapter(Context contexto, Postagens[] posts) {
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
			
			rowView = inflater.inflate(R.layout.row_post_new, parent, false);
						
		}
				
		TextView tvUser = (TextView) rowView.findViewById(R.id.tvNick);
		tvUser.setText(posts[position].getUser());
		
		final ProgressBar pbAvatar = (ProgressBar) rowView.findViewById(R.id.pBarAvatar);
				
		//Pattern p = Pattern.compile("<table|<img|value=\"http://www.youtube.com");
		
		/*
		if (!p.matcher(posts[position].getPost()).find()) {
			layout.addView(criarTextView(posts[position].getPost()));
		} else {
			layout.addView(criarWebView(posts[position].getPost()));
		}
		*/
				
		WebView wvPost = (WebView) rowView.findViewById(R.id.webView1);
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
			final ImageView ivAvatar = (ImageView) rowView.findViewById(R.id.ivAvatarUser);
	        iLoader.displayImage(posts[position].getURLAvatar(), ivAvatar, options, 
	        		new SimpleImageLoadingListener() {

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							super.onLoadingCancelled(imageUri, view);
							ivAvatar.setVisibility(View.VISIBLE);
							pbAvatar.setVisibility(View.GONE);
							ivAvatar.setImageResource(R.drawable.ic_error);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							ivAvatar.setVisibility(View.VISIBLE);
							pbAvatar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							super.onLoadingFailed(imageUri, view, failReason);
							ivAvatar.setVisibility(View.VISIBLE);
							pbAvatar.setVisibility(View.GONE);
							ivAvatar.setImageResource(R.drawable.ic_error);
						}

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							super.onLoadingStarted(imageUri, view);
							ivAvatar.setVisibility(View.GONE);
							pbAvatar.setVisibility(View.VISIBLE);
						}
	        	
	        });
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
	
	public TextView criarTextView(String post) {
		final TextView t = new TextView(contexto);
		t.setTextSize(TypedValue.COMPLEX_UNIT_PX, 17);
		t.setTextColor(Color.BLACK);
		t.setText(Html.fromHtml(post));
		t.setMovementMethod(LinkMovementMethod.getInstance());
		return t;
	}
	
	public WebView criarWebView(String html) {
		WebView wvPost = new WebView(contexto);
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
		
		if (!html.contains("value=\"http://www.youtube.com")) {
			wvPost.loadDataWithBaseURL(null, sHTML(html), "text/html", "utf-8",null);
		} else {
			wvPost.loadDataWithBaseURL(null, sHTML(uTube(html)), "text/html", "utf-8",null);
		}
		return wvPost;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return posts.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
		
}
