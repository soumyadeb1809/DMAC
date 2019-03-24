package in.teamconsultants.dmac.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadUtils {
    
    public static Long downloadFile(Activity activity, Uri downloadUri, String documentTitle){

        DownloadManager downloadManager = (DownloadManager)activity.getSystemService(DOWNLOAD_SERVICE);

        Log.d(AppConstants.LOG_TAG, "Downloading from link: " + downloadUri.getPath());

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setTitle(documentTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/*");

        String fileFormat = downloadUri.getLastPathSegment();

        Log.d(AppConstants.LOG_TAG, "File name: " + documentTitle + fileFormat);
        request.setDestinationInExternalPublicDir("/dmac/reports/",documentTitle + fileFormat);

        Long downloadReference = downloadManager.enqueue(request);

        Toast.makeText(activity, "Downloading file: " + documentTitle + "...", Toast.LENGTH_SHORT).show();

        return downloadReference;

    }
    
}
