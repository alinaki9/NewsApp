package com.example.newsapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class DownloadService extends Service {

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    public int flag;
    private String[] url_data_string = new String[3];
    MyAsyncTasks myAsyncTasks;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            //Check for Internet
            if (!isInternetAvailable()) {
                Toast.makeText(getBaseContext(), "Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {

                myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();

            }
        }
    }

    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
               Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();
        flag = 0;

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        flag = 1;
        Toast.makeText(this, "Service Stopped!", Toast.LENGTH_SHORT).show();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");

            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    public class MyAsyncTasks extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();
            JSONObject json = null;



            while (true) {

                String[] files = getBaseContext().fileList();
                int filesSize = files.length;
                int most_recent_number;

                if (filesSize == 1){

                    most_recent_number = 0;
                }
                else{
                    most_recent_number = filesSize/3 ;
                }

                String url = "https://petwear.in/mc2022/news/news_" + Integer.toString(most_recent_number) + ".json";


                try {
                    json = sh.readJsonFromUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (json == null){

                    break;
                }


                String jsonStr = json.toString();
                Log.i("lifecycle", "Response from url: " + jsonStr);
                JSONObject reader = null;
                try {
                    reader = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String title = null;
                try {
                    title = reader.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String body = null;
                try {
                    body = reader.getString("body");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String image_url = null;
                try {
                    image_url = reader.getString("image-url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SaveToInternal(title, "title_" + Integer.toString(most_recent_number) );
                SaveToInternal(body, "body_" + Integer.toString(most_recent_number));
                SaveToInternal(image_url, "image-url_" + Integer.toString(most_recent_number));


                Log.i("lifecycle", "Image URL: " + GetFromInternal("image-url_" + Integer.toString(most_recent_number) ));
                Log.i("lifecycle", "Filelength: " + files.length);

                SharedPreferences prefs = getSharedPreferences("YO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("title",title);
                editor.putString("body", body);
                editor.putString("image-url", image_url);
                editor.apply();

                Intent local = new Intent();

                local.setAction("com.hello.action");

                getBaseContext().sendBroadcast(local);



                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (flag == 1){
                    break;
                }

            }
            //clearMyFiles();

            SharedPreferences prefs = getSharedPreferences("YO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("title","1");
            editor.apply();

            if (flag == 1){
                stopSelf();
                return null;
            }
            Looper.prepare();
            Toast.makeText(getApplicationContext(), "Data not Available!", Toast.LENGTH_SHORT).show();
            stopSelf();
            return null;
        }

        public void clearMyFiles() {
            File[] files = getBaseContext().getFilesDir().listFiles();
            if(files != null)
                for(File file : files) {
                    file.delete();
                }
        }

        public void SaveToInternal(String data, String filename){

            try {

                FileOutputStream fOut = openFileOutput(filename,MODE_PRIVATE);
                fOut.write(data.getBytes());
                fOut.close();

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        public String GetFromInternal(String filename){

            String temp= "";
            try {

                FileInputStream fin = openFileInput(filename);
                int c;

                while( (c = fin.read()) != -1){
                    temp = temp + Character.toString((char)c);
                }

            }
            catch(Exception e){
            }

            return temp;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experience
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

}
