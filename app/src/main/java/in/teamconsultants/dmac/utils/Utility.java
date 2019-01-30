package in.teamconsultants.dmac.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

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


    public static String getFormattedDate(String dateTime){
        String result = dateTime;
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
        try{
            Date date = simpleDateFormat.parse(dateTime);
            String datePattern = "HH:mm, dd MMM yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            result = dateFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static RequestBody getTextRequestBody(String text){
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }

    public static MultipartBody.Part getMultipartImage(String partName,String filePath){
            File file = new File(filePath);
            Log.d(AppConstants.LOG_TAG, "Filename " + file.getName());
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData(partName, file.getName(), mFile);
            return fileToUpload;
    }

}
