package com.shapira.collectorscanner.utility;

import static androidx.core.content.FileProvider.getUriForFile;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.shapira.collectorscanner.BuildConfig;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.shapira.collectorscanner.BuildConfig;
public class UpdateApp extends AsyncTask<String,Void,Void> {
    private Context context;
    private Activity activity;
    public void setContext(Activity activityf,Context contextf,DownloadUpdateInterface downloadUpdateInterface){
        context = contextf;
        activity = activityf;
        mDownloadUpdateInterface = downloadUpdateInterface;
    }
    private DownloadUpdateInterface mDownloadUpdateInterface;
    @Override
    protected Void doInBackground(String... arg0) {
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
//            c.setDoOutput(true);
//            c.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            c.setRequestProperty("Accept","*/*");
            c.setChunkedStreamingMode(0);
            c.connect();

//            String PATH = "/mnt/sdcard/";

            String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            File file = new File(PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
//            file.mkdirs();
            File outputFile = new File(file, "app-debug.apk");
            if(outputFile.exists()){
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("UpdateAPP", "http response code is " + c.getResponseCode());
                return null;
            }
            InputStream is = c.getInputStream();
            InputStream error = c.getErrorStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
                Log.d("UpdateAPP", "buffer " + len1);
                mDownloadUpdateInterface.onDownloadProgress(len1);
            }
            fos.close();
            is.close();

//            Intent intent = new Intent(Intent.ACTION_VIEW);
            File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file1 = new File (downloads + "//app-debug.apk");//downloads.listFiles()[0];
            Uri contentUri1 = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file1);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    mDownloadUpdateInterface.onDownloadComplete();
                    Toast.makeText(context, "הורדת הקובץ הושלמה", Toast.LENGTH_LONG).show();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(!context.getPackageManager().canRequestPackageInstalls()){
                    activity.startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                            .setData(Uri.parse(String.format("package:%s", context.getApplicationContext().getPackageName()))), 1);
                }
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            installAPK();

        } catch (Exception e) {
            mDownloadUpdateInterface.onUpdateFailed();
            Log.e("UpdateAPP", "Update error! " + e.getMessage());
        }
        return null;
    }
    void installAPK(){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, "מנסה להתקין אפליקציה", Toast.LENGTH_LONG).show();
            }
        });
        String PATH =    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "//app-debug.apk";
        File file = new File(PATH);
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(context.getApplicationContext(), new File(PATH)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
                mDownloadUpdateInterface.onUpdateFailed();
            }
        }else{
            Toast.makeText(context, "מתקין אפליקציה", Toast.LENGTH_LONG).show();
            //Toast.makeText(context.getApplicationContext(),"installing",Toast.LENGTH_LONG).show();
        }
    }
    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
    public interface DownloadUpdateInterface {
        void onDownloadProgress(int buffer);
        void onDownloadComplete();
        void onUpdateFailed();
    }
}