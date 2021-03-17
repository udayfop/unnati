package com.project.tmc.Async;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.PhotoUpload.PhotoUploadResponse;
import com.project.tmc.Retrofit.APIService;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.Retrofit.NetworkClient;
import com.project.tmc.utils.ApplicationActivity;

import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoUploadAsync extends AsyncTask<String, String, Response<PhotoUploadResponse>>
{
    private Response<PhotoUploadResponse> responseResponse;
    private ErrorResponse errorResponse;
    private Context mContext;
    private Gson gson = new Gson();
    private APIUtility.APIResponseListener<PhotoUploadResponse> apiResponseListener;
    private MultipartBody.Part imagePart;
    private APIUtility apiUtility = ApplicationActivity.getApiUtility();

    public PhotoUploadAsync(Context context, MultipartBody.Part imagePart, final APIUtility.APIResponseListener<PhotoUploadResponse> apiResponseListener)
    {
        this.mContext = context;
        this.apiResponseListener = apiResponseListener;
        this.imagePart = imagePart;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        apiUtility.startDialog(mContext,true);
    }

    @Override
    protected Response<PhotoUploadResponse> doInBackground(String... strings)
    {
    try {
            Retrofit retrofit = NetworkClient.getRetrofitClient();
            APIService uploadAPIs = retrofit.create(APIService.class);
        responseResponse = uploadAPIs.UPLOAD_IMAGE(imagePart).execute();
        }
    catch (IOException e) {
            e.printStackTrace();
        }

        return responseResponse;
    }

    @Override
    protected void onPostExecute(Response<PhotoUploadResponse> profileDataResponseResponse) {
        super.onPostExecute(profileDataResponseResponse);
        if (profileDataResponseResponse != null)
        {
            apiResponseListener.onReceiveResponse(profileDataResponseResponse.body());
        }
        else
        {
        try {
//                errorResponse = gson.fromJson(profileDataResponseResponse.errorBody().string(), ErrorResponse.class);
                apiResponseListener.onReceiveFailureResponse(profileDataResponseResponse.message());
            }
            catch (Exception e) {}
        }
        apiUtility.dismisDialog(true);
    }

}
