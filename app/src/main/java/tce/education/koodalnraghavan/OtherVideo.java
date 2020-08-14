package tce.education.koodalnraghavan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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


public class OtherVideo extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private Bundle bundle;
    private Intent intent;

    private String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private JsonObjectRequest request;
    private RequestQueue queue;
    private JSONArray jsonArray;
    private JSONObject video;
    private String videoName;
    private String videoUrl;

    public OtherVideo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_video, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        list = new ArrayList<String>();
        url = new ArrayList<String>();

        adapter = new ArrayAdapter<String >(view.getContext(),R.layout.musicinfo,R.id.portal,list);

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("othervideo");
                            progressBar.setVisibility(View.VISIBLE);

                            for(int iterator = 0 ; iterator < jsonArray.length() ; iterator ++)
                            {
                                video = jsonArray.getJSONObject(iterator);

                                videoName = video.getString("name");
                                videoUrl = video.getString("url");

                                list.add(videoName);
                                url.add(videoUrl);
                            }

                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String videoUrl = url.get(position);
                                    String videoTitle = list.get(position);

                                    intent = new Intent(view.getContext(),VideoPlayer.class);

                                    bundle = new Bundle();

                                    bundle.putString("list",videoTitle);
                                    bundle.putString("url",videoUrl);

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
                Toast.makeText(view.getContext(),"Please try again later ",Toast.LENGTH_SHORT).show();
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
        return  view;
    }
}
