package in.teamconsultants.dmac.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;

public class Utility {

    public static void showAlert(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OKAY", null);
        builder.show();
    }

    public static Map<String, String> getHeader(String token){
        Map<String, String> headers = new HashMap<>();
        headers.put("TOKEN", token);
        return headers;
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


}
