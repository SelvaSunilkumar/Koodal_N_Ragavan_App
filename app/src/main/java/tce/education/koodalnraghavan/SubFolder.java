package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Map;

public class SubFolder extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;
    private int folderNumber;

    private TextView folderName;


    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject folder;
    private String folder_name;

    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_folder);

        bundle = getIntent().getExtras();

        folderNumber = bundle.getInt("number");

        folderName = findViewById(R.id.folderName);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progress);

        if(folderNumber ==  0)
        {
            folderName.setText("Thinam oru nal vaarthai thirumaalai patri");

            prepareThirumalaiList(getApplicationContext());
        }
        else
        {
            folderName.setText("Thinam oru nal vaarthai");

            prepareList(getApplicationContext());
        }

        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(SubFolder.this,R.layout.folderinfo,R.id.folderName,arrayList);


    }

    private void prepareList(Context applicationContext) {


        queue = Volley.newRequestQueue(applicationContext);
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("video");

                            progressBar.setVisibility(View.VISIBLE);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");

                                if (! arrayList.contains(folder_name))
                                {
                                    arrayList.add(folder_name);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(arrayAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent intent = new Intent(SubFolder.this,subLister.class);

                                    Bundle bundle = new Bundle();

                                    bundle.putString("folderName",arrayList.get(position));
                                    bundle.putString("folder","video");

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
                Toast.makeText(SubFolder.this,"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
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

    private void prepareThirumalaiList(Context applicationContext) {

        queue = Volley.newRequestQueue(applicationContext);
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("thirumalai");

                            progressBar.setVisibility(View.VISIBLE);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                folder = jsonArray.getJSONObject(i);
                                folder_name = folder.getString("folder");

                                if (! arrayList.contains(folder_name))
                                {
                                    arrayList.add(folder_name);
                                }
                            }
                            progressBar.setVisibility(View.GONE);

                            listView.setAdapter(arrayAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(SubFolder.this,subLister.class);

                                    Bundle bundle = new Bundle();

                                    bundle.putString("folderName",arrayList.get(position));
                                    bundle.putString("folder","thirumalai");

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
                Toast.makeText(SubFolder.this,"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
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
