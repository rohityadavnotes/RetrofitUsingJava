package com.retrofit.using.java.data.remote;

import com.google.gson.JsonObject;
import com.retrofit.using.java.model.BaseResponse;
import com.retrofit.using.java.model.Data;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @GET(RemoteConfiguration.EMPLOYEE)
    Observable<Response<JsonObject>> getEmployee();

    @GET(RemoteConfiguration.EMPLOYEE_LIST)
    Observable<Response<JsonObject>> getEmployeeList();

    @FormUrlEncoded
    @POST(RemoteConfiguration.GET_USERS)
    Observable<Response<JsonObject>> getPage(
            @Field("apiKey") String key,
            @Field("pageNumber") String pageNumber);

    @Multipart
    @POST(RemoteConfiguration.SIGN_UP)
    Observable<Response<BaseResponse<Data>>> signUp(
            @Part MultipartBody.Part profilePic,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("gender") RequestBody gender,
            @Part("countryCode") RequestBody countryCode,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("fcmToken") RequestBody fcmToken);

    @FormUrlEncoded
    @POST(RemoteConfiguration.SIGN_IN)
    Observable<Response<BaseResponse<Data>>> signIn(
            @Field("email") String email,
            @Field("password") String password,
            @Field("fcmToken") String fcmToken);

    @FormUrlEncoded
    @POST(RemoteConfiguration.SOCIAL_SIGN_IN)
    Observable<Response<JsonObject>> socialSignIn(
            @Field("provider") String provider,
            @Field("socialId") String socialId,
            @Field("picture") String picture,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("email") String email,
            @Field("fcmToken") String fcmToken);
}
