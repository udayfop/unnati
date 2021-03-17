package com.project.tmc.Retrofit;

import com.project.tmc.Models.BottomSheet.BottomSheetResponse;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResponse;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResult;
import com.project.tmc.Models.LatLongData.LatLongResponse;
import com.project.tmc.Models.MobileVerification.MobileVerificationResponse;
import com.project.tmc.Models.PhotoUpload.PhotoUploadResponse;
import com.project.tmc.Models.Theme.ThemeResponse;
import com.project.tmc.Models.sabha.SabhaRequest;
import com.project.tmc.Models.sabha.SabhaResponse;
import com.project.tmc.Models.sabhaList.SabhaListResponse;
import com.project.tmc.Models.uploadtext.UploadImageUrlResponse;
import com.project.tmc.Models.uploadtext.UploadTextDataRequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface APIService {

//    BASE_URL = "http://api.adroitiot.in/api/"

    @Headers("Content-Type: application/json")
    @POST("sabha")
    Call<SabhaResponse> SABHA(@Body SabhaRequest request);

    @GET()
    Call<SabhaListResponse> SABHA_LIST(@Url String url);

    @Multipart
    @POST("track_api/driver/UploadFile")
    Call<PhotoUploadResponse> UPLOAD_IMAGE(@Part MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @POST("sabhadetail")
    Call<UploadImageUrlResponse> UPLOAD_IMAGE_URL(@Body UploadTextDataRequest request);

    @GET()
    Call<MobileVerificationResponse> MOBILE_VERIFICAITON(@Url String url);

    @GET()
    Call<ThemeResponse> THEME(@Url String url);

    @GET()
    Call<BottomSheetResponse> BOTTOM_SHEET(@Url String url);

    @GET()
    Call<BottomSheetImageResponse> SHOW_IMAGES(@Url String url);

    @GET()
    Call<LatLongResponse> DEVICE_GPS(@Url String url);
}
