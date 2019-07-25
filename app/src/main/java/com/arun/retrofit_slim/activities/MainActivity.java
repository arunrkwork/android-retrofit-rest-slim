package com.arun.retrofit_slim.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arun.retrofit_slim.R;
import com.arun.retrofit_slim.api.RetrofitClient;
import com.arun.retrofit_slim.models.DefaultResponse;
import com.arun.retrofit_slim.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    EditText edName, edPassword, edSchool, edEmail;
    Button btnSignup, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.edName);
        edPassword = findViewById(R.id.edPassword);
        edSchool = findViewById(R.id.edSchool);
        edEmail = findViewById(R.id.edEmail);

        btnSignup = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSignUp) {
            signUp();
        } else if (view.getId() == R.id.btnLogin) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void signUp() {
        String email = edEmail.getText().toString();
        String pass = edPassword.getText().toString();
        String name = edName.getText().toString();
        String school = edSchool.getText().toString();

        if (email.isEmpty()) {
            edEmail.setError("Email required");
            edEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Enter valid email");
            edEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            edPassword.setError("Password Required");
            edPassword.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            edPassword.setError("Password should be atleast  6 character");
            edPassword.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            edName.setError("Name required");
            edName.requestFocus();
            return;
        }

        if (school.isEmpty()) {
            edSchool.setError("School required");
            edSchool.requestFocus();
            return;
        }


      /*  Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(email, pass, name, school);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 201) {
                        String s = null;// != null ? response.body().string() : "";
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: " + s);
                    } else {
                        String s = null;
                        try {
                            s = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: " + s);
                    }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: err : " + t.getMessage());
            }
        });*/



      Call<DefaultResponse> call = RetrofitClient
              .getInstance()
              .getApi()
              .createUser(email, pass, name, school);

      call.enqueue(new Callback<DefaultResponse>() {
          @Override
          public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
              if (response.code() == 201) {
                  DefaultResponse dr = response.body();
                  Toast.makeText(MainActivity.this, "" + dr.getMsg(), Toast.LENGTH_SHORT).show();
              } else if (response.code() == 422) {
                  Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
              }
          }

          @Override
          public void onFailure(Call<DefaultResponse> call, Throwable t) {

          }
      });


    }
}
