package scl0958.cusersscl0958desktopblogstestcode;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;
    List<HashMap<String, String>> onlineData;
    ProgressDialog pd;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);
        toolbar= (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        final String url = "http://www.qa4.org/?json=get_recent_posts&count=45";
        new AsyncHttpTask().execute(url);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(MainActivity.this);
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.setMessage("Loading please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            pd.dismiss();

            if (result == 1) {
                recycleAdapter = new RecycleAdapter(MainActivity.this,onlineData);
                recyclerView.setAdapter(recycleAdapter);
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            onlineData = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);

                HashMap<String, String> item = new HashMap<>();
                item.put("title", post.optString("title"));

                JSONArray jsonArray = post.getJSONArray("attachments");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONObject jsonArrayImages = jsonObject1.getJSONObject("images");
                JSONObject jsonArrayThumb = jsonArrayImages.getJSONObject("thumbnail");

                item.put("thump", jsonArrayThumb.optString("url"));

                onlineData.add(item);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
