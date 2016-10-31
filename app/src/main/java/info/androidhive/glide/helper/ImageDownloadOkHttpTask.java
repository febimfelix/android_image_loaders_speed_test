package info.androidhive.glide.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Febi.M.Felix on 31/10/16.
 */

public class ImageDownloadOkHttpTask extends AsyncTask<Object, Void, Bitmap> {
    ImageView mImageView;

    @Override
    protected Bitmap doInBackground(Object... params) {
        mImageView              = (ImageView) params[1];
        OkHttpClient client     = new OkHttpClient();
        Request request         = new Request.Builder().url((String) params[0]).build();

        Response response       = null;
        Bitmap bitmap           = null;
        try {
            response            = client.newCall(request).execute();
            if(response != null) {
                InputStream inputStream = response.body().byteStream();
                bitmap          = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
