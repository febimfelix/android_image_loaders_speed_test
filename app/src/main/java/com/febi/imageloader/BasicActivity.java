package com.febi.imageloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.febi.imageloader.activity.GlideActivity;

import java.util.Locale;

public class BasicActivity extends AppCompatActivity {

    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        (findViewById(R.id.id_glide_button)).setOnClickListener(buttonClickListener);
        (findViewById(R.id.id_picasso_button)).setOnClickListener(buttonClickListener);
        (findViewById(R.id.id_volley_button)).setOnClickListener(buttonClickListener);
        (findViewById(R.id.id_fresco_button)).setOnClickListener(buttonClickListener);
        (findViewById(R.id.id_okhttp_button)).setOnClickListener(buttonClickListener);

        mResultTextView = (TextView) findViewById(R.id.id_result_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 10 && data != null && mResultTextView != null) {
            Bundle bundle =  data.getExtras();
            String result = "%s took %s seconds to load images";
            String library = "";
            if(bundle.getInt("library_type", 0) == 1) {
                library = "Glide";
            } else if(bundle.getInt("library_type", 0) == 2) {
                library = "Picasso";
            } else if(bundle.getInt("library_type", 0) == 3) {
                library = "Fresco";
            } else if(bundle.getInt("library_type", 0) == 4) {
                library = "Volley";
            } else if(bundle.getInt("library_type", 0) == 5) {
                library = "OKHttp";
            }
           mResultTextView .setText("\n" + String.format(Locale.getDefault(),
                   result, library, bundle.getString("time_taken")));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BasicActivity.this, GlideActivity.class);
            if(v.getId() == R.id.id_glide_button) {
                intent.putExtra("library_type", 1);
            } else if(v.getId() == R.id.id_picasso_button) {
                intent.putExtra("library_type", 2);
            } else if(v.getId() == R.id.id_fresco_button) {
                intent.putExtra("library_type", 3);
            } else if(v.getId() == R.id.id_volley_button) {
                intent.putExtra("library_type", 4);
            } else if(v.getId() == R.id.id_okhttp_button) {
                intent.putExtra("library_type", 5);
            }
            startActivityForResult(intent, 10);
        }
    };
}
