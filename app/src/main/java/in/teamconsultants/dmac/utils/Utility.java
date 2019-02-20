package in.teamconsultants.dmac.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.jobs.NewJobActivity;
import in.teamconsultants.dmac.ui.registration.RegisterActivity;
import in.teamconsultants.dmac.ui.registration.RegularRegistrationActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

import static android.content.Context.CONNECTIVITY_SERVICE;

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


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isPermissionsGranted(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ) {

            return false;

        }
        else {
            return true;
        }
    }

    public static void askForPermissions(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CAMERA}, 0);
    }

    public static void showImageSelectionDialog(final Activity activity, final int requestCode){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_image_source_selecction, null);
        builder.setView(dialogLayout);

        final AlertDialog dialog = builder.create();

        LinearLayout grpCamera = dialogLayout.findViewById(R.id.grp_snap_image);
        LinearLayout grpGallery = dialogLayout.findViewById(R.id.grp_open_gallery);

        grpCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(activity, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                activity.startActivityForResult(intent, requestCode);
                dialog.dismiss();
            }
        });

        grpGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int preference = ScanConstants.OPEN_MEDIA;
                Intent intent = new Intent(activity, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                activity.startActivityForResult(intent, requestCode);
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void showDatePicker(Context context, final TextView tvDate) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvDate.setText(addZeroB4DtElem(dayOfMonth)+"-" +addZeroB4DtElem((monthOfYear+1))+"-"+year);

                    }
                }, year, month, day);

        datePickerDialog.show();

    }


    public static String addZeroB4DtElem(int dateElement){
        return String.valueOf((dateElement)<10 ? "0"+(dateElement) : (dateElement));
    }



}
