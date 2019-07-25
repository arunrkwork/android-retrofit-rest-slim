package com.arun.retrofit_slim.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arun.retrofit_slim.R;
import com.arun.retrofit_slim.activities.LoginActivity;
import com.arun.retrofit_slim.activities.MainActivity;
import com.arun.retrofit_slim.activities.ProfileActivity;
import com.arun.retrofit_slim.api.RetrofitClient;
import com.arun.retrofit_slim.models.DefaultResponse;
import com.arun.retrofit_slim.models.LoginResponse;
import com.arun.retrofit_slim.models.User;
import com.arun.retrofit_slim.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragments extends Fragment implements View.OnClickListener {


    EditText edEmail, edName, edSchool, edCurrentPassword, edNewPassword;
    Button btnUpdate, btnChangePassword, btnDeleteProfile, btnLogout;

    public SettingsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_fragments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edEmail = view.findViewById(R.id.edEmail);
        edName = view.findViewById(R.id.edName);
        edSchool= view.findViewById(R.id.edSchool);
        edCurrentPassword= view.findViewById(R.id.edCurrentPassword);
        edNewPassword= view.findViewById(R.id.edNewPassword);

        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePassword= view.findViewById(R.id.btnChangePassword);
        btnDeleteProfile= view.findViewById(R.id.btnDeleteProfile);
        btnLogout= view.findViewById(R.id.btnLogout);

        btnUpdate.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnDeleteProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnUpdate) {
            updateProfile();
        } else if (id == R.id.btnChangePassword) {
            updatePassword();
        } else if (id == R.id.btnDeleteProfile) {
            deleteUser();
        } else if (id == R.id.btnLogout) {
            logout();
        }
    }

    private void deleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?");
        builder.setMessage("This action is irreversible...");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void delete() {
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteUser(user.getId());

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                if (!response.body().isErr()) {
                    SharedPrefManager.getInstance(getActivity()).clear();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void logout() {
        SharedPrefManager.getInstance(getActivity()).clear();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void updatePassword() {

        String currentPassword = edCurrentPassword.getText().toString().trim();
        String newPassword = edNewPassword.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            edCurrentPassword.setError("Current Password Required");
            edCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            edNewPassword.setError("New Password Required");
            edNewPassword.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updatePassword(
            currentPassword,
                newPassword,
                user.getEmail()
        );

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void updateProfile() {
        String email = edEmail.getText().toString();
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

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateUser(user.getId(), email, name, school);
        
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (!response.body().isError()) {
                    SharedPrefManager
                            .getInstance(getActivity())
                            .saveUser(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });




    }
}
