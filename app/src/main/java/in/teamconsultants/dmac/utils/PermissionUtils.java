package in.teamconsultants.dmac.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class PermissionUtils {

    public static boolean checkStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {

            return false;

        }
        else {
            return true;
        }

    }


    public static void askForStoragePermissions(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, }, 0);

        Toast.makeText(activity, "Please grant the requested permissions and try again", Toast.LENGTH_SHORT).show();


    }
}
