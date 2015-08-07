package app.team3.t3.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import app.team3.t3.R;

/**
 * Helper class
 */
public class AppUtiles {

    // check internet
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else
            return isInternetAvailable(context);
    }

    public static void showAlertDialog(Context context, int title, final int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (message) {
                            case R.string.message_no_internet: {
                                System.runFinalization();
                                System.exit(0);
                            }
                            default:
                                break;
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isInternetAvailable(final Context context) {
        String netAddress = null;
        NetChecker netChecker = new NetChecker(context);

        try {
            netAddress = netChecker.execute("www.google.com").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return netAddress != null;
    }


    public static class NetChecker extends AsyncTask<String, Integer, String> {

        private Context context;

        NetChecker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            InetAddress inetAddress = null;
            synchronized (context) {
                try {
                    inetAddress = InetAddress.getByName(params[0].toString());
                    Log.e("host_address", inetAddress.getHostAddress().toString());
                } catch (UnknownHostException e) {
                    return null;
                }
                return inetAddress.getHostAddress().toString();
            }
        }
    }

}
