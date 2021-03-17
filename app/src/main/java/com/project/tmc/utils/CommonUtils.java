package com.project.tmc.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.project.tmc.Extra.Constants;
import com.project.tmc.Models.Error.Message;
import com.project.tmc.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.tmc.Extra.Prefrences.getPreference_int;

public class CommonUtils {


//    public static int refreshTime(Context mContext) {
//        int time = 0;
//        if (getPreference_int(mContext, Constants.REFRESHTIME) == 0) {
//            return 30000;
//        } else if (getPreference_int(mContext, Constants.REFRESHTIME) == 1) {
//            time = 15000;
//        } else if (getPreference_int(mContext, Constants.REFRESHTIME) == 2) {
//            time = 30000;
//        } else if (getPreference_int(mContext, Constants.REFRESHTIME) == 3) {
//            time = 45000;
//        } else if (getPreference_int(mContext, Constants.REFRESHTIME) == 4) {
//            time = 60000;
//        }
//        return time;
//    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                componentInfo = taskInfo.get(0).topActivity;
            }
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
    public static String errorMessageArray(ArrayList<Message> messages) {
        String stringMessage = "";
        for (Message message : messages) {
            stringMessage = stringMessage + "\n" + message.getErrorMessage();
        }

        return stringMessage;
    }


    public static void alert(final Context context, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_dialog_layout, null);
        TextView title = (TextView) customView.findViewById(R.id.title);
        TextView message = (TextView) customView.findViewById(R.id.message);
        TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
        TextView cancel_btn = (TextView) customView.findViewById(R.id.cancel_btn);
        RelativeLayout rl_seperator = (RelativeLayout) customView.findViewById(R.id.rl_seperator);
        rl_seperator.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        message.setText(msg);
        builder.setView(customView);
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        cancel_btn.setVisibility(View.GONE);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();

    }



//    public static void setLanguage(Context context) {
//        String language = null;
//        if (getPreference_int(context, Constants.LANGUAGE) == 1) {
//            language = "hi";
//        } else if (getPreference_int(context,Constants.LANGUAGE)==2){
//            language="mr";
//        }
//        else {
//            language = "en";
//        }
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
//    }

    public static String parsedateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        //String outputPattern = "dd-MMM-yyyy";
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

//    public static void dialogNearby(final Context mContext, final LatLng latLng) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppDialogTheme);
//        LayoutInflater layoutInflater = ((AppCompatActivity) mContext).getLayoutInflater();
//        View customView = layoutInflater.inflate(R.layout.nearby_dialog_layot, null);
//        final TextView atm = (TextView) customView.findViewById(R.id.atm);
//        final TextView petrol_pump = (TextView) customView.findViewById(R.id.petrol_pump);
//        final TextView hospitals = (TextView) customView.findViewById(R.id.hospitals);
//        final TextView police_station = (TextView) customView.findViewById(R.id.police_station);
//        final TextView service_points = (TextView) customView.findViewById(R.id.service_points);
//        final TextView shopping_malls = (TextView) customView.findViewById(R.id.shopping_malls);
//        final TextView restaurants = (TextView) customView.findViewById(R.id.restaurants);
//        final TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
//
//        builder.setView(customView);
//        final AlertDialog alert = builder.create();
//        final AlertDialog finalAlert = alert;
//
//        atm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/atm/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        petrol_pump.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/Petrol+Pump/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        hospitals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/hospital/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        police_station.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/Police+Station/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        service_points.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/Service+Point/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        shopping_malls.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/Shopping+mall/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        restaurants.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://www.google.com/maps/search/Restaurant/@" + latLng.latitude + "," +
//                                "" + latLng.longitude + ",15z/data=!3m1!4b1")).setPackage("com.google.android.apps.maps"));
//                alert.dismiss();
//            }
//        });
//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//
//        alert.show();
//
//    }

    public static String getRealPathFromURI(Context mContaxt, Uri contentURI) {
        String filePath;
        Cursor cursor = mContaxt.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }

//    public static void launchCameraIntent(Context mContext) {
//        Intent intent = new Intent(mContext, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        // setting maximum bitmap width and height
//       /* intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);*/
//        ((AppCompatActivity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
//    }
//
//    public static void launchGalleryIntent(Context mContext) {
//        Intent intent = new Intent(mContext, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        ((AppCompatActivity)mContext).startActivityForResult(intent, REQUEST_IMAGE);
//    }
//
//    public static SpinnerModel getItemName(int id, ArrayList<SpinnerModel>spinnerModels)
//    {
//
//        for (int i= 0; i<spinnerModels.size(); i++)
//        {
//            if (spinnerModels.get(i).getId() == id)
//            {
//                return spinnerModels.get(i);
//            }
//        }
//        return null;
//    }



//    public static void setTheme(final Context mContext)
//    {
//        int themeCode = getPreference_int(mContext, Constants.THEME_TYPE);
////        switch (themeCode)
////        {
////            case 0:
////                mContext.setTheme(R.style.AppTheme);
////                break;
////            case 1:
//        if (themeCode !=0 || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//        {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            ((AppCompatActivity)mContext).getDelegate().applyDayNight();
//        }
//
////        }
//
//    }


    public static String getDurationString(int seconds) {
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

//    public static void moveCamera(LatLng destination, GoogleMap mMap) {
//        Projection projection = mMap.getProjection();
//        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
//        int boundsTopY = projection.toScreenLocation(bounds.northeast).y;
//        int boundsBottomY = projection.toScreenLocation(bounds.southwest).y;
//        int boundsTopX = projection.toScreenLocation(bounds.northeast).x;
//        int boundsBottomX = projection.toScreenLocation(bounds.southwest).x;
//
//        int offsetY = (boundsBottomY - boundsTopY) / 2;
//        int offsetX = (boundsTopX - boundsBottomX) / 2;
//
//        Point destinationPoint = projection.toScreenLocation(destination);
//        int destinationX = destinationPoint.x;
//        int destinationY = destinationPoint.y;
//
//        int scrollX = 0;
//        int scrollY = 0;
//
//        if (destinationY <= (boundsTopY + offsetY)) {
//            scrollY = -(Math.abs((boundsTopY + offsetY) - destinationY));
//        } else if (destinationY >= (boundsBottomY - offsetY)) {
//            scrollY = (Math.abs(destinationY - (boundsBottomY - offsetY)));
//        }
//        if (destinationX >= (boundsTopX - offsetX)) {
//            scrollX = (Math.abs(destinationX - (boundsTopX - offsetX)));
//        } else if (destinationX <= (boundsBottomX + offsetX)) {
//            scrollX = -(Math.abs((boundsBottomX + offsetX) - destinationX));
//        }
//        mMap.animateCamera(CameraUpdateFactory.scrollBy(scrollX, scrollY));
//
//        //  marker.setPosition(destination);
//    }
//
//    public static float getBearing(LatLng begin, LatLng end) {
//        double lat = Math.abs(begin.latitude - end.latitude);
//        double lng = Math.abs(begin.longitude - end.longitude);
//        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
//            return (float) (Math.toDegrees(Math.atan(lng / lat)));
//        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
//        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
//            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
//        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
//        return -1;
//    }
//
//    public interface LatLngInterpolator {
//        LatLng interpolate(float fraction, LatLng a, LatLng b);
//
//        class LinearFixed implements LatLngInterpolator {
//            @Override
//            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
//                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
//                double lngDelta = b.longitude - a.longitude;
//                // Take the shortest path across the 180th meridian.
//                if (Math.abs(lngDelta) > 180) {
//                    lngDelta -= Math.signum(lngDelta) * 360;
//                }
//                double lng = lngDelta * fraction + a.longitude;
//                return new LatLng(lat, lng);
//            }
//        }
//    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    private static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

//    public static boolean slideUpDown(final Context context, final View hiddenPanel) {
//        boolean bloor =false;
//        if (!isPanelShown(hiddenPanel)) {
//            Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
//            hiddenPanel.startAnimation(bottomUp);
//            hiddenPanel.setVisibility(View.VISIBLE);
//            bloor = true;
//        }
//        else {
//            Animation bottomDown = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
//            hiddenPanel.startAnimation(bottomDown);
//            hiddenPanel.setVisibility(View.GONE);
//            bloor = false;
//        }
//        return bloor;
//    }

    private static boolean isPanelShown(View hiddenPanel) {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    public static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;
        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

//    public static File getOutputMediaFile(int type) {
//
//        // External sdcard location
//        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), "/GPSIndiaImage");
//
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.e(DashboardActivity.GALLERY_DIRECTORY_NAME, "Oops! Failed create "
//                        + DashboardActivity.GALLERY_DIRECTORY_NAME + " directory");
//                return null;
//            }
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == DashboardActivity.MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + "." + DashboardActivity.IMAGE_EXTENSION);
//        } else {
//            return null;
//        }
//
//        return mediaFile;
//    }

    public static String compressImage(Context context, String imageUri) {

        String filename = null;

        String filePath = getRealPathFromURI(context, imageUri);
//        Log.e("ajlglakslkg", filePath);

        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        if (options.outHeight != 0 && options.outWidth != 0) {

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 1000.0f;
            float maxWidth = 800.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[16 * 3024];
            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;

            filename = imageUri;     // Also call getFilename for add file in this directory
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            filename = "";
        }

        return filename;
    }

    public static long differenceSecon(String firstTime, String secondTime) {

        long seconds = 0;
        try {

            Date startDate = null;
            Date endDate = null;
            try {
                startDate = new SimpleDateFormat(determineDateFormat(firstTime)).parse(firstTime);
                endDate = new SimpleDateFormat(determineDateFormat(secondTime)).parse(secondTime);
            } catch (Exception e) {
                e.getMessage();
            }


            long diff = endDate.getTime() - startDate.getTime();
            seconds = diff / 1000;
            Log.e("nfjhfkfkf_goa chalo", String.valueOf(seconds));
            return seconds;
        }
        catch (Exception e){}

        return seconds;
    }

    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
    }};

    public static String determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null; // Unknown format.
    }

    public static String parseDate(String dateTime) {
        Date date = null;

        try {
            date = new SimpleDateFormat(determineDateFormat(dateTime)).parse(dateTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    public static String getDayTime(String dateTime, String formate) {
        Date date = null;

        try {
            date = new SimpleDateFormat(determineDateFormat(dateTime)).parse(dateTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(formate).format(date);
    }


    public static String parseDateToddMMyyyy(String time) {
        Date date = null;

        try {
            date = new SimpleDateFormat(determineDateFormat(time)).parse(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd MMM, yyyy").format(date);
    }

    private static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "/GPSIndiaImage");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + "IMG_" + System.currentTimeMillis() + ".jpg");
    }

    private static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static String calculateTime(long seconds) {
        String dateTime = "";
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        String hours = String.valueOf(TimeUnit.SECONDS.toHours(seconds) - (day * 24));
        String minute = String.valueOf(TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60));
        String second = String.valueOf(TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60));
        if (Integer.parseInt(hours) < 10) {
            hours = "0" + hours;
        }
        if (Integer.parseInt(minute) < 10) {
            minute = "0" + minute;
        }
        if (Integer.parseInt(second) < 10) {
            second = "0" + second;
        }

        dateTime = day + " Day(s), " + hours + " : " + minute + " : " + second;

        //dateTime=  day + " : " + hours + " : " + minute + " : " + second;
        return dateTime;
    }


    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        boolean check = false;
        int diffDays = 0;
        try {
            long diff = TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
            diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            return diffDays;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//    public static void showPopUp(final Context context, String titl, String msg, String imageUrl)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View customView = layoutInflater.inflate(R.layout.custom_dialog_layot, null);
//        TextView message = (TextView) customView.findViewById(R.id.message);
//        TextView title = (TextView) customView.findViewById(R.id.title);
//        title.setText(titl);
//        message.setText(msg);
//        TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
//        TextView cancel_btn = (TextView)customView.findViewById(R.id.cancel_btn);
//        RelativeLayout rl_seperator =(RelativeLayout)customView.findViewById(R.id.rl_seperator);
//        CircularImageView circular_img = (CircularImageView)customView.findViewById(R.id.circular_img);
//        builder.setView(customView);
//        AlertDialog alert = builder.create();
//        final AlertDialog finalAlert = alert;
//        finalAlert.setCancelable(false);
//        cancel_btn.setVisibility(View.GONE);
//        rl_seperator.setVisibility(View.GONE);
//
//
//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    finalAlert.dismiss();
//            }
//        });
//        alert.show();
//
////        if (!imageUrl.equals(""))
////        {
////            Glide.with(context).load(imageUrl).into(circular_img);
////        }
//    }

//    public static void callingNew(final Context mContext, final boolean check)
//    {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View customView = layoutInflater.inflate(R.layout.view_progress_dialog, null);
////        ImageView imageView = (ImageView) customView.findViewById(R.id.progress_bar);
//        builder.setView(customView);
//        AlertDialog alert = builder.create();
//        alert.setCancelable(false);
//        alert.create();
//        alert.dismiss();
//        if (check)
//        {
//            alert.show();
//        }
//    }
//    public static void startDialog(Context context, boolean start)
//    {
//        callingNew(context, true);
//    }
//    public static void distmisDialog(Context context, boolean dismis)
//    {
//        callingNew(context, false);
//    }

    public static double roundTwoDecimals(double d) {
//round off decimal to int
        Double dd = (Double) Math.floor(d);
        DecimalFormat twoDForm = new DecimalFormat("##");
        return Double.valueOf(twoDForm.format(dd));
    }

    //for show only 2 decimal
    public static double roundTwoDecimals1(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public static int dpToPx(int dp) {
        Resources r = Resources.getSystem();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String currentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
        String formattedDate = df.format(c.getTime());
        Log.e("diffreSghsdheconds", String.valueOf(formattedDate));
        return formattedDate.toUpperCase();
    }

    public static boolean getDurationTimeStamp(Context context) {
        boolean value = false;
        try {
            if (getPreference_int(context, Constants.ACCEXPIRE_DATE) > getCurrentTimeSeconds()) {
                value = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getCurrentTimeSeconds() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        String str = sdf.format(new Date());

        String hour = str;
        hour = hour.substring(0, hour.length() - 6);

        String minutes = str;
        minutes = minutes.substring(3);
        minutes = minutes.substring(0, minutes.length() - 3);
        String second = str;
        second = second.substring(6);

        return Integer.parseInt(hour) * 3600 + Integer.parseInt(minutes) * 60 + Integer.parseInt(second);
    }

//    public static void alert(final Context context, String msg) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
//        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
//        View customView = layoutInflater.inflate(R.layout.custom_dialog_layot, null);
//        TextView title = (TextView) customView.findViewById(R.id.title);
//        TextView message = (TextView) customView.findViewById(R.id.message);
//        TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
//        TextView cancel_btn = (TextView) customView.findViewById(R.id.cancel_btn);
//        RelativeLayout rl_seperator = (RelativeLayout) customView.findViewById(R.id.rl_seperator);
//        rl_seperator.setVisibility(View.GONE);
//        cancel_btn.setText(context.getResources().getString(R.string.off));
//        ok_btn.setText(context.getResources().getString(R.string.ok));
//        message.setVisibility(View.GONE);
//        title.setText(msg);
//        builder.setView(customView);
//        final AlertDialog alert = builder.create();
//        alert.setCancelable(false);
//        cancel_btn.setVisibility(View.GONE);
//
//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//        alert.show();
//
//    }
//
//    public static void addUserAlert(final Context context, String msg) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
//        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
//        View customView = layoutInflater.inflate(R.layout.custom_dialog_layot, null);
//        TextView title = (TextView) customView.findViewById(R.id.title);
//        TextView message = (TextView) customView.findViewById(R.id.message);
//        TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
//        TextView cancel_btn = (TextView) customView.findViewById(R.id.cancel_btn);
//        RelativeLayout rl_seperator = (RelativeLayout) customView.findViewById(R.id.rl_seperator);
//        rl_seperator.setVisibility(View.GONE);
//        cancel_btn.setText(context.getResources().getString(R.string.off));
//        ok_btn.setText(context.getResources().getString(R.string.ok));
//        message.setVisibility(View.GONE);
//        title.setText(msg);
//        builder.setView(customView);
//        final AlertDialog alert = builder.create();
//        alert.setCancelable(false);
//        cancel_btn.setVisibility(View.GONE);
//
//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//                ((Activity) context).finish();
//            }
//        });
//        alert.show();
//
//    }
//
//    public static void ExitAlert(final Context context, String msg) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
//        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
//        View customView = layoutInflater.inflate(R.layout.custom_dialog_layot, null);
//        TextView title = (TextView) customView.findViewById(R.id.title);
//        TextView message = (TextView) customView.findViewById(R.id.message);
//        TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
//        TextView cancel_btn = (TextView) customView.findViewById(R.id.cancel_btn);
//        RelativeLayout rl_seperator = (RelativeLayout) customView.findViewById(R.id.rl_seperator);
//        rl_seperator.setVisibility(View.VISIBLE);
//        cancel_btn.setText(context.getResources().getString(R.string.cancel));
//        ok_btn.setText(context.getResources().getString(R.string.ok));
//        message.setVisibility(View.GONE);
//        title.setText(msg);
//        builder.setView(customView);
//        final AlertDialog alert = builder.create();
//        alert.setCancelable(false);
//        cancel_btn.setVisibility(View.VISIBLE);
//
//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//                ((Activity) context).finish();
//            }
//        });
//
//        cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//        alert.show();
//
//    }
//
//    public static void successAlert(final Context context, final String msg, final Dashboard dashboard, final int viewPagerPosition) {
//
//        new AlertDialog.Builder(context)
//                .setMessage(msg)
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        context.startActivity(new Intent(context, VehicleManagementActivity.class)
//                                .putExtra("vehicleManagementData", dashboard)
//                                .putExtra("viewPagerPosition", String.valueOf(viewPagerPosition)));
//                        ((AppCompatActivity) context).finish();
//                    }
//                })
//                .show();
//    }

//    public static void dialog(final Context context, String message) {
//        AlertDialog alertDialog= new AlertDialog.Builder(context)
//                .setTitle(context.getResources().getString(R.string.kycmiracle))
//                .setCancelable(false)
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(context,DashboardActivity.class);
//                        context.startActivity(intent);
//                        ((Activity)context).finish();
//                    }
//                }).show();
//        alertDialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, Color.WHITE));
//
//    }

//    public static void Alerdialog(final Context context)
//    {
//        AlertDialog alertDialog= new AlertDialog.Builder(context)
//                .setTitle(context.getResources().getString(R.string.app_name))
//                .setCancelable(false)
//                .setMessage(context.getResources().getString(R.string.session_denied_login_again))
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("EXIT", true);
//                        context.startActivity(intent);
//                        ((Activity)context).finish();
//                    }
//                })
//                .show();
//        alertDialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, Color.WHITE));
//
//    }

    public static void setTint(Context activity, ImageView image, int color) {
        if (!(image instanceof ImageView))
            throw new InvalidParameterException();
        if (Build.VERSION.SDK_INT < 23) {
            ((ImageView) image).setColorFilter(activity.getResources().getColor(color));
        } else {
            ((ImageView) image).setColorFilter(activity.getApplicationContext().getColor(color));
        }
    }


    /**
     * Uses static final constants to detect if the device's platform version is
     * Gingerbread or later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is
     * Honeycomb or later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Uses static final constants to detect if the device's platform version is
     * Honeycomb MR1 or later.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Uses static final constants to detect idiaf the device's platform version is
     * ICS or later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * This Method is Checked that network is Available or Not If available
     * Result Will be True If not available Result Will be False
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

//    public static String getDevId(Context mContext) {
//        TelephonyManager telephonyManager = (TelephonyManager) mContext
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        return telephonyManager.getDeviceId();
//    }

    public static String getCountry(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Locale locale = new Locale("", String.valueOf(telephonyManager.getSimCountryIso()));
        return locale.getDisplayCountry();
    }

    public static String getCurrentMonthFirstDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(c.getTime());

    }


//    public static ApplicationDate isValidDate(String date) {
//        ApplicationDate result = new ApplicationDate();
///*
//        String customerDateFormat= getPreference(ApplicationActivity.getContext(), Constants.CUSTOMER_DATE);
//        String customerDateTimeFormat= getPreference(ApplicationActivity.getContext(), Constants.CUSTOMER_DATE_TIME);
//
//        if(!customerDateFormat.isEmpty()){
//            result.setCustomerDateFormat(customerDateFormat);
//        }
//        if(!customerDateTimeFormat.isEmpty()){
//            result.setCustomerDateTimeFormat(customerDateTimeFormat);
//        }
//*/
//
//        result.setValid(false);
//        try {
//            result.setDate(new SimpleDateFormat(result.getJsonDateFormat()).parse(date));
//            if (result.getDate().toString().contains("1970") ||  date.contains("0001")) {
//                result.setValid(false);
//            } else {
//                result.setValid(true);
//            }
//        } catch (Exception ex) {
//
//        } finally {
//            return result;
//        }
//    }

    public static String getParamsToUrl(String Url,
                                        LinkedHashMap<String, String> ParamsLinked) {
        // Using StringBuffer append for better performance.
        StringBuilder combinedParams = new StringBuilder();
        if (!ParamsLinked.isEmpty()) {

            for (Map.Entry<String, String> entry : ParamsLinked.entrySet()) {
                try {
                    combinedParams.append(combinedParams.length() > 1 ? "&" : "").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return Url + combinedParams.toString();
    }

    public static boolean isInternetReachable() {
        try {
            // make a URL to a known source
            URL url = new URL("http://www.google.com");

            // open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection) url
                    .openConnection();

            // trying to retrieve data from the source. If there
            // is no connection, this line will fail
            urlConnect.setConnectTimeout(5 * 1000);
            urlConnect.getContent();

        } catch (UnknownHostException e) {

            e.printStackTrace();
            return false;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static int getStatusBarHeight(Context Con) {
        int result = 0;
        int resourceId = Con.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = Con.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // Calculate ActionBar height

    public static int getActionBarBarHeight(Context Con) {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (Con.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
                true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, Con
                    .getResources().getDisplayMetrics());
        }

        return result;
    }

    // getHeight of NavigationBar bar
    public static int getNavigationBarHeight(Context Con) {
        int result = 0;

        Resources resources = Con.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;

    }

    public static Bitmap makeBitmap(String path, Context mContext, int width,
                                    int height) {
        int orient = 0;

        /*
         * Display dispDefault = ((WindowManager)
         * mContext.getSystemService(Context.WINDOW_SERVICE))
         * .getDefaultDisplay(); DisplayMetrics dispMetrics = new
         * DisplayMetrics(); dispDefault.getMetrics(dispMetrics);
         *
         * Point mPoint = CommonUtils.getDisplaySize(dispDefault);
         */
        Resources resource = mContext.getResources();
        // int screenWidth = resource.getDisplayMetrics().widthPixels;
        // int screenHeight = resource.getDisplayMetrics().heightPixels;

        try {
            ExifInterface ei = new ExifInterface(path);
            orient = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Old code for load bitmaps
        // BitmapFactory.Options opt = new BitmapFactory.Options();
        // opt.inSampleSize = 4;
        // Bitmap image = BitmapFactory.decodeFile(path, opt);

        Bitmap image = decodeSampledBitmapFromResource(path, resource, 1,
                width, height);

        Matrix matrix = new Matrix();
        switch (orient) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            default:
                matrix.postRotate(0);
        }
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
                image.getHeight(), matrix, true);
        matrix.reset();

        float scaleWidth = ((float) width) / image.getWidth();
        float scaleHeight = ((float) height) / image.getHeight();
        float scale = Math.min(scaleWidth, scaleHeight);
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(image, 0, 0, image.getWidth(),
                image.getHeight(), matrix, true);
    }

    // public static int calculateInSampleSize(BitmapFactory.Options options,
    // int reqWidth, int reqHeight) {
    // // Raw height and width of image
    // final int height = options.outHeight;
    // final int width = options.outWidth;
    // int inSampleSize = 1;
    //
    // if (height > reqHeight || width > reqWidth) {
    //
    // final int halfHeight = height / 2;
    // final int halfWidth = width / 2;
    //
    // // Calculate the largest inSampleSize value that is a power of 2 and
    // // keeps both
    // // height and width larger than the requested height and width.
    // while ((halfHeight / inSampleSize) > reqHeight
    // && (halfWidth / inSampleSize) > reqWidth) {
    // inSampleSize *= 2;
    // }
    // }
    //
    // return inSampleSize;
    // }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(Context context, Uri uri) {
        String type = "";
        if (uri != null) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.MediaColumns.MIME_TYPE}, null,
                    null, null);

            if (cursor != null && cursor.moveToNext()) {
                type = cursor.getString(0);
            }

            return type;
        }
        return type;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * this method is for getting Display Dimensions Weather this method is
     * Deprecated or not
     **/
    @SuppressLint("NewApi")
    public static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    public static String getDateDifference(Date thenDate) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(thenDate);

        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);


        if (diffMinutes < 60) {
            if (diffMinutes == 1)
                return diffMinutes + " minute ago";
            else
                return diffMinutes + " minutes ago";
        } else if (diffHours < 24) {
            if (diffHours == 1)
                return diffHours + " hour ago";
            else
                return diffHours + " hours ago";
        } else if (diffDays < 30) {
            if (diffDays == 1)
                return diffDays + " day ago";
            else
                return diffDays + " days ago";
        } else if ((diffDays / 30) >= 2 && (diffDays / 30) < 12) {
            return String.format("%d months ago", (diffDays / 30));
        } else if ((diffDays / 365) > 1) {
            return String.format("%d years ago", (diffDays / 365));
        } else {
            return "a long time ago..";
        }
    }

    public static boolean CalculateTimeDifferenceInTermsofMinute(String StartDate, String StopDate) {
        boolean check = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            Date oldDate = null;
            Date currentDate = null;
            try {
                oldDate = dateFormat.parse(StartDate);
                currentDate = dateFormat.parse(StopDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            System.out.println(oldDate);

            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            //     long minutes = seconds / 60;
            //   long hours = minutes / 60;

            Log.e("dirrdgaslgl", String.valueOf(seconds));

            if (seconds >= 0) {
                check = true;
            }

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return check;
    }

    /**
     * @param view EditText
     * @return true/false
     * @category EditText Validation
     */
    public static boolean isEditValid(EditText view) {
        boolean isValid = false;

        EditText mEditText = view;

        if (mEditText.getText().toString().trim().length() > 0) {
            isValid = true;
        }

        return isValid;
    }

    /*
     * isNameValid function is check wheather given text contains any numeric
     * value or special character if it contains numeric value or special
     * character it will return false
     */

    public static boolean isNameValid(String name) {
        boolean isValid = true;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!(Character.isSpaceChar(c) || Character.isLetter(c))) {
                isValid = false;
            }

        }
        return isValid;
    }

    /**
     * @param email as a String
     * @return true/false
     * @category Email Validation
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPasswordValid(String password) {
        boolean isValid = false;

        if (password.length() > 4) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidMobile(EditText phoneEditText) {
        boolean check;
        if (phoneEditText.getText().toString().trim().length() < 6 || phoneEditText.getText().toString().trim().length() > 13) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    public static boolean isValidMobNumber(String phoneNumber) {
        boolean check;
        if (phoneNumber.trim().length() == 10) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }


    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String MillisecondsToDate(long millis) {
        String returnTime;
        // SimpleDateFormat formatter = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df = new SimpleDateFormat("hh:mm a");
        // formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        returnTime = df.format(c.getTime());
        return returnTime;
    }

    public static void CopyRAWtoSDCard(Context mContext, int id, String path)
            throws IOException {
        InputStream in = mContext.getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Compare start Date is before start Date
     *
     * @param startDate
     * @param endDate
     * @return
     */

    public static boolean isDateBefore(String startDate, String endDate) {
        try {
            String myFormatString = "yyyy-MM-dd"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (startingDate.before(date1))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String MillisecondsToHeaderDate(long milliSeconds,
                                                  String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static String Base64Decoder(String base64string) {
        byte[] data = Base64.decode(base64string, Base64.DEFAULT);
        String text = "";
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text.trim();
    }

//    public static void displayNetworkAlert(final Context context, final boolean isFinish) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppDialogTheme);
//        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
//        View customView = layoutInflater.inflate(R.layout.custom_dialog_layot, null);
//        TextView title = (TextView) customView.findViewById(R.id.title);
//        TextView message = (TextView) customView.findViewById(R.id.message);
//        TextView ok_btn = (TextView) customView.findViewById(R.id.ok_btn);
//        TextView cancel_btn = (TextView) customView.findViewById(R.id.cancel_btn);
//        RelativeLayout rl_seperator = (RelativeLayout) customView.findViewById(R.id.rl_seperator);
//        rl_seperator.setVisibility(View.GONE);
//        cancel_btn.setText(context.getResources().getString(R.string.off));
//        ok_btn.setText(context.getResources().getString(R.string.ok));
//        message.setVisibility(View.VISIBLE);
//        title.setText(context.getResources().getString(R.string.network_error));
//        message.setText(context.getResources().getString(R.string.please_check_network));
//        builder.setView(customView);
//        final AlertDialog alert = builder.create();
//
//        alert.setCancelable(false);
//        cancel_btn.setVisibility(View.GONE);
//        ok_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//        if (!alert.isShowing()) {
//            alert.show();
//        }
//    }

    public static String getDate(String timestamp) {
        Date oneWayTripDate;
        String formatedDate = "";
        if (!TextUtils.isEmpty(timestamp)) {
            try {
                String time[] = timestamp.split(" ");
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                oneWayTripDate = input.parse(time[0]);
                formatedDate = output.format(oneWayTripDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return formatedDate;
    }

    public static int getExpireDays(String date) {
        SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd, yyyy");
        Date d = null;
        Date d1 = null;
        int diffInDays;

        Calendar cal = Calendar.getInstance();
        try {
            d = dfDate.parse(date);
            d1 = dfDate.parse(dfDate.format(cal.getTime()));// Returns
            // 15/10/2012
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));

        return diffInDays;

    }

    public static Bitmap getBitmapFromPath(String FilePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(FilePath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        if (imageWidth > imageHeight) {
            options.inSampleSize = calculateInSampleSize(options, 512, 256);// if
            // landscape
        } else {
            options.inSampleSize = calculateInSampleSize(options, 256, 512);// if
            // portrait
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(FilePath, options);
    }

    public static Bitmap getBitmapThumbnailsFromPath(String FilePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(FilePath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        if (imageWidth > imageHeight) {
            options.inSampleSize = calculateInSampleSize(options, 512, 256);// if
            // landscape
        } else {
            options.inSampleSize = calculateInSampleSize(options, 256, 512);// if
            // portrait
        }
        options.inJustDecodeBounds = false;
        // return BitmapFactory.decodeFile(FilePath, options);
        return Bitmap.createScaledBitmap(
                BitmapFactory.decodeFile(FilePath, options), 64, 64, false);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static String removeZeroFromDate(String date) {
//		Log.e("Original Date---", "" + date);
        String[] separated = date.split(" ");
        String s = separated[1];
        String remove = "";
        if (s.startsWith("0")) {
//			Log.e("Comp", "Comp---" + s.substring(1, s.length() - 1));
            remove = s.substring(1, s.length() - 1);
//			Log.e("Seprated[0]", "" + separated[0]);
//			Log.e("s", "" + remove);
//			Log.e("Final String---", "" + separated[0] + " " + remove + ","
//					+ " " + separated[2]);
            return separated[0] + " " + remove + "," + " " + separated[2];
        } else {
//			Log.e("Else", "Else");
//			Log.e("Not Chaged Date---", "" + date);
            return date;
        }

    }

    public static void AlertDialogDefault(Context mContext, String Title,
                                          String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
/*
    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }*/



    public static boolean checkAccessToken(Context context, String time) {
        boolean check = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date oldDate = null;
            try {
                oldDate = dateFormat.parse(time);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            System.out.println(oldDate);

            Date currentDate = new Date();

            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            //     long minutes = seconds / 60;
            //   long hours = minutes / 60;

            Log.e("onLogoutResponseBody", String.valueOf(getPreference_int(context, Constants.ACCEXPIRE_DATE)) + " " + String.valueOf(seconds));

            if (getPreference_int(context, Constants.ACCEXPIRE_DATE) > (int) seconds + 5) {
                check = true;
            }
            if ((int)seconds+5<0)
            {
                check = false;
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return check;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hide_keyboard(@NonNull View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public static String encodeString(String string) {
        String s = "";
        if (!string.equals("")) {
            //encoding  byte array into base 64
            byte[] encoded = Base64.encode(string.getBytes(), Base64.DEFAULT);
            s = new String(encoded);
        }
        return s.trim();
    }

    public static String twoTimeDifference(String startTime, String endTime) {
        int min = 0;
        String returnTime = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date date1 = simpleDateFormat.parse(startTime);
            Date date2 = simpleDateFormat.parse(endTime);

            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);

            if (hours > 0) {
                returnTime = "" + hours + " hrs";
            } else if (min > 0) {
                returnTime = "" + min + " mins";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    public static long dateDifferent(String dateStart, String dateStop) {
//		String dateStart = "01/14/2012 09:29:58";
//		String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        long diffDays = 0;
        if (!dateStart.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                diffDays = diff / (24 * 60 * 60 * 1000);

                System.out.print(diffDays + " days, ");
                System.out.print(diffHours + " hours, ");
                System.out.print(diffMinutes + " minutes, ");
                System.out.print(diffSeconds + " seconds.");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return diffDays;
    }


//	public static String getAddress(LatLng latLng, Context context)
//	{
//		Geocoder geocoder = new Geocoder(context);
//		double latitude = latLng.latitude;
//		double longitude = latLng.longitude;
//
//		String address = "";
//
//		try
//		{
//			Log.i("Address Info","Address based opn geocoder");
//			DrawerList<Address> addresses = geocoder.getFromLocation(latitude,
//					longitude, 1);
//
//			if (addresses != null && !addresses.isEmpty())
//			{
//				Address returnedAddress = addresses.get(0);
//				StringBuilder strReturnedAddress = new StringBuilder();
//				int addressLineIndex = returnedAddress.getMaxAddressLineIndex();
//
//				int addressLinesToShow = 2;
//				//              To get address in limited lines
//				if (addressLineIndex < 2)
//				{
//					addressLinesToShow = addressLineIndex;
//				}
//				for (int p = 0; p < addressLinesToShow; p++)
//				{
//					strReturnedAddress
//							.append(returnedAddress.getAddressLine(p)).append(
//							"\n");
//				}
//				address = strReturnedAddress.toString();
//			}
//			else
//			{
//				address = "Address not available";
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			address = "Address not available";
//			Log.e("Address not found","Unable to get Address in info window");
//		}
//		return address;
//	}

//	public GeoPoint getLocationFromAddress(String strAddress,Context mContext)
//	{
//
//		Geocoder coder = new Geocoder(mContext);
//		DrawerList<Address> address;
//		GeoPoint p1 = null;
//
//		try {
//			address = coder.getFromLocationName(strAddress, 5);
//			if (address == null) {
//				return null;
//			}
//			Address location = address.get(0);
//			location.getLatitude();
//			location.getLongitude();
//
//			p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
//					(int) (location.getLongitude() * 1E6));
//
//
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return p1;
//	}


    public static void getUniqueDeviceID(Context mContext) {
        try {
            String deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.e("deviceId---", "" + deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//	public static String getCityName(Context mContext)
//	{
//		String cityName="";
//		try
//		{
//			//if(CustomerHomeActivity.mAutoSearchLocation.getText().length() > 0 && !CustomerHomeActivity.mAutoSearchLocation.getText().toString().equals(mContext.getResources().getString(R.string.no_address_found))) {
//				Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
//				DrawerList<Address> addresses = gcd.getFromLocation(Double.parseDouble(Global.latitude), Double.parseDouble(Global.longitude), 1);
//				if (addresses.size() > 0) {
//					System.out.println(addresses.get(0).getLocality());
//					cityName = addresses.get(0).getLocality();
//				}
//			//}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return cityName;
//	}

    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 3) {
            return true;
        }
        return false;
    }

    public static boolean isValidUserName(String username) {
        if (username != null && username.length() >= 3) {
            return true;
        }
        return false;
    }

//	public static boolean idValidUsername(String username){
//		Pattern pattern;
//		Matcher matcher;
//		String USERNAME_PATTERN = "^[a-z0-9A-Z]{3,15}$";
//		pattern = Pattern.compile(USERNAME_PATTERN);
//		matcher = pattern.matcher(username);
//		return matcher.matches();
//
//	}

//	public static boolean idValidUsername(String s)
//	{
//		Pattern letter = Pattern.compile("[a-zA-z]");
//		Pattern digit = Pattern.compile("[0-9]");
//		Matcher hasLetter = letter.matcher(s);
//		Matcher hasDigit = digit.matcher(s);
//
//		if(hasLetter.find() && hasDigit.find())
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}

    public static String getDateFormate(String timestamp) {
        String date = "";
        if (!timestamp.equals("")) {
            try {
                String time[] = timestamp.split(" ");
                date = time[0];
//				if(getDate(time[0]).equals(getCurrentDate()))
//				{
//					date=getCurrentTime(timestamp);
//				}
//				else {
//					date=getDate(time[0])+" "+getCurrentTime(timestamp);
//				}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    private static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }

    private static String getCurrentTime(String dateString) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            try {
                parsed = sourceFormat.parse(dateString);
            } catch (java.text.ParseException e) {

                e.printStackTrace();
                return "";
            }
        } catch (ParseException e1) {

            e1.printStackTrace();
            return "";
        }

        TimeZone tz = TimeZone.getDefault();
        SimpleDateFormat destFormat = new SimpleDateFormat("HH:mm");
        destFormat.setTimeZone(tz);
        String result = destFormat.format(parsed);
        return result;
    }

    public static boolean checkNumber(String s) {
        if (s.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }
    }

    public static String getRelativeDate(String date) {

        Calendar future = Calendar.getInstance();
        String relativeDate = "";

//		Log.e("days---",""+getDateDifference(new Date(date)));

        //19 May 2015 10:13:28
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yyyy HH:MM:SS");
        String inputString1 = "23 01 1997";
        String inputString2 = "27 04 1997";

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long days = getDateDiff(future.getTime(), Calendar.getInstance().getTime(), TimeUnit.DAYS);

//		Log.e("days---",""+date.substring(5));

        if (days < 7) {
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(future.getTimeInMillis(), System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL);

//      Timber.d("relativeTime - " + relativeTime);

            if (relativeTime.toString().equals("0 minutes ago")
                    || relativeTime.toString().equals("in 0 minutes")) {
                relativeDate = "Just now";
            } else if (relativeTime.toString().contains("hr. ")) {
                if (relativeTime.toString().equals("1 hr. ago")) {
                    relativeDate = "1 hour ago";
                } else {
                    relativeDate = relativeTime.toString().replace("hr. ", "hours ");
                }
            } else {
                relativeDate = relativeTime.toString();
            }
        } else if (days >= 7 && days < 14) {
            relativeDate = "A week ago";
        } else if (days >= 14 && days < 21) {
            relativeDate = "2 weeks ago";
        } else if (days >= 21 && days < 28) {
            relativeDate = "3 weeks ago";
        } else if ((days / 30) == 1) {
            relativeDate = "1 month ago";
        } else if ((days / 30) >= 2 && (days / 30) < 12) {
            relativeDate = String.format("%d months ago", (days / 30));
        } else if ((days / 365) > 1) {
            relativeDate = String.format("%d years ago", (days / 365));
        }

//        Timber.d("getRelativeDate() : days - " + days);
//        Timber.d("getRelativeDate() : relativeDate - " + relativeDate);

        return relativeDate;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void showToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

//	public static void showSnackBar(View view,String message)
//	{
//		Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
//	}


    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    public static void getListViewSize(GridView mGridView) {
        ListAdapter myListAdapter = mGridView.getAdapter();
        if (myListAdapter == null) {
            // do nothing return null
            return;
        }
        // set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, mGridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // setting listview item in adapter
        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
        params.height = totalHeight - (myListAdapter.getCount() - 1);//(totalHeight+ (1 * (myListAdapter.getCount() - 1)))/2;
        mGridView.setLayoutParams(params);
        // print height of adapter on log
    }

    public static void cancelToastMessage(final Toast toast) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

    public static void customToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getDeviceType(Context context) {
        if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return "Tablet";
        } else {
            return "Mobile";
        }

    }

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            default:
                return "UNKNOWN";
        }
    }


    public static String convertTimestampToDate(String timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String localTime = sdf.format(new Date(Long.parseLong(timeStamp) * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("DATE : ", date.toString());
        return "" + sdf.format(date);
    }

    public static String convertDateToTimestamp(String date) {

        String timestamp[] = date.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Integer.parseInt(timestamp[0]), Integer.parseInt(timestamp[1]) - 1, Integer.parseInt(timestamp[2]));
        return String.valueOf(calendar.getTimeInMillis() / 1000);
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static void refreshGallery(Context context, String filePath) {
        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }


    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    public static class ImageUtils {
        private static final float maxHeight = 1280.0f;
        private static final float maxWidth = 1280.0f;

        public static byte[] compressImage(String imagePath) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = ImageUtils.calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                if (actualHeight > 0 && actualWidth > 0) {
                    scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
                }

            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
            return out.toByteArray();
        }

        public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }
    }


//    protected void getLocation(Context context)
//    {
//        LocationManager lm = (LocationManager)context.getSystemService(LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch(Exception ex) {}
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch(Exception ex) {}
//        if (!network_enabled && !gps_enabled) {
//
//            final Context mContext = context;
//
//            new androidx.appcompat.app.AlertDialog.Builder(context)
//                    .setMessage("GPS Not Enabled")
//                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                            ((AppCompatActivity)mContext).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_FINE_LOCATION);
//                        }
//                    })
//                    .setNegativeButton("Cancel",null)
//                    .show();
//        }
//        else
//        {
//            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//            Criteria criteria = new Criteria();
//            String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
//            } else {
//                Location location = getLastKnownLocation();
//                if (location != null) {
//
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//                    Log.e("GPSLocation", "latitude:" + latitude + " longitude:" + longitude);
//
//
//                    Geocoder geocoder;
//                    List<Address> addresses;
//                    geocoder = new Geocoder(context, Locale.getDefault());
//
//                    try {
//                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                        String state = addresses.get(0).getAdminArea();
//                        Log.e("GPSLocation", state);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
//                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, context);
//                }
//            }
//        }
//    }


}