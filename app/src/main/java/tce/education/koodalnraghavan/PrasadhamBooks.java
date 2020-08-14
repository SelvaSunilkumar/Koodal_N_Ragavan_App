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

public class PrasadhamBooks extends Fragment {

    private ProgressBar progressBar;
    private ListView listView;
    private View view;

    private ArrayList<String> list;
    private ArrayList<String> url;
    private ArrayAdapter<String> adapter;

    private String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";

    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray array;
    private JSONObject object;

    public PrasadhamBooks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_prasadham_books, container, false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        list = new ArrayList<>();
        url = new ArrayList<>();
        adapter = new ArrayAdapter<>(view.getContext(),R.layout.pdfinfo,R.id.portal,list);

        parseJson();

        return view;
    }

    private void parseJson() {

        queue = Volley.newRequestQueue(view.getContext());

        request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            array = response.getJSONArray("prasadhamtext");

                            progressBar.setVisibility(View.VISIBLE);
                            for (int iterator = 0; iterator < array.length(); iterator++)
                            {
                                object = array.getJSONObject(iterator);

                                list.add(object.getString("name"));
                                url.add(object.getString("url"));
                            }

                            progressBar.setVisibility(View.GONE);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent intent = new Intent(view.getContext(),PdfViewer.class);
                                    Bundle bundle = new Bundle();

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
                Toast.makeText(view.getContext(),"Please try again Later",Toast.LENGTH_SHORT).show();
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
