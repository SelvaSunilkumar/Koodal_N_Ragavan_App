package tce.education.koodalnraghavan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import java.util.List;
import java.util.Map;

public class Azhwarmusictab extends Fragment  {

    private List<String> listDataHeader;
    private HashMap<String,List<DataPriceLister>> listDataChild;

    private int counter;

    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";
    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray jsonArray;
    private JSONObject folder;


    private View view;
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_azhwarmusictab,container,false);

        listView = view.findViewById(R.id.listView);
        progressBar = view.findViewById(R.id.progress);

        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.folderinfo,R.id.folderName,list);

        prepareListData(view);


        return view;
    }

    private void prepareListData(View view) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<DataPriceLister>>();

        counter = 0;

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = response.getJSONArray("alwaraudio");

                            int max = maxFolderNo(response);

                            progressBar.setVisibility(View.VISIBLE);
                            for (int k = 1 ; k<= max;k++)
                            {
                                for (int i = 0; i < jsonArray.length() ; i++ )
                                {
                                    folder = jsonArray.getJSONObject(i);
                                    if (k == folder.getInt("folderno"))
                                    {
                                        if (! list.contains(folder.getString("folder")))
                                        {
                                            list.add(folder.getString("folder"));
                                        }
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(view.getContext(),AlwarSubFolder.class);

                                        Bundle bundle = new Bundle();

                                        bundle.putString("foldername",list.get(position));
                                        bundle.putInt("index",position);

                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Please try Again later or Try again",Toast.LENGTH_SHORT).show();
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

    private int maxFolderNo(JSONObject response) {
        int max = -1;

        JSONArray jsonArray;
        JSONObject jsonObject;

        try {
            jsonArray = response.getJSONArray("alwaraudio");

            for (int i=0;i<jsonArray.length();i++)
            {
                jsonObject = jsonArray.getJSONObject(i);

                System.out.println("Folder Number : " + jsonObject.getInt("folderno"));

                if (jsonObject.getInt("folderno") > max)
                {
                    max = jsonObject.getInt("folderno");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Max : " + max);
        return max;
    }
}
