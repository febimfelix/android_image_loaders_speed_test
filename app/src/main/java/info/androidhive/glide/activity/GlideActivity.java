package info.androidhive.glide.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.androidhive.glide.R;
import info.androidhive.glide.adapter.GalleryAdapter;
import info.androidhive.glide.app.AppController;
import info.androidhive.glide.model.Image;
import okhttp3.MediaType;

public class GlideActivity extends AppCompatActivity {

    private String TAG          = GlideActivity.class.getSimpleName();
    private String endpoint     = "http://api.androidhive.info/json/glide.json";
    private ArrayList<String> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private Chronometer mChronometer;

    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView    = (RecyclerView) findViewById(R.id.recycler_view);
        mChronometer    = (Chronometer) findViewById(R.id.id_chronometer);

        int libraryType = getIntent().getIntExtra("library_type", 0);
        pDialog         = new ProgressDialog(this);
        images          = new ArrayList<>();
        mAdapter        = new GalleryAdapter(getApplicationContext(), images, libraryType);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

         recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(
                 getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle   = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mChronometer != null) {
            mChronometer.start();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("time_taken", mChronometer.getText());
        intent.putExtra("library_type", getIntent().getIntExtra("library_type", 0));
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mChronometer != null) {
            mChronometer.stop();
        }
    }

    private void populateImages() {
        images.add("http://www.zameen.com/common/resize.php?img=4/9711182_3983379.jpg");
        images.add("https://cdn.myhubber.com/uploads/user/20161006103613image.jpeg");
        images.add("https://cdn.myhubber.com/uploads/user/20161009070405image.jpeg");
        images.add("https://cdn.myhubber.com/uploads/user/20161006083406image.jpeg");
        images.add("https://apiideaprodigies.quickblox.com/blobs/5faae0544efb42a2883f59094862486800.json");
        images.add("https://apiideaprodigies.quickblox.com/blobs/1e53aa5fff394167b8d17df3db85491c00");
        images.add("https://cdn.myhubber.com/uploads/user/20160808100852image_-1858269274.jpg");
        images.add("https://cdn.myhubber.com/uploads/user/20160919024304image_-121043074.jpg");
        images.add("https://cdn.myhubber.com/uploads/user/20161008090136image.jpeg");
        images.add("https://cdn.myhubber.com/uploads/user/20160929063639image_-1589186007.jpg");

        mAdapter.notifyDataSetChanged();
    }

    private void fetchImages() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object   = response.getJSONObject(i);
                                Image image         = new Image();
                                image.setName(object.getString("name"));

                                JSONObject url      = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                images.add(image.getMedium());

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}