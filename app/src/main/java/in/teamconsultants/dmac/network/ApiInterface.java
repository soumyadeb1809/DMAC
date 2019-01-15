package in.teamconsultants.dmac.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.model.CreateJobResponse;
import in.teamconsultants.dmac.model.FileCategoryResponse;
import in.teamconsultants.dmac.model.FileTypeResponse;
import in.teamconsultants.dmac.model.LoginResponse;
import in.teamconsultants.dmac.model.QuickRegisterResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {



   @POST("user_login")
   @FormUrlEncoded
   Call<LoginResponse> doUserLogin(
           @Field("Email") String email,
           @Field("Password") String password
   );


/*
    @POST("qucik_registration")
    @FormUrlEncoded
    Call<LoginResponse> doQuickRegistration(
            @Field("FullName") String fullName,
            @Field("Email") String email,
            @Field("Phone") String phone,
            @Field("Password") String password,
            @Field("BusinessLegalName") String businessLongName,
            @Field("BusinessShortName") String businessShortName,
            @Field("BusinessAddress") String businessAddress,
            @Field("CityCode") String cityCode,
            @Field("StateCode") String stateCode,
            @Field("PinCode") String pinCode,
            @Field("TypeOfEntity") String typeOfEnitity

            );
*/
    @POST("qucik_registration")
    @FormUrlEncoded
    Call<QuickRegisterResponse> doQuickRegistration(
            @FieldMap Map<String, String> fieldMap
    );



    @POST("create_job")
    @Multipart
    Call<CreateJobResponse> doCreateJob (
            @HeaderMap Map<String, String> headers,
            @PartMap Map<String, RequestBody> requestBodyMap,
            @Part List<MultipartBody.Part> multipartList
    );


    @GET("get_fileCategory")
    Call<FileCategoryResponse> doGetFileCategory(
            @HeaderMap Map<String, String> headers
    );

    @GET("get_fileType")
    Call<FileTypeResponse> doGetFileType(
            @HeaderMap Map<String, String> headers,
            @Query("FileCategory") String fileCategory
    );




}
