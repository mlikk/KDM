package com.example.weatherapp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button getlatlon,getweather;
	TextView lat,lon,temp2;
	EditText addr1,addr2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addr1 = (EditText) findViewById(R.id.editText1);
        addr2 = (EditText) findViewById(R.id.editText2);
        getlatlon = (Button) findViewById(R.id.button1);
        lat = (TextView) findViewById(R.id.textView1);
        lon = (TextView) findViewById(R.id.textView2);
        temp2=(TextView) findViewById(R.id.textView4);
        getweather = (Button) findViewById(R.id.button2);
        
        getlatlon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String add = addr1.getText()+"";
				add = add + "" + addr2.getText();
				
				GetLatLong gll = new GetLatLong();
				gll.execute(new String[]{add});
				
			}
		
		});
        
        getweather.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String latln = lat.getText()+"";
				
				latln = latln + " " + lon.getText();
				GetWeather gw = new GetWeather();
				gw.execute(new String[]{latln});
				
			}
        	
        });
    }

    class GetLatLong extends AsyncTask<String,Void,JSONObject>{

		@Override
		protected JSONObject doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			try{
				String address = arg0[0];
				address = address.replaceAll(" ", "%20");
				HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=false");
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;
				sb = new StringBuilder();
				
				response = client.execute(httppost);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while((b = stream.read())!=-1){
					sb.append((char)b);
				}
			}catch(ClientProtocolException e){
			} catch(IOException e){	
			}
			
			JSONObject jsonObject = new JSONObject();
			try{
				jsonObject = new JSONObject(sb.toString());
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			return jsonObject;
		}
    	
		protected void onPostExecute(JSONObject jsonObject){
			super.onPostExecute(jsonObject);
			
			try{
				//String location = "";
				double longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lng");
				double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lat");
				
				MainActivity.this.lat.setText(""+latitude);
				MainActivity.this.lon.setText(""+longitude);
			}catch(JSONException e){
				e.printStackTrace();
			}
			
		}
		
    }
    class GetWeather extends AsyncTask<String,Void,JSONObject>{

		@Override
		protected JSONObject doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			StringBuilder sb1 = new StringBuilder();
			try{
				String lat = arg0[0].split(" ")[0];
				String longg = arg0[0].split(" ")[1];
				lat = lat.replaceAll(" ", "%20");
				longg = longg.replaceAll(" ", "%20");
				HttpPost httppost1 = new HttpPost("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+longg+"&sensor=false");
				HttpClient client1 = new DefaultHttpClient();
				HttpResponse response1;
				sb1 = new StringBuilder();
				
				response1 = client1.execute(httppost1);
				HttpEntity entity1 = response1.getEntity();
				InputStream stream1 = entity1.getContent();
				int b1;
				while((b1 = stream1.read())!=-1){
					sb1.append((char)b1);
				}
			}catch(ClientProtocolException e){
			} catch(IOException e){	
			}
			
			JSONObject jsonObject = new JSONObject();
			try{
				jsonObject = new JSONObject(sb1.toString());
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			return jsonObject;
		}
		protected void onPostExecute(JSONObject jsonObject){
			super.onPostExecute(jsonObject);
			
			try{
				//String location = "";
				
				double temp = jsonObject.getJSONObject("main").getDouble("temp");
				
				
				MainActivity.this.temp2.setText(""+temp);
				
			}catch(JSONException e){
				e.printStackTrace();
			}
			
		}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
