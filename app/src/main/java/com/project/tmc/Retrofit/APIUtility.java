package com.project.tmc.Retrofit;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.BottomSheet.BottomSheetResponse;
import com.project.tmc.Models.BottomSheetImages.BottomSheetImageResponse;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.LatLongData.LatLongResponse;
import com.project.tmc.Models.MobileVerification.MobileVerificationResponse;
import com.project.tmc.Models.Theme.ThemeResponse;
import com.project.tmc.Models.sabha.SabhaRequest;
import com.project.tmc.Models.sabha.SabhaResponse;
import com.project.tmc.Models.sabhaList.SabhaListResponse;
import com.project.tmc.Models.uploadtext.UploadImageUrlResponse;
import com.project.tmc.Models.uploadtext.UploadTextDataRequest;
import com.project.tmc.R;
import com.project.tmc.Retrofit.retrofithelper.APIServiceGenerator;
import com.project.tmc.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtility {

    private APIService mApiService = null;
    private static final String BASE_URL = Constants.BASE_URL;
    private Gson gson = new Gson();
    private ErrorResponse errorResponse;

    public interface APIResponseListener<T> {
        void onReceiveResponse(T response);

        void onReceiveErrorResponse(ErrorResponse errorResponse);

        void onReceiveFailureResponse(String response);
    }

    public APIUtility(Context context) {
        APIServiceGenerator.setBaseUrl(BASE_URL);
        mApiService = APIServiceGenerator.createService(APIService.class);
    }


    public void startDialog(Context context, boolean isDialog) {
        if (isDialog) {
            CustomDialog.showDialog(context);
        }
    }

    public void dismisDialog(boolean isDialog) {
        if (isDialog) {
            CustomDialog.dismissDialog();
        }
    }

    public void sabha(final Context context,  SabhaRequest versionCheckReques, boolean isDialog,
                            final APIResponseListener<SabhaResponse> apiResponseListener) {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.SABHA(versionCheckReques).enqueue(new Callback<SabhaResponse>() {
                @Override
                public void onResponse(Call<SabhaResponse> call, Response<SabhaResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    } else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<SabhaResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void sabhaList(final Context context,  String url, boolean isDialog,
                      final APIResponseListener<SabhaListResponse> apiResponseListener) {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.SABHA_LIST(url).enqueue(new Callback<SabhaListResponse>() {
                @Override
                public void onResponse(Call<SabhaListResponse> call, Response<SabhaListResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    } else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<SabhaListResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveImageTextData(final Context context, UploadTextDataRequest request, boolean isDialog,
                                  final APIResponseListener<UploadImageUrlResponse> apiResponseListener)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.UPLOAD_IMAGE_URL(request).enqueue(new Callback<UploadImageUrlResponse>() {
                @Override
                public void onResponse(Call<UploadImageUrlResponse> call, Response<UploadImageUrlResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    } else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<UploadImageUrlResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void mobileVerification(final Context context, String url, boolean isDialog,
                                  final APIResponseListener<MobileVerificationResponse> apiResponseListener)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.MOBILE_VERIFICAITON(url).enqueue(new Callback<MobileVerificationResponse>() {
                @Override
                public void onResponse(Call<MobileVerificationResponse> call, Response<MobileVerificationResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    }
                    else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<MobileVerificationResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void theme(final Context context, String url, boolean isDialog,
                                   final APIResponseListener<ThemeResponse> apiResponseListener)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.THEME(url).enqueue(new Callback<ThemeResponse>() {
                @Override
                public void onResponse(Call<ThemeResponse> call, Response<ThemeResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    } else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<ThemeResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void bottomSheet(final Context context, String url, boolean isDialog,
                                   final APIResponseListener<BottomSheetResponse> apiResponseListener)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.BOTTOM_SHEET(url).enqueue(new Callback<BottomSheetResponse>() {
                @Override
                public void onResponse(Call<BottomSheetResponse> call, Response<BottomSheetResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    }
                    else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<BottomSheetResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void bottomSheetImage(final Context context, String url, boolean isDialog,
                            final APIResponseListener<BottomSheetImageResponse> apiResponseListener)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.SHOW_IMAGES(url).enqueue(new Callback<BottomSheetImageResponse>() {
                @Override
                public void onResponse(Call<BottomSheetImageResponse> call, Response<BottomSheetImageResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    }
                    else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<BottomSheetImageResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void devicGps(final Context context, String url, boolean isDialog,
                            final APIResponseListener<LatLongResponse> apiResponseListener)
    {
        if (CommonUtils.isNetworkAvailable(context)) {
            startDialog(context, isDialog);
            mApiService.DEVICE_GPS(url).enqueue(new Callback<LatLongResponse>() {
                @Override
                public void onResponse(Call<LatLongResponse> call, Response<LatLongResponse> response) {
                    dismisDialog(true);

                    if (response.isSuccessful())
                    {
                        apiResponseListener.onReceiveResponse(response.body());
                    }
                    else
                    {
                        try {
                            errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            apiResponseListener.onReceiveErrorResponse(errorResponse);
                        } catch (Exception e) {
                        }
                    }
                }
                @Override
                public void onFailure(Call<LatLongResponse> call, Throwable throwable) {
                    apiResponseListener.onReceiveFailureResponse(context.getResources().getString(R.string.something_error));
                    dismisDialog(true);
                }
            });
        } else {
            Toast.makeText(context, "Check your Network Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
