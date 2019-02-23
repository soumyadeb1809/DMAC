package in.teamconsultants.dmac.network.api;


import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.network.dto.AccountCountResponse;
import in.teamconsultants.dmac.network.dto.AccountDetailResponse;
import in.teamconsultants.dmac.network.dto.AccountSearchResponse;
import in.teamconsultants.dmac.network.dto.CityResponse;
import in.teamconsultants.dmac.network.dto.CreateAccountResponse;
import in.teamconsultants.dmac.network.dto.CreateJobResponse;
import in.teamconsultants.dmac.network.dto.CreateReportResponse;
import in.teamconsultants.dmac.network.dto.FileCategoryResponse;
import in.teamconsultants.dmac.network.dto.FileCategoryWiseCountResponse;
import in.teamconsultants.dmac.network.dto.FileCountResponse;
import in.teamconsultants.dmac.network.dto.FileSearchResponse;
import in.teamconsultants.dmac.network.dto.FileTypeResponse;
import in.teamconsultants.dmac.network.dto.LoginResponse;
import in.teamconsultants.dmac.network.dto.NotificationCountResponse;
import in.teamconsultants.dmac.network.dto.NotificationTypeResponse;
import in.teamconsultants.dmac.network.dto.NotificationsResponse;
import in.teamconsultants.dmac.network.dto.QuickRegisterResponse;
import in.teamconsultants.dmac.network.dto.ReUploadFileResponse;
import in.teamconsultants.dmac.network.dto.ReportTypeResponse;
import in.teamconsultants.dmac.network.dto.ReportsResponse;
import in.teamconsultants.dmac.network.dto.StateResponse;
import in.teamconsultants.dmac.network.dto.StatusResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
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
import retrofit2.http.QueryMap;

public interface ApiInterface {

   @POST("user_login")
   @FormUrlEncoded
   Call<LoginResponse> doUserLogin(
           @Field("Email") String email,
           @Field("Password") String password
   );


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

    @GET("get_accountList")
    Call<AccountSearchResponse> doAccountSearch(
            @HeaderMap Map<String, String> headers
    );

    @GET("get_status")
    Call<StatusResponse> doGetStatus(
            @HeaderMap Map<String, String> headers,
            @Query("Type") String type
    );


    @GET("get_jobList")
    Call<FileSearchResponse> doSearchFiles(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, Object> queries
    );


    @GET("get_state_list")
    Call<StateResponse> doGetStates();


    @GET("get_city_list")
    Call<CityResponse> doGetCities();


    @GET("get_account_count")
    Call<AccountCountResponse> doGetAccountCount(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queries
    );


    @GET("get_file_count")
    Call<FileCountResponse> doGetFileCount(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queries
    );

    @POST("create_account")
    @Multipart
    Call<CreateAccountResponse> doCreateAccount(
            @HeaderMap Map<String, String> headers,
            @PartMap Map<String, RequestBody> requestBodyMap,
            @Part List<MultipartBody.Part> multipartList
    );

    @POST("job_file_re_upload")
    @Multipart
    Call<ReUploadFileResponse> doReUploadFile(
            @HeaderMap Map<String, String> headers,
            @PartMap Map<String, RequestBody> requestBodyMap,
            @Part MultipartBody.Part multipartFile
    );


    @GET("get_account_data")
    Call<AccountDetailResponse> doGetAccountDetails(
            @HeaderMap Map<String, String> headers,
            @QueryMap Map<String, String> queries
    );


    @GET("get_report_type")
    Call<ReportTypeResponse> doGetReportTypes (
            @Header("TOKEN") String token
    );


    @GET("get_report_list")
    Call<ReportsResponse> doGetReportList (
            @Header("TOKEN") String token
    );


    @POST("create_report_request")
    @FormUrlEncoded
    Call<CreateReportResponse> doCreateReportRequest (
            @Header("TOKEN") String token,
            @FieldMap Map<String, String> fieldMap
    );


    @GET("get_file_count_category_wise")
    Call<FileCategoryWiseCountResponse> doGetFileCategoryCount (
            @Header("TOKEN") String token
    );


    @GET("get_notification_type")
    Call<NotificationTypeResponse> doGetNotificationsTypes (
            @Header("TOKEN") String token
    );


    @GET("get_notification_count")
    Call<NotificationCountResponse> doGetNotificationCount (
            @Header("TOKEN") String token
    );


    @GET("get_notification_list")
    Call<NotificationsResponse> doGetNotificationList (
            @Header("TOKEN") String token,
            @Query("notification_type_id") String notificationTypeId
    );


}
