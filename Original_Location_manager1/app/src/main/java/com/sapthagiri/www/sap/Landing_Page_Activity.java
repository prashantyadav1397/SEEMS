package com.sapthagiri.www.sap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;
import com.sapthagiri.www.sap.activity.SampleActivity;
import com.sapthagiri.www.sap.fragments.Student_Info;
import com.sapthagiri.www.sap.fragments.Student_Logout;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.sapthagiri.www.sap.fragments.Student_Login_Activity.mStudentProduct;

public class Landing_Page_Activity extends AppCompatActivity implements Fragment_Drawer.OnDrawerListener{

    Fragment_Drawer drawerFragment;
    ImageView click;
    String url = "http://192.168.10.41/sas/API/location1.php";
    String url1 = "http://192.168.10.41/sas/API/location2.php";
    TextView uname;
    public double latitude,longitude;
    String v1,v2,s1,s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_landing_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        setTitle("");
        click=(ImageView) findViewById(R.id.checkin);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        uname = (TextView) findViewById(R.id.username);
       // checkout=(TextView) findViewById(R.id.checkout);
       uname.setText(mStudentProduct.getUname());
       // checkout.setText(mStudentProduct.getUname());
        drawerFragment = (Fragment_Drawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setOnDrawerListener(this);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Landing_Page_Activity.this, MapsActivity.class);
                startActivity(i);
            }}
        );
        s1 = uname.getText().toString();
        s2 =uname.getText().toString();
        getLocation();

    }
    protected void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            v1 = String.valueOf(latitude);
            v2 = String.valueOf(longitude);

            SharedPreferences location_preferences = getSharedPreferences("lat_lang", 0);
            SharedPreferences.Editor edt = location_preferences.edit();
            edt.putString("txt1",s2);
            edt.putString("txt0",s1);
            edt.putString("latitude", v1);
            edt.putString("longitude", v2);
            edt.apply();
            //Toast.makeText(this, v1, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, v2, Toast.LENGTH_SHORT).show();
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JSONObject jsonBody = new JSONObject( );
                jsonBody.put("txt1",s2);
                jsonBody.put("txt0",s1);
                jsonBody.put("latitude", latitude);
                jsonBody.put("longitude", longitude);
                final String mRequestBody = jsonBody.toString( );

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>( ) {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("message");
                            Toast.makeText(Landing_Page_Activity.this, result, Toast.LENGTH_SHORT).show( );

                        } catch (JSONException e) {
                            e.printStackTrace( );
                        }

                    }
                }, new Response.ErrorListener( ) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString( ));
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Nullable
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }
                };
                requestQueue.add(stringRequest);
            } catch (Exception e) {

            }


        }

    }
    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onDrawerItemClick(String menu, int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Student_Info.newInstance();
                break;

            default:
                fragment =Student_Logout.newInstance();
                break;
        }
        switchFragment(fragment);
    }

}
