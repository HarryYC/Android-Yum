package app.team3.t3;

import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ivan on 6/29/15.
 * Image Downloader
 */
public class ImageDownloader extends AsyncTask<String, Void, Boolean> {

    private ImageView imageView;
    private boolean isScale;
    private Bitmap bitmap = null;
    private BufferedInputStream bufferedInputStream;
    private InputStream inputStream;
    private int width;
    private int height;

    public ImageDownloader(ImageView imageView, int width, int height) {
        this.imageView = imageView;
        isScale = true;
        this.width = width;
        this.height = height;
    }

    public ImageDownloader(ImageView imageView) {
        this.imageView = imageView;
        isScale = false;
    }

    /**
     * Return an bitmap object for display.
     *
     * @param params <tt>String</tt> for image URL
     * @return bitmap <tt>Bitmap</tt>
     */
    @Override
    protected Boolean doInBackground(String... params) {
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

        return true;
    }

    protected void onPostExecute(Boolean result) {
        this.imageView.setImageBitmap(bitmap);
    }
}