package tce.education.koodalnraghavan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class BoyBabyName extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayList<String> list;
    //private ArrayList<String> url;
    private ArrayAdapter<String> adapter;


    //********* VOLLEY LIBRARY ***********
    private RequestQueue queue;
    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject name;
    private String babyName;

    public BoyBabyName() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boy_baby_name, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.baby_names,R.id.name,list);

        //------------------------------------------------------------------------------------------
        queue = Volley.newRequestQueue(view.getContext());

        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("boysName");
                            progressBar.setVisibility(View.VISIBLE);
                            for(int iterator = 0 ; iterator < jsonArray.length() ; iterator++)
                            {
                                name = jsonArray.getJSONObject(iterator);

                                babyName = name.getString("name");
                                list.add(babyName);
                            }

                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(view.getContext(),"Please try again Later",Toast.LENGTH_SHORT).show();
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
        //------------------------------------------------------------------------------------------
        queue.add(request);
        return view;
    }
}
