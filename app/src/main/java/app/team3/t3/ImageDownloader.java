package app.team3.t3;

import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by ivan on 6/29/15.
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    boolean scale;
    WindowManager windowManager;
    Display display;

    public ImageDownloader(Context context, ImageView imagView, boolean scaleImg) {
        this.imageView = imagView;
        scale = scaleImg;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap applicationIcon = null;
        Point size = new Point();
        display.getSize(size);
        int inWidth = size.x;
        int inHeight = size.y;
        try {
            InputStream inputStream = new URL(url).openStream();
            applicationIcon = BitmapFactory.decodeStream(inputStream);

            if (scale) {
                applicationIcon = Bitmap.createScaledBitmap(applicationIcon, inWidth, inHeight * 3 / 5, true);
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return applicationIcon;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}