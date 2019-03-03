package in.teamconsultants.dmac.ui.home.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.User;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.ProfileImageUploadResponse;
import in.teamconsultants.dmac.ui.login.LoginActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.PermissionUtils;
import in.teamconsultants.dmac.utils.Utility;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private OnProfileFragmentInteractionListener mListener;

    private TextView tvName, tvEmail, tvPhone, tvRole, tvAccountName, tvKeyUser;


    private ImageButton btnUploadPhoto;

    private SharedPreferences sp;

    private ApiInterface apiInterface;
    private ProgressDialog progress;
    private String token;
    private Gson gson;
    private CircleImageView imgProfile;

    private UserData userData;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        progress = new ProgressDialog(getContext());
        sp = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);
        token = sp.getString(AppConstants.SP.TAG_TOKEN, "");
        gson = new Gson();

        initializeUi(v);

        populateData();

        return v;
    }



    private void initializeUi(View v) {

        tvName = v.findViewById(R.id.txt_name);
        tvEmail = v.findViewById(R.id.txt_email);
        tvPhone = v.findViewById(R.id.txt_phone);
        tvRole = v.findViewById(R.id.txt_role);
        tvAccountName = v.findViewById(R.id.txt_account_name);
        tvKeyUser = v.findViewById(R.id.txt_key_user);
        btnUploadPhoto = v.findViewById(R.id.upload_photo);
        imgProfile = v.findViewById(R.id.img_profile_pic);

        setOnClickListeners();

    }


    private void populateData() {

        userData = gson.fromJson(sp.getString(AppConstants.SP.TAG_USER_DETAILS, null), UserData.class);

        tvName.setText(userData.getFullName());
        tvEmail.setText(userData.getEmail());
        if(TextUtils.isEmpty(userData.getPhone())){
            tvPhone.setText("NA");
        }
        else {
            tvPhone.setText(userData.getPhone());
        }

        String role = "NA";
        if(userData.getRoleId().equals(AppConstants.USER_ROLE.FE)){
            role = "Field Executive";
        }
        else {
            role = "Customer";
        }
        tvRole.setText(role);
        tvAccountName.setText(userData.getAccountId());
        if(userData.getKeyUser().equals("1")) {
            tvKeyUser.setText("Yes");
        }
        else {
            tvKeyUser.setText("No");
        }

        String profilePicUrl = userData.getProfilePic();

        if(null == profilePicUrl || TextUtils.isEmpty(profilePicUrl.trim())){
            Picasso.get().load(R.drawable.ic_user).into(imgProfile);
        }
        else {
            Picasso.get().load(AppConstants.FILE_BASE_URL + profilePicUrl)
                    .placeholder(R.drawable.ic_user).into(imgProfile);
        }


    }

    private void setOnClickListeners() {

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PermissionUtils.checkStoragePermissions(getActivity())){
                    selectAndCropPhoto();
                }
                else {
                   mListener.askForPermission(ProfileFragment.this);
                }

            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnProfileFragmentInteractionListener {

        void askForPermission(Fragment fragment);
        void updateNavigationProfileImg(String filePath);

    }



    private void selectAndCropPhoto() {

        CropImage.activity().setAspectRatio(1, 1)
                .start(getContext(), this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                //Log.d(AppConstants.LOG_TAG, "URI:: " + resultUri.toString());
                Log.d(AppConstants.LOG_TAG, "URI PATH:: " + resultUri.getPath());


                if(resultUri != null){
                    //String filePath = Utility.getRealPathFromUri(getContext(), resultUri);
                    String filePath = resultUri.getPath();
                    uploadUserProfilePic(filePath);

                }
                else {
                    Log.d(AppConstants.LOG_TAG, "resultUri: null");
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(AppConstants.LOG_TAG, "Error occurred:: " + error);
            }
        }
    }

    private void uploadUserProfilePic(String filePath) {

        progress.setMessage("Uploading profile picture...");
        progress.setCancelable(false);
        progress.show();

        MultipartBody.Part multipartImage = Utility.getMultipartImage("ProfilePic", filePath);

        final Call<ProfileImageUploadResponse> profileImageUploadCall = apiInterface.doUploadProfilePic(token, multipartImage);

        profileImageUploadCall.enqueue(new Callback<ProfileImageUploadResponse>() {
            @Override
            public void onResponse(Call<ProfileImageUploadResponse> call, Response<ProfileImageUploadResponse> response) {

                progress.dismiss();

                ProfileImageUploadResponse profileImageUploadResponse = response.body();

                if(profileImageUploadResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){

                    userData.setProfilePic(profileImageUploadResponse.getProfilePicPath());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(AppConstants.SP.TAG_USER_DETAILS, gson.toJson(userData));
                    editor.commit();

                    String profilePicUrl = userData.getProfilePic();

                    if(null == profilePicUrl || TextUtils.isEmpty(profilePicUrl.trim())){
                        Picasso.get().load(R.drawable.ic_user).into(imgProfile);
                    }
                    else {
                        Picasso.get().load(AppConstants.FILE_BASE_URL + profilePicUrl)
                                .placeholder(R.drawable.ic_user).into(imgProfile);
                    }

                    mListener.updateNavigationProfileImg(profilePicUrl);

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Success");
                    alertBuilder.setMessage("Your profile picture has been uploaded successfully");
                    alertBuilder.setPositiveButton("OKAY", null);
                    alertBuilder.show();
                }
                else {
                    if(getActivity() != null) {
                        Utility.forceLogoutUser(getActivity());
                    }
                }

            }

            @Override
            public void onFailure(Call<ProfileImageUploadResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getActivity(), "Error", "An unknown error occurred, please try again");
                t.printStackTrace();
            }
        });

    }
}
