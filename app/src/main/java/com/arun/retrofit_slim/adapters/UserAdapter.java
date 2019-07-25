package com.arun.retrofit_slim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arun.retrofit_slim.R;
import com.arun.retrofit_slim.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        holder.txtName.setText(userList.get(position).getName());
        holder.txtEmail.setText(userList.get(position).getEmail());
        holder.txtSchool.setText(userList.get(position).getSchool());
    }

    @Override
    public int getItemCount() {
        return userList!= null ? userList.size() : 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView txtEmail, txtName, txtSchool;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtName = itemView.findViewById(R.id.txtName);
            txtSchool= itemView.findViewById(R.id.txtSchool);
        }
    }
}
