package edu.ucuccs.diksyonaryo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class DikHome extends Fragment {

	EditText search;

	private final String WEB_URL = "http://www.ajdeguzman.x10.mx/api/books.xml";
	MyTask task;
	String[] books = new String[] { "book_id", "book_title", "book_author" };
	int[] books_layout = new int[] { R.id.wordId, R.id.word, R.id.author };
	private ListView lstWords;
	private ProgressDialog pd;

	Document doc;
	DocumentBuilder db;
	DocumentBuilderFactory dbf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		search = (EditText) rootView.findViewById(R.id.editText1);
		lstWords = (ListView) rootView.findViewById(R.id.lstWords);

		YoYo.with(Techniques.Bounce).delay(100).playOn(search);

		task = new MyTask();
		task.execute();

		lstWords.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View arg1, int arg2,
					long arg3) {

				Map<String, String> map = (Map<String, String>) av
						.getItemAtPosition(arg2);

				String id = map.get("book_id");
				String title = map.get("book_title");
				String author = map.get("book_author");

				Intent intent = new Intent(getActivity(), Meaning.class);
				Bundle b = new Bundle();
				b.putString("wordId", id);
				b.putString("wordTitle", title);
				b.putString("wordAuthor", author);
				intent.putExtras(b);
				startActivity(intent);

			}

		});

		return rootView;
	}

	private class MyTask extends AsyncTask<String, String, List> {
		@Override
		protected List doInBackground(String... arg0) {
			List<HashMap<String, String>> allBooks = new ArrayList<HashMap<String, String>>();
			// call this method which connections to web
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				doc = db.parse(webConnect());
			} catch (Exception e) {
				e.printStackTrace();
			}
			NodeList nodeListBook = doc.getElementsByTagName("book");
			for (int i = 0; i < nodeListBook.getLength(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				Element elementBook = (Element) nodeListBook.item(i);

				NodeList nodeListTitle = elementBook
						.getElementsByTagName("title");
				Element elementTitle = (Element) nodeListTitle.item(0);

				NodeList nodeListAuthor = elementBook
						.getElementsByTagName("author");
				Element elementAuthor = (Element) nodeListAuthor.item(0);

				map.put("book_id", elementBook.getAttribute("id"));
				map.put("book_title", elementTitle.getFirstChild()
						.getTextContent());
				map.put("book_author", elementAuthor.getFirstChild()
						.getTextContent());

				allBooks.add(map);

			}

			return allBooks;
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Loading contents...");
			pd.show();
		}

		@Override
		protected void onPostExecute(List str) {
			SimpleAdapter simple = new SimpleAdapter(getActivity(), str,
					R.layout.words_layout, books, books_layout);
			lstWords.setAdapter(simple);
			pd.hide();
		}
	}

	private InputStream webConnect() {
		InputStream in = null;
		try {
			URL url = new URL(WEB_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			in = conn.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

}