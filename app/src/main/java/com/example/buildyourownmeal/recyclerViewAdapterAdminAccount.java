package com.example.buildyourownmeal;

import static androidx.core.content.ContextCompat.startActivities;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterAdminAccount  extends RecyclerView.Adapter<recyclerViewAdapterAdminAccount.MyViewHolder> {

    //DATABASE
    private static databaseFunctions databaseFunctions;

    private static Context context;
    private static ArrayList<Integer> userId;
    private static ArrayList<String> username;
    private static ArrayList<String> userEmail;
    private static ArrayList<String> userContactNum;
    private static ArrayList<String> userPassword;
    private static ArrayList<String> userBan;
    private static ArrayList<String> userRole;

    public recyclerViewAdapterAdminAccount(Context context, ArrayList<Integer> userId, ArrayList<String> username,
                                           ArrayList<String> userEmail, ArrayList<String> userContactNum, ArrayList<String> userPassword,
                                           ArrayList<String> userBan, ArrayList<String> userRole) {
        recyclerViewAdapterAdminAccount.context = context;
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.userContactNum = userContactNum;
        this.userPassword = userPassword;
        this.userBan = userBan;
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public recyclerViewAdapterAdminAccount.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_admin_account, parent, false);
        return new recyclerViewAdapterAdminAccount.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapterAdminAccount.MyViewHolder holder, int position) {
        holder.userId.setText(String.valueOf(userId.get(position)));
        holder.username.setText(username.get(position));
        holder.userEmail.setText(userEmail.get(position));
        holder.userContactNum.setText(userContactNum.get(position));
        holder.userPassword.setText(userPassword.get(position));
        holder.userBan.setText(userBan.get(position));
        holder.userRole.setText(userRole.get(position));

        int getUserId = userId.get(position);

        holder.userEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, adminEditUser.class);

                    intent.putExtra("userId", getUserId);
                    intent.putExtra("username", username.get(position));
                    intent.putExtra("userEmail", userEmail.get(position));
                    intent.putExtra("userContactNum", userContactNum.get(position));
                    intent.putExtra("userPassword", userPassword.get(position));
                    intent.putExtra("ban", userBan.get(position));
                    intent.putExtra("role", userRole.get(position));

                    context.startActivity(intent);
                }
            }
        });

        holder.userDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int getPosition = holder.getAdapterPosition();
                int position = getPosition;

                if (position != RecyclerView.NO_POSITION) {
                    databaseFunctions = new databaseFunctions(context);
                    Dialog popUpAlert;
                    Button deleteAccBtn;
                    TextView cancelAccDeleteBtn;

                    popUpAlert = new Dialog(context);
                    popUpAlert.setContentView(R.layout.pop_up_delete_account);
                    popUpAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    popUpAlert.getWindow().setBackgroundDrawableResource(R.drawable.pop_up_bg);
                    popUpAlert.setCancelable(true);
                    popUpAlert.show();

                    cancelAccDeleteBtn = popUpAlert.findViewById(R.id.cancelAccDeleteBtn);
                    deleteAccBtn = popUpAlert.findViewById(R.id.deleteAccBtn);

                    //ALERT TEXT
                    cancelAccDeleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUpAlert.dismiss();
                        }
                    });

                    //CLOSE
                    deleteAccBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Position", String.valueOf(getUserId));
                            databaseFunctions.deleteAccount("account", getUserId);
                            userId.remove(position);
                            username.remove(position);
                            userEmail.remove(position);
                            userContactNum.remove(position);
                            userPassword.remove(position);
                            userBan.remove(position);
                            userRole.remove(position);
                            notifyItemRemoved(position);
                            popUpAlert.dismiss();

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userId, username, userEmail, userContactNum, userPassword, userBan, userRole, userEdit, userDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userId = itemView.findViewById(R.id.userId);
            username = itemView.findViewById(R.id.username);
            userEmail = itemView.findViewById(R.id.userEmail);
            userPassword = itemView.findViewById(R.id.userPassword);
            userContactNum = itemView.findViewById(R.id.userContactNum);
            userBan = itemView.findViewById(R.id.userBan);
            userRole = itemView.findViewById(R.id.userRole);
            userEdit = itemView.findViewById(R.id.userEdit);
            userDelete = itemView.findViewById(R.id.userDelete);
        }
    }
}
