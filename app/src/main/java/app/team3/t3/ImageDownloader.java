package app.team3.t3;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ivan on 6/29/15.
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    ProgressBar progressBar;
    boolean isScale;
    int width;
    int height;

    public ImageDownloader(ProgressBar progressBar, ImageView imageView, int width, int height) {
        this.progressBar = progressBar;
        this.imageView = imageView;
        isScale = true;
        this.width = width;
        this.height = height;
    }

    public ImageDownloader(ProgressBar progressBar, ImageView imageView) {
        this.progressBar = progressBar;
        isScale = false;
        this.imageView = imageView;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urlString = params[0];
        Bitmap applicationIcon = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            if (isScale) {
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                options2.inSampleSize = 2;

                applicationIcon = BitmapFactory.decodeStream(inputStream, null, options2);
                applicationIcon = applicationIcon.createScaledBitmap(applicationIcon, width, (height * 2 / 5), false);
            } else {
                applicationIcon = BitmapFactory.decodeStream(inputStream);
            }

            httpURLConnection.disconnect();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return applicationIcon;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        progressBar.setVisibility(View.GONE);
    }
}
