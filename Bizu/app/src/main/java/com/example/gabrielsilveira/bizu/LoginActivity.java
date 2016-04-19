package com.example.gabrielsilveira.bizu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabrielsilveira.bizu.model.User;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private Button btLogin, btFacebook;
    private ToggleableRadioButton rdBtRememberMe;
    private TextView forgetPassword, register, btFacebookText;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.findViewsById();
        this.setFonts();
        this.dismissKeyboard(parentLayout);
        this.setOnClickListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLogin:
                if(verifyFields()) {
                    this.login();
                }
                break;
            case R.id.btFacebook:
                this.loginFacebook();
                break;
            case R.id.forgetPassword:
                this.forgotPassword();
                break;
            case R.id.goToRegister:
                this.goToRegister();
                break;
        }
    }

    private void findViewsById(){
        email = (EditText) findViewById(R.id.emailLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        btLogin = (Button) findViewById(R.id.btLogin);
        btFacebook = (Button) findViewById(R.id.btFacebook);
        rdBtRememberMe = (ToggleableRadioButton) findViewById(R.id.rdBtRememberMe);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        register = (TextView) findViewById(R.id.goToRegister);
        btFacebookText = (TextView) findViewById(R.id.btFacebookText);
        parentLayout = (LinearLayout) findViewById(R.id.loginParentLayount);
    }

    private void setOnClickListener(){
        btLogin.setOnClickListener(this);
        btFacebook.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private boolean verifyFields(){
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Todos os campos são obrigatorios", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!email.getText().toString().contains("@")){
            Toast.makeText(getApplicationContext(), "Email inválido", Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(), "Senha deve conter no mínimo 5 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void forgotPassword() {

    }

    private void loginFacebook() {

    }

    private void login() {
        User user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());

        //verify if this user is in database
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    private void setFonts(){
        Utilities.setFontNexa(this, parentLayout);
    }

    private void dismissKeyboard(View view){
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilities.hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                dismissKeyboard(innerView);
            }
        }
    }

}

