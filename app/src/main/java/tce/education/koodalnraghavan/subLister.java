package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class subLister extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;
    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";

    private String FolderName;
    private String folder;
    private String folderIndex;
    private TextView textView;
    private ListView listView;
    private ProgressBar progressBar;

    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_lister);

        bundle = getIntent().getExtras();

        FolderName = bundle.getString("folderName");
        folderIndex = bundle.getString("folder");

        textView = findViewById(R.id.pageTitle);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progress);
        textView.setText(FolderName);

        list = new ArrayList<String>();
        url = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(subLister.this,R.layout.musicinfo,R.id.portal,list);

        new AsynClass().execute();
    }

    class AsynClass extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            parseJson();
            return null;
        }
    }

    private void parseJson() {
        queue = Volley.newRequestQueue(subLister.this);
        request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray(folderIndex);
                            progressBar.setVisibility(View.VISIBLE);
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);
                                folder = jsonObject.getString("folder");

                                System.out.println("Folder NAme : " + folder);
                                if (folder.equals(FolderName))
                                {
                                    if (!list.contains(jsonObject.getString("name")))
                                    {
                                        list.add(jsonObject.getString("name"));
                                        url.add(jsonObject.getString("url"));
                                    }
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //Toast.makeText(getApplicationContext(),list.get(position) + url.get(position),Toast.LENGTH_SHORT).show();
                                    intent = new Intent(subLister.this,VideoPlayer.class);

                                    bundle = new Bundle();
                                    bundle.putString("list",list.get(position));
                                    bundle.putString("url",url.get(position));

                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(subLister.this,"Please try again",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                String username = "tpvsuser1";
                String password = "tpvs@userONE";
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
                headers.put("authorization",auth);
                return headers;
            }
        };
        queue.add(request);
    }
}
