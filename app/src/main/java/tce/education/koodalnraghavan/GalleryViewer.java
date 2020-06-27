package tce.education.koodalnraghavan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.koodalnraghavan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryViewer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private ArrayList<GalleryResource> resources;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_viewer);

        recyclerView = findViewById(R.id.imageLister);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resources = new ArrayList<>();

        queue = Volley.newRequestQueue(this);
        parseJson();
    }

    private void parseJson() {

        String url = "https://raw.githubusercontent.com/SelvaSunilkumar/jsonRepo/master/portalInfo.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("gallery");

                            for(int iterator = 0 ; iterator < jsonArray.length() ; iterator++)
                            {
                                JSONObject gallery = jsonArray.getJSONObject(iterator);

                                String imageUrl = gallery.getString("url");

                                resources.add(new GalleryResource(imageUrl));
                            }
                            adapter = new GalleryAdapter(GalleryViewer.this,resources);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }
}
