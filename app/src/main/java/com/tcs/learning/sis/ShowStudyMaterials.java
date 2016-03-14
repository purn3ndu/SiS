package com.tcs.learning.sis;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tcs.learning.sis.Network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShowStudyMaterials extends AppCompatActivity {

    public String courseTime;
    public String keywords;
    public String level;
    public static int flag = 0;
    private static final String TAG = "";

    //RecyclerView, ArrayList and Adapter for showing text links.
    private RecyclerView listStudyLinks;
    private ArrayList<TextLinks> listTextLinks=new ArrayList<>();
    private AdapterShowResults adapterShowResults;


    //RecyclerView, ArrayList and Adapter for showing videos
    private RecyclerView listVidStudyLinks;
    private ArrayList<VideoLinks> listVideoLinks=new ArrayList<>();
    private AdapterShowVids adapterShowVids;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private static final String URL_SIS_VIDEO_API = "http://sis-video.appspot.com/?q=";
    private static final String DEVELOPER_KEY="AIzaSyCfpTa4xpRp2jpEvR0CdYPC1uqSwTAfaJE";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_study_materials);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listStudyLinks= (RecyclerView) findViewById(R.id.listStudyLinks);
        listStudyLinks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapterShowResults=new AdapterShowResults(getApplicationContext());
        listStudyLinks.setAdapter(adapterShowResults);


        listVidStudyLinks= (RecyclerView) findViewById(R.id.listVidStudyLinks);
        listVidStudyLinks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapterShowVids=new AdapterShowVids(getApplicationContext());
        listVidStudyLinks.setAdapter(adapterShowVids);

        /*
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        */


    }

    @Override
    public void onBackPressed()
    {
            super.onBackPressed();
            flag=0;
            //return;
            //android.os.Process.killProcess(android.os.Process.myPid());
            return;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_study_materials, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==android.R.id.home){

            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (flag == 0) {
            Intent intent = getIntent();
            Bundle extra;
            if ((extra = intent.getExtras()) != null) {
                keywords = extra.getString(Welcome.ex_keywords);
                courseTime = extra.getString(Welcome.ex_time);
                level = extra.getString(Welcome.ex_level);
                Log.d("temp", "Intent Received: " + courseTime + " " + keywords + " " + level);

                flag = 1;

                makeApiCall2();

                makeApiCall1();




            }
        } else {
            Log.d("temp", "Intent extra is empty");
        }
    }

    private void makeApiCall1() {


        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("keywords", keywords);
        params.put("time", courseTime);
        params.put("level", level);

        client.post("http://zippy-vigil-797.appspot.com", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String title;
                String time1;

                int errorcheck1;
                String urlreceived;
                String thumbnail = null; //To show a preview of the page. ***to be worked on.


                if (statusCode == HttpURLConnection.HTTP_OK) {
                    if (responseBody != null) {
                        String response = new String(responseBody);
                        Log.d(TAG, "response: " + response);
                        //close socket after getting response.


                        int count = 1;
                        try {
                            JSONObject json = new JSONObject(response);

                            errorcheck1 = (int) json.get("errorcheck");
                            if (errorcheck1 == 0) {
                                JSONArray userdetails = json.getJSONArray("text");
                                for (int i = 0; i < userdetails.length(); i++) {
                                    JSONObject user = userdetails.getJSONObject(i);
                                    urlreceived = user.getString("url");
                                    title = user.getString("title");
                                    time1 = user.getString("time");
                                    // time2 = Integer.parseInt(time1);


                                    int timetest = Integer.parseInt(time1);

                                    if (timetest == 0) {
                                        timetest = 1;
                                    }

                                    time1 = String.valueOf(timetest);

                                    Log.d("THE title RECEIVED IS :", title);
                                    Log.d("THE URL RECEIVED IS : ", urlreceived);
                                    Log.d("THE time RECEIVED IS :", time1);


                                    TextLinks textLinks = new TextLinks();
                                    textLinks.setTitle(title);
                                    textLinks.setTime(time1);
                                    textLinks.setUrlreceived(urlreceived);

                                    listTextLinks.add(textLinks);

                                    //Toast.makeText(getApplicationContext(),textLinks.getTime(),Toast.LENGTH_LONG).show();

                                    adapterShowResults.setListTextLinks(listTextLinks);
                                }


                            } else {
                                if (errorcheck1 == 1) {
                                    thissathichafunction();

                                } else {
                                    populating();
                                }
                            }

                        } catch (Exception e) {
                            Log.d("TAG", "Error is : ", e);
                        }


                    } else {
                        Log.d(TAG, "response: null");
                    }
                } else {
                    Log.d(TAG, "response status: " + statusCode);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public static String getRequestUrl(String keywords){
        //URLifying the Keywords
        return URL_SIS_VIDEO_API+keywords.replace(' ', '+');
    }

    private void makeApiCall2() {

        volleySingleton=VolleySingleton.getsInstance();
        requestQueue=volleySingleton.getRequestQueue();
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, getRequestUrl(keywords), (String)null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
              //  Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                parseJSONResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void parseJSONResponse(JSONArray response){
        if(response==null || response.length()==0)
            return;

        try {
            StringBuilder data=new StringBuilder();
            //JSONArray arrayItems = response.getJSONArray("");
            for(int i=0; i<response.length();i++){
                JSONObject currentItem=response.getJSONObject(i);

                String title = currentItem.getString("title");
                String id =currentItem.getString("id");
                String duration=currentItem.getString("duration");

                String thumbUrl="https://i.ytimg.com/vi/"+id+"/default.jpg";

                String videoUrl="https://www.youtube.com/watch?v=" + id;

                Log.d("THE title RECEIVED IS :", title);
                Log.d("THE time RECEIVED IS :", duration);
                Log.d("THE id RECEIVED IS :", id);

                data.append(id + " ");

                VideoLinks videoLinks=new VideoLinks();

                videoLinks.setId(id);
                videoLinks.setTitle(title);
                videoLinks.setUrlreceived(videoUrl);
                videoLinks.setTime(duration);
                videoLinks.setThumbnail(thumbUrl);

                Log.d("Working 1", title);


                Log.d("Working 10", duration);
                data.append(duration + "\n");



                listVideoLinks.add(videoLinks);

                //Toast.makeText(getApplicationContext(),listVideoLinks.toString(),Toast.LENGTH_LONG).show();

                adapterShowVids.setListVideoLinks(listVideoLinks);

            }
            //Toast.makeText(getApplicationContext(),data.toString(),Toast.LENGTH_LONG).show();

        }
        catch (JSONException e){

        }

    }


        /*

        final AsyncHttpClient client1 = new AsyncHttpClient();
        RequestParams params1 = new RequestParams();

        params1.put("keywords", keywords);
        params1.put("time", courseTime);
        params1.put("level", level);

        client1.post("http://short-is-sweet.appspot.com", params1, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String thumbnail;
                        String videotime1;
                        String videotitle;
                        String videourl;

                        if (statusCode == HttpURLConnection.HTTP_OK) {
                            if (responseBody != null) {
                                String response1 = new String(responseBody);
                                Log.d(TAG, "response: " + response1);
                                //close socket after getting response.


                                int count = 1;
                                try {
                                    JSONObject json1 = new JSONObject(response1);
                                    JSONArray userdetails = json1.getJSONArray("video");

                                    for (int i = 0; i < userdetails.length(); i++) {
                                        JSONObject user = userdetails.getJSONObject(i);
                                        videourl = user.getString("url");
                                        videotitle = user.getString("title");
                                        videotime1 = user.getString("time");
                                        thumbnail= user.getString("thumbnail");
                                        Log.d("THE VIDEO URL IS : ", videourl);
                                        Log.d("THE VIDEO thumb IS : ", thumbnail);


                                        VideoLinks videoLinks=new VideoLinks();
                                        videoLinks.setTitle(videotitle);
                                        videoLinks.setUrlreceived(videourl);
                                        videoLinks.setTime(videotime1);
                                        videoLinks.setThumbnail(thumbnail);

                                        listVideoLinks.add(videoLinks);

                                        //Toast.makeText(getApplicationContext(),listVideoLinks.toString(),Toast.LENGTH_LONG).show();

                                        adapterShowVids.setListVideoLinks(listVideoLinks);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                } catch (Exception e) {
                                    Log.d("TAG", "Error in video is : ", e);
                                }


                            } else {
                                Log.d(TAG, "response: null");
                            }
                        } else {
                            Log.d(TAG, "response status: " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        error.printStackTrace();
                        //populating();
                    }
                }
        );  */



    private void populating() {

        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setMessage("Its not you,it's us !! Kindly click on the home button on the bottom of your screen and try again !");
        dlgAlert.setTitle("OOPS!!");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

    }





    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void thissathichafunction() {

        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setMessage("You seem to have entered an invalid query. Please press ok and the home icon on the bottom right of your screen try again !");
        dlgAlert.setTitle("INVALID QUERY");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

    }




}