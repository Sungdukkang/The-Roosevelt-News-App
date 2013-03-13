package com.example.therooseveltnews;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticlePage extends Activity {
	TextView header;
	TextView body;
	TextView titleOfArticle;
	ImageView picture;
	Document doc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_page);
		Intent extras = getIntent();
		String url = (String) extras.getStringExtra("articlepage");
		titleOfArticle = (TextView) findViewById(R.id.title);
		header = (TextView) findViewById(R.id.header);
		body = (TextView) findViewById(R.id.articleBody);
		picture = (ImageView)findViewById(R.id.picture);
		picture.setVisibility(View.GONE);
	
		try {
				doc = Jsoup.connect(url).timeout(30000).get();
				Elements articleTitle = doc.select("h1.entry-title");
		        Elements dates = doc.select("span.entry-date");					
				Elements author = doc.select("h5");
				
				Elements textBody = doc.select("div.entry-content").first().select("p");
				String title = articleTitle.text();
				String authorName = author.text();
				Elements image = doc.select("div.entry-content").select("img");
			    String imageURL = image.attr("src");
				if ((imageURL.length()) > 1) {
					picture.setVisibility(View.VISIBLE);
					try {						  
						  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
						  picture.setImageBitmap(bitmap); 
						} catch (MalformedURLException e) {
						  e.printStackTrace();
						} catch (IOException e) {
						  e.printStackTrace();
						}
					}

				if ((authorName.length()) < 2) {
						authorName = "null";						
					}
		        titleOfArticle.setText(title);
			    header.setText("Posted on " + dates.text() + " by " + authorName + "\n");      
			    body.setText(textBody.text());																
			
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
}
