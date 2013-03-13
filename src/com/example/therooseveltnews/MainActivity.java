package com.example.therooseveltnews;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10;
	Button refresh,olderPosts,newerPosts;
	Document doc;
	String[] articlesNames;
	String[] urls;
	Button[] buttons;
	
	String page;
	int pageNumber;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pageNumber = 1;
		page = "";
		refresh = (Button) findViewById(R.id.pageRefresh);
		olderPosts = (Button) findViewById(R.id.olderPosts);
		newerPosts = (Button) findViewById(R.id.newerPosts);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		b3 = (Button) findViewById(R.id.button3);
		b4 = (Button) findViewById(R.id.button4);
		b5 = (Button) findViewById(R.id.button5);
		b6 = (Button) findViewById(R.id.button6);
		b7 = (Button) findViewById(R.id.button7);
		b8 = (Button) findViewById(R.id.button8);
		b9 = (Button) findViewById(R.id.button9);
		b10 = (Button) findViewById(R.id.button10);
		Button[] b = {b1,b2,b3,b4,b5,b6,b7,b8,b9,b10}; 
		buttons = b;
		setPageText(buttons);
		olderPosts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pageNumber++;
				page = ("page/" + pageNumber + "/");
				setPageText(buttons);
			}
		});
		
		newerPosts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pageNumber > 1) {
					pageNumber--;
					if(pageNumber==1) {
						page = "";
					}
					setPageText(buttons);
				} 
			}
		});
		
		refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setPageText(buttons);
			}
		});
	}
	
	 public void myClickHandler(View target) {
	        Intent openArticlePage = new Intent(MainActivity.this,ArticlePage.class);
	           int index = 0;
	           for (int i = 0; i < buttons.length; i++)
	           {
	              if (buttons[i].getId() == target.getId())
	              {
	                 index = i;
	                 break;
	              }
	           }
	        openArticlePage.putExtra("articlepage",urls[index]);
	        startActivity(openArticlePage);
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void setPageText(Button[] b) {
		int count = 0;		
		try {
			articlesNames = new loadWebPageStuff().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] postedDates = getArticleDate();
		for(Button button : b) {			 
			button.setText(articlesNames[count] + "\nPosted On " + postedDates[count]);
			count++;
		}
	}
	

	public String[] getArticleDate() {
		String[] postedDates = new String[10];
		try {
			Elements dates = doc.select("span.entry-date");
			int count = 0;
			for (Element d : dates) {
				postedDates[count]=d.text();
				count++;			}
			
		} catch(Exception e) {
				e.printStackTrace();
		}
		return postedDates;
	}
	
	
	public class loadWebPageStuff extends AsyncTask<Void,Void,String[]>{
		
		@Override
		protected String[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			urls = new String[10];
			String[] articles = new String[10];
			try {
				Connection conn = Jsoup.connect("http://www.therooseveltnews.org/" + page).timeout(60000);
				doc = conn.get();
				Elements links = doc.select("h2 > a");
				int count = 0;
				for (Element link : links) {
					urls[count]=link.attr("href");
					articles[count]=link.text();
					count++;			}
				
			} catch(Exception e) {
					e.printStackTrace();
			}
			
			return articles;
		}
	}
}
	
