package com.example.tiffy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    public static final String UserEmail = "";
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static Boolean OneTimeLogin;
    public static String EMAIL, Email;
    EditText emailLogin, passwordLogin;
    Button loginBtn;
    TextView forgotPasswordText, getRegisteredText;
    Cursor cursor;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    int condition;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String TempPassword = "NOT_FOUND" ;
    String EmailHolder, PasswordHolder;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);


        emailLogin=(EditText)findViewById(R.id.login_email);
        passwordLogin=(EditText)findViewById(R.id.login_password);


        loginBtn=(Button)findViewById(R.id.login_btn);

        forgotPasswordText=(TextView)findViewById(R.id.login_forgot_password);
        getRegisteredText=(TextView)findViewById(R.id.login_register);

        sqLiteHelper = new SQLiteHelper(this);


        OneTimeLogin=false;



        getRegisteredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this, RegisterClass.class);
                startActivity(i);
            }
        });




        //Adding click listener to log in button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                CheckEditTextStatus();

                GetEmail();

                //Shared Prefrence for using the value of EMAIL outside this onclick method
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Email Address Value", EMAIL);
                editor.commit();

                // Calling login method.
                LoginFunction();

                if(condition!=1)
                {
                    EmptyEdittext();
                }
            }
        });


        //If statement for the one time login style
        if (settings.getString("logged", "").toString().equals("logged")) {

            OneTimeLogin=true;
            Email=sharedpreferences.getString("Email Address Value", null);
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(),"Logged in as: "+Email,Toast.LENGTH_LONG).show();
        }
    }

    // Login function starts from here.
    public void LoginFunction(){

        if(EditTextEmptyHolder) {

            if(EMAIL.matches(emailPattern)) {
                // Opening SQLite database write permission.
                sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

                // Adding search email query to cursor.
                cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

                while (cursor.moveToNext()) {

                    if (cursor.isFirst()) {

                        cursor.moveToFirst();

                        // Storing Password associated with entered email.
                        TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password));

                        // Closing cursor.
                        cursor.close();
                    }
                }

                // Calling method to check final result ..
                CheckFinalResult();
            }

            else{
                Toast.makeText(getApplicationContext(),"Please enter Valid Email Address!",Toast.LENGTH_LONG).show();
                emailLogin.getText().clear();
                emailLogin.requestFocus();
            }

        }
        else {

            //If any of login EditText empty then this block will be executed.
            Toast.makeText(getApplicationContext(),"Please fill all the Fields Correctly!",Toast.LENGTH_LONG).show();

        }

    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        EmailHolder = emailLogin.getText().toString().trim();
        PasswordHolder = passwordLogin.getText().toString();

        // Checking EditText is empty or no using TextUtils.
        if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    // Checking entered password from SQLite database email associated password.
    public void CheckFinalResult(){

        if(TempPassword.equalsIgnoreCase(PasswordHolder))
        {

            Toast.makeText(getApplicationContext(),"Logged in Successfully",Toast.LENGTH_SHORT).show();

            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(Login.this, Home.class);

            //Shared prefrence for the one time login style
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("logged", "logged");
            editor.commit();

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(UserEmail, EmailHolder);

            startActivity(intent);

            finish();


        }
        else {

            Toast.makeText(getApplicationContext(),"Email or Password is Wrong, Please Try Again!",Toast.LENGTH_LONG).show();
            condition=1;

        }
        TempPassword = "NOT_FOUND" ;

    }





    public String GetEmail(){
        EMAIL=emailLogin.getText().toString().trim();
        return EMAIL;
    }
    public void EmptyEdittext(){
        emailLogin.getText().clear();
        passwordLogin.getText().clear();
    }

}