package tce.education.koodalnraghavan.customlayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tce.education.koodalnraghavan.PdfViewAuthentication;
import tce.education.koodalnraghavan.R;
import tce.education.koodalnraghavan.adapter.SampleBooksAdapter;
import tce.education.koodalnraghavan.classes.SampleEBooksDetails;
import tce.education.koodalnraghavan.eBooksDisplay;

public class SampleEBooksLister extends BottomSheetDialogFragment implements SampleBooksAdapter.OnItemClickListener {

    private final String JSON_URL = "https://tpvs.tce.edu/restricted/koodal_app/Koodal_raghavan_json.php";

    private ImageView closeEBookLister;
    private RecyclerView recyclerView;
    private View view;

    private SampleBooksAdapter adapter;
    private ArrayList<SampleEBooksDetails> details;

    private String name;
    private String url;

    private RequestQueue queue;
    private JsonObjectRequest request;
    private JSONArray array;
    private JSONObject object;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sample_ebooks,container,false);

        closeEBookLister = view.findViewById(R.id.closeActivity);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        details = new ArrayList<>();

        closeEBookLister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        queue = Volley.newRequestQueue(view.getContext());
        request = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            array = response.getJSONArray("sampleBooks");
                            for (int i=0;i<array.length();i++) {
                                object = array.getJSONObject(i);

                                name = object.getString("name");
                                url = object.getString("url");
                                details.add(new SampleEBooksDetails(name,url));
                            }

                            adapter = new SampleBooksAdapter(view.getContext(),details);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(SampleEBooksLister.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(view.getContext(),"Please try again later",Toast.LENGTH_SHORT).show();
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
        return view;
    }

    @Override
    public void onItemClick(int position) {
        SampleEBooksDetails booksDetails = details.get(position);
        Intent nextActivity = new Intent(view.getContext(), PdfViewAuthentication.class);
        Bundle bundle = new Bundle();
        bundle.putString("url",booksDetails.getUrl());
        nextActivity.putExtras(bundle);
        dismiss();
        startActivity(nextActivity);
    }
}
