package app.team3.t3;

import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ivan on 6/29/15.
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    protected boolean isScale;
    protected Bitmap applicationIcon = null;
    protected BufferedInputStream bufferedInputStream;
    protected InputStream inputStream;
    protected int width;
    protected int height;

    public ImageDownloader(int width, int height) {
        isScale = true;
        this.width = width;
        this.height = height;
    }

    public ImageDownloader() {
        isScale = false;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urlString = params[0];

        try {
            URL url = new URL(urlString);
            inputStream = url.openStream();
            bufferedInputStream = new BufferedInputStream(inputStream);

            if (isScale) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                applicationIcon = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                applicationIcon = Bitmap.createScaledBitmap(applicationIcon, width, (height * 2 / 5), false);
            } else {
                applicationIcon = BitmapFactory.decodeStream(bufferedInputStream);
            }

            if (inputStream != null) {
                inputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return applicationIcon;
    }
}
