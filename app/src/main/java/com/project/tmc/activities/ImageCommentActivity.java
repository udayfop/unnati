package com.project.tmc.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tmc.Adapters.ImagesListAdapter;
import com.project.tmc.Async.PhotoUploadAsync;
import com.project.tmc.BuildConfig;
import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.Error.ErrorResponse;
import com.project.tmc.Models.PhotoUpload.PhotoUploadResponse;
import com.project.tmc.Models.SabhaHeader.SabhaHeaderRequest;
import com.project.tmc.Models.SabhaHeader.SabhaLineRequest;
import com.project.tmc.Models.uploadtext.UploadImageUrlResponse;
import com.project.tmc.Models.uploadtext.UploadTextDataRequest;
import com.project.tmc.R;
import com.project.tmc.Retrofit.APIUtility;
import com.project.tmc.fragments.DashboardFragment;
import com.project.tmc.fragments.SabhaListFragment;
import com.project.tmc.utils.ApplicationActivity;
import com.project.tmc.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.project.tmc.Extra.Prefrences.getPreference;
import static com.project.tmc.Extra.Prefrences.getPreference_boolean;
import static com.project.tmc.Extra.Prefrences.getPreference_int;

public class ImageCommentActivity extends AppCompatActivity {
    private int TAKE_PICTURE = 1;
    private int MY_CAMERA_REQUEST_CODE = 2;
    private File mPhotoFile;
    private String dir;
    private int REQUEST_FINE_LOCATION = 100;
    private int SELECT_PICTURES = 1004;
    public String imageStoragePath;
    public String GALLERY_DIRECTORY_NAME = "TMC";
    private String IMAGE_EXTENSION = "jpg";

    private LocationManager locationManager;
    public double latitude;
    public double longitude;
    public Criteria criteria;
    public String bestProvider;
    private ArrayList<File> imageList;
    private ArrayList<SabhaLineRequest> imageUrlList;
    private UploadTextDataRequest uploadedImageListUrl;
    private ArrayList<Boolean> checkImageUploadList;

    Button submit_all;
    TextView browse_btn;
    private ImageView party_logo;
    private ImagesListAdapter imagesListAdapter;
    private RecyclerView recycler_image;
    private EditText et_sabha_comments;
    private TextView sabha_name, uploadedCount;
    Button upload_btn;
    private boolean callUpload = true;
    private int imageListCount = 0, sabhaId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_comment);
        imageList = new ArrayList<>();
        browse_btn = (TextView) findViewById(R.id.browse_btn);

        upload_btn = (Button) findViewById(R.id.upload_btn);
        submit_all = (Button) findViewById(R.id.submit_all);
        et_sabha_comments = (EditText) findViewById(R.id.et_sabha_comments);
        sabha_name = (TextView) findViewById(R.id.sabha_name_description);
        checkImageUploadList = new ArrayList<>();
        uploadedCount = (TextView) findViewById(R.id.uploadedCount);
        imageUrlList = new ArrayList<>();
        recycler_image = (RecyclerView) findViewById(R.id.recycler_image);
        party_logo = (ImageView) findViewById(R.id.party_logo);
        recycler_image.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recycler_image.setHasFixedSize(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("");

        if (getPreference_boolean(this,Constants.VERIFIED))
        {

            party_logo.setImageResource(R.drawable.logo_unnati);
        }



        Intent intent = getIntent();
        if (intent != null) {
            sabha_name.setText(intent.getStringExtra("sabhaName"));
            sabhaId = intent.getIntExtra("sabhaId", 0);
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, MY_CAMERA_REQUEST_CODE);
        }

        browse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, MY_CAMERA_REQUEST_CODE);
                } else {
                    cameraGalleryDialog();
                }
            }
        });


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callUpload) {
                    checkImageUploadList.clear();
                    for (int i = 0; i < imageList.size(); i++) {
                        checkImageUploadList.add(true);
                    }

                    recycler_image.setAdapter(new ImagesListAdapter(imageList, checkImageUploadList, true, false));

                    if (imageList.size() > 0) {

                        uplodImge(MultipartBody.Part.createFormData("ImageUrl", "Profile_" +
                                getCurrentDate() + ".jpeg", RequestBody.create(MediaType.parse("image/jpeg"),
                                new File(CommonUtils.compressImage(ImageCommentActivity.this, imageList.get(imageListCount).getPath())))));

                    }

                    upload_btn.setVisibility(View.GONE);
                    browse_btn.setVisibility(View.GONE);

                }
                callUpload = false;
            }
        });

        submit_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageUrl(new UploadTextDataRequest(new SabhaHeaderRequest(
                        getPreference(ImageCommentActivity.this, Constants.MOBILE),
                        sabhaId, et_sabha_comments.getText().toString(),
                        imageUrlList.size(), imageListCount), imageUrlList));
            }

        });


    }

    private void saveImageUrl(UploadTextDataRequest uploadedImageListUrl) {
        if (CommonUtils.isNetworkAvailable(ImageCommentActivity.this)) {
            ApplicationActivity.getApiUtility().saveImageTextData(ImageCommentActivity.this,
                    uploadedImageListUrl, true,
                    new APIUtility.APIResponseListener<UploadImageUrlResponse>() {
                        @Override
                        public void onReceiveResponse(UploadImageUrlResponse response) {
                            Toast.makeText(ImageCommentActivity.this,
                                    response.getResult().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {

                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {

                        }
                    });
        } else {
//            CommonUtils.displayNetworkAlert(getActivity(), true);
        }
    }

    private void uplodImge(MultipartBody.Part imagePart) {
        if (CommonUtils.isNetworkAvailable(ImageCommentActivity.this)) {
            PhotoUploadAsync profileUploadAsync = new PhotoUploadAsync(ImageCommentActivity.this, imagePart,
                    new APIUtility.APIResponseListener<PhotoUploadResponse>() {
                        @Override
                        public void onReceiveResponse(PhotoUploadResponse response) {
                            if (response != null) {

                                Log.e("SuccessRessponse", "Success " + imageListCount);
                                uploadedCount.setVisibility(View.VISIBLE);

                                checkImageUploadList.set(imageListCount, true);
                                recycler_image.setAdapter(new ImagesListAdapter(imageList, checkImageUploadList, true, true));
                                imageListCount = imageListCount + 1;
                                imageUrlList.add(new SabhaLineRequest(response.getResult()));

                                if (imageListCount < imageList.size()) {
                                    uplodImge(MultipartBody.Part.createFormData("ImageUrl", "Profile_" +
                                            getCurrentDate() + ".jpeg", RequestBody.create(MediaType.parse("image/jpeg"),
                                            new File(CommonUtils.compressImage(ImageCommentActivity.this, imageList.get(imageListCount).getPath())))));
                                } else {
//                                        uploadTextData();
                                }
                                uploadedCount.setText((imageListCount) + "/" + imageList.size());
                            }
                        }

                        @Override
                        public void onReceiveErrorResponse(ErrorResponse errorResponse) {
                            if (errorResponse.getErrorMessage() != null) {
//                                    CommonUtils.alert(getActivity(), CommonUtils.errorMessageArray(errorResponse.getErrorMessage().getMessageList()));
                            } else {
//                                    CommonUtils.alert(getActivity(), errorResponse.getMessage());
                            }
                            checkImageUploadList.set(imageListCount, false);
                            recycler_image.setAdapter(new ImagesListAdapter(imageList, checkImageUploadList, true, true));

                        }

                        @Override
                        public void onReceiveFailureResponse(String response) {
//                                CommonUtils.alert(getActivity(),  response);
                            checkImageUploadList.set(imageListCount, false);
                            recycler_image.setAdapter(new ImagesListAdapter(imageList, checkImageUploadList, true, true));
                        }
                    });
            profileUploadAsync.execute();
        } else {
//            CommonUtils.displayNetworkAlert(getActivity(), true);
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String dateTime = sdf.format(new Date());
        return dateTime;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void cameraGalleryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ImageCommentActivity.this);
        LayoutInflater layoutInflater = (ImageCommentActivity.this).getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.browse_popup_layout, null);
        Button camera = (Button) customView.findViewById(R.id.camera);
        Button gallery = (Button) customView.findViewById(R.id.gallery);
        builder.setView(customView);
        final AlertDialog alert = builder.create();
        alert.setCancelable(true);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                alert.cancel();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGallery();
                alert.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PICTURE);

            }
        }
    }

    void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && TAKE_PICTURE == requestCode) {
            if (requestCode == TAKE_PICTURE) {
                imageList.add(mPhotoFile);
                checkImageUploadList.add(true);
            }
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
//            } else {
//                getLocation();
//            }
        } else if (resultCode == RESULT_OK && SELECT_PICTURES == requestCode) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    try {
                        imageList.add(getFile(ImageCommentActivity.this, data.getClipData().getItemAt(i).getUri()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    checkImageUploadList.add(true);
                }
            }
        } else if (data.getData() != null) {
            String imagePath = data.getData().getPath();
            try {
                imageList.add(getFile(ImageCommentActivity.this, Uri.parse(imagePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkImageUploadList.add(true);
        }

        if (imageList != null) {
            upload_btn.setVisibility(View.VISIBLE);
        }

        recycler_image.setAdapter(new ImagesListAdapter(imageList, null, false, false));
    }

    public File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}