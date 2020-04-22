package com.deepak.algofocustest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //variable declaration

    TextView id,name,email,location1,location2,location3,location4,location5;
    ImageView imageView;
    Button signout,btnlocation;
    GoogleSignInClient mGoogleSignInClient;

    //location services
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //location variable declaration
        btnlocation=(Button)findViewById(R.id.btnlocatoin);
        location1=(TextView)findViewById(R.id.location1);
        location2=(TextView)findViewById(R.id.location2);
        location3=(TextView)findViewById(R.id.location3);
        location4=(TextView)findViewById(R.id.location4);
        location5=(TextView)findViewById(R.id.location5);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {
                    getlocation();

                }
                else
                    //when permission not granted
                {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }



            }
        });


        //google sign option
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        id=(TextView)findViewById(R.id.id);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        imageView=(ImageView)findViewById(R.id.imageview);

        signout=(Button)findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.signout:
                        signOut();
                        break;
                    // ...
                }
            }
        });

        //save data in string for further use
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //set data in its containers
            id.setText(personId);
            name.setText(personName);
            email.setText(personEmail);
            //glide library  for image
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }

    }
    //method for signout
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"signout succesfully",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    //method to get location
    private void getlocation()
    {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if (location!=null)
                {

                    try {
                        Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());

                        //initialize address list
                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),
                                1);

                        //set lattitude on location
                        location1.setText(Html.fromHtml("<font color='#000'><b>Latitude:</b><br></font>"
                                +addresses.get(0).getLatitude()));
                        //set longitude on location
                        location2.setText(Html.fromHtml("<font color='#000'><b>Longitude:</b><br></font>"
                                +addresses.get(0).getLongitude()));
                        //set countryname on location
                        location3.setText(Html.fromHtml("<font color='#000'><b>Country Name:</b><br></font>"
                                +addresses.get(0).getCountryName()));
                        //set locale on location
                        location4.setText(Html.fromHtml("<font color='#000'><b>Locality:</b><br></font>"
                                +addresses.get(0).getLocality()));
                        //set Address line of location
                        location5.setText(Html.fromHtml("<font color='#000'><b>AddressLine:</b><br></font>"
                                +addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

