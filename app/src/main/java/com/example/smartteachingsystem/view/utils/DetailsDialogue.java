package com.example.smartteachingsystem.view.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.TeacherApp;

import java.util.List;

public class DetailsDialogue extends DialogFragment {

    private TextView status, head, body;
    private TeacherApp teacherApp;

    public DetailsDialogue(TeacherApp teacherApp) {
        this.teacherApp = teacherApp;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.details_dialogue,null);
        builder.setView(view).setTitle("Appointment").setIcon(R.drawable.ic_view).setCancelable(true)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        status= view.findViewById(R.id.detailsStatusId);
        head= view.findViewById(R.id.detailsMessageHeadId);
        body= view.findViewById(R.id.detailsMessageBodyId);

        switch (teacherApp.getStatus()) {
            case "Pending":
                status.setText(teacherApp.getStatus());
                status.setTextColor(Color.RED);
                head.setText("Your message: ");
                body.setText(teacherApp.getMessage());
                break;
            case "Decline":
                status.setText(teacherApp.getStatus());
                status.setTextColor(Color.RED);
                head.setText("Reply of: " + teacherApp.getName());
                body.setText(teacherApp.getMessage());
                break;
            case "Approve":

                status.setText(teacherApp.getStatus());
                status.setTextColor(Color.GREEN);
                head.setText("Reply of: " + teacherApp.getName());
                body.setText(teacherApp.getMessage());
                break;
        }


        return builder.create();
    }
}
