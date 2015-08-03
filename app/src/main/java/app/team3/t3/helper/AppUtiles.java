package app.team3.t3.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import app.team3.t3.R;

/**
 * Helper class
 */
public class AppUtiles {

    // check internet
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null) {
            return false;
        } else
            return true;
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
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
    }
}
