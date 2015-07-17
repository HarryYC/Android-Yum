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
 * Image Downloader
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    protected boolean isScale;
    protected Bitmap bitmap = null;
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

    /**
     * Return an bitmap object for display.
     *
     * @param params <tt>String</tt> for image URL
     * @return bitmap <tt>Bitmap</tt>
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        String urlString = params[0];

        try {
            URL url = new URL(urlString);
            inputStream = url.openStream();
            bufferedInputStream = new BufferedInputStream(inputStream);

            if (isScale) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            } else {
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
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

        return bitmap;
    }
}
