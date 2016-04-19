package com.example.gabrielsilveira.bizu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.gabrielsilveira.bizu.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, email, confirmPassword, password, dateBirth;
    private Button btRegister;
    private RadioButton rdBtMale;
    private ToggleableRadioButton rdAcceptTerms;
    private ImageView goUp;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.findViewsById();
        this.setOnclickListener();
        this.setDateTimeField();
        this.dismissKeyboard(parentLayout);
        this.setToolbar();
        this.setFonts();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateBirth:
                datePickerDialog.show();
                break;
            case R.id.btRegister:
                if(verifyFields()) {
                    this.register();
                }
                break;
            case R.id.registerGoUp:
                Utilities.goUpActivity(RegisterActivity.this);
                break;
        }
    }

    private void setOnclickListener() {
        btRegister.setOnClickListener(this);
        dateBirth.setOnClickListener(this);
        goUp.setOnClickListener(this);
    }

    private void setToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(myToolbar);
        if (myToolbar != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void findViewsById(){
        email = (EditText) findViewById(R.id.emailRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordRegister);
        name = (EditText) findViewById(R.id.nameRegister);
        dateBirth = (EditText) findViewById(R.id.dateBirth);
        btRegister = (Button) findViewById(R.id.btRegister);
        rdBtMale = (RadioButton) findViewById(R.id.rdBtMale);
        rdAcceptTerms = (ToggleableRadioButton) findViewById(R.id.rdAcceptTerms);
        goUp = (ImageView) findViewById(R.id.registerGoUp);
        parentLayout = (LinearLayout) findViewById(R.id.registerParentLayout);
    }

    private void setDateTimeField() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setFonts() {
        Utilities.setFontNexa(this, parentLayout);
    }

    private boolean verifyFields(){
        if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || dateBirth.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()){
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
        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "As senhas devem ser iguais", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!rdAcceptTerms.isChecked()){
            Toast.makeText(getApplicationContext(), "Para continuar voce deve aceitar os termos de compromisso", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void register(){
        User user = new User();
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setBirthDate(dateBirth.getText().toString());
        if(rdBtMale.isChecked()){
            user.setGender("M");
        } else {
            user.setGender("F");
        }

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

        //call the method for register the user in the database
    }

    private void dismissKeyboard(View view){
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilities.hideSoftKeyboard(RegisterActivity.this);
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
