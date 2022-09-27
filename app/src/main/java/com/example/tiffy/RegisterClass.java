package com.example.tiffy;

import static com.example.tiffy.SQLiteHelper.TABLE_NAME;
import static com.example.tiffy.SQLiteHelper.Table_Column_1_Name;
import static com.example.tiffy.SQLiteHelper.Table_Column_2_Email;
import static com.example.tiffy.SQLiteHelper.Table_Column_3_Password;
import static com.example.tiffy.SQLiteHelper.Table_Column_ID;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterClass extends AppCompatActivity {

    EditText emailRegister, passwordRegister, confirmPassRegister, nameRegister;
    String NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";
    int condition;
    String password, cpassword, correctEmail;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sqLiteHelper = new SQLiteHelper(this);

        emailRegister=(EditText)findViewById(R.id.register_email);
        passwordRegister=(EditText)findViewById(R.id.register_password);
        confirmPassRegister=(EditText)findViewById(R.id.register_confirm_password);
        nameRegister=(EditText)findViewById(R.id.register_name);

        Button buttonRegister=(Button)findViewById(R.id.register);



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password=passwordRegister.getText().toString();
                cpassword=confirmPassRegister.getText().toString();
                getEmail();

                if(cpassword.equals(password))
                {
                    if(correctEmail.matches(emailPattern)) {
                        // Creating SQLite database if dose n't exists
                        SQLiteDataBaseBuild();

                        // Creating SQLite table if dose n't exists.
                        SQLiteTableBuild();

                        // Checking EditText is empty or Not.
                        CheckEditTextStatus();

                        // Method to check Email is already exists or not.
                        CheckingEmailAlreadyExistsOrNot();


                        if (condition == 0) {

                            Intent i = new Intent(RegisterClass.this, Login.class);
                            startActivity(i);
                            // Empty EditText After done inserting process.
                            EmptyEditTextAfterDataInsert();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please enter Valid Email Address!",Toast.LENGTH_LONG).show();
                        emailRegister.getText().clear();
                        emailRegister.requestFocus();
                    }
                }
                else
                {
                    Toast.makeText(RegisterClass.this,"Password does not Match!",Toast.LENGTH_LONG).show();
                    confirmPassRegister.getText().clear();
                    confirmPassRegister.requestFocus();
                }
            }
        });

    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+Table_Column_1_Name+" VARCHAR, "+Table_Column_2_Email+" VARCHAR, "+Table_Column_3_Password+" VARCHAR)");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(EditTextEmptyHolder == true)
        {

            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO "+ TABLE_NAME+" (name,email,password) VALUES('"+NameHolder+"', '"+EmailHolder+"', '"+PasswordHolder+"');";

            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // Closing SQLite database object.
            sqLiteDatabaseObj.close();

            // Printing toast message after done inserting.
            Toast.makeText(RegisterClass.this,"Registration Successful!", Toast.LENGTH_LONG).show();
            condition=0;

        }
        // This block will execute if any of the registration EditText is empty.
        else {

            // Printing toast message if any of EditText is empty.
            Toast.makeText(RegisterClass.this,"Please fill all the Required Fields Correctly!", Toast.LENGTH_LONG).show();
            condition=1;
        }

    }

    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){

        nameRegister.getText().clear();

        emailRegister.getText().clear();

        passwordRegister.getText().clear();

        confirmPassRegister.getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        NameHolder = nameRegister.getText().toString() ;
        EmailHolder = emailRegister.getText().toString();
        PasswordHolder = passwordRegister.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(TABLE_NAME, null, " " + Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                F_Result = "Email Found";

                // Closing cursor.
                cursor.close();
            }
        }

        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }


    // Checking result
    public void CheckFinalResult(){

        // Checking whether email is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found"))
        {

            // If email is exists then toast msg will display.
            Toast.makeText(RegisterClass.this,"Email Already Exists!",Toast.LENGTH_LONG).show();
            emailRegister.getText().clear();
            emailRegister.requestFocus();
            condition=1;

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }

    public String getEmail()
    {
        correctEmail=emailRegister.getText().toString().trim();
        return correctEmail;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
