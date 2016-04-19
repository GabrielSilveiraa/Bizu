package com.example.gabrielsilveira.bizu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, email, birth;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.findViewsById();
        this.setFonts();
    }

    private void findViewsById(){
        name = (TextView) findViewById(R.id.profileName);
        email = (TextView) findViewById(R.id.profileEmail);
        birth = (TextView) findViewById(R.id.profileBirth);
        parentLayout = (LinearLayout) findViewById(R.id.profileParentLayout);
    }

    private void setFonts(){
        Utilities.setFontNexa(this, parentLayout);
    }

    private void getProfile(){

    }
}
