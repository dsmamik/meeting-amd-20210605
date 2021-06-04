package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class CustomDialog extends AppCompatDialogFragment {

    EditText formEditTextFullName, formEditTextPhoneNumber;
    String initFullName, initPhoneNumber;
    int initPosition;

    // listener untuk menyimpan data ketika user mengedit data item di dialog (edit)
    CustomDialogListener listener;

    public CustomDialog(String fullName, String phoneNumber, int position) {
        initFullName = fullName;
        initPhoneNumber = phoneNumber;
        initPosition = position;
    }

    // instance dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_form, null);

        builder.setView(view)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // what happen if the dialog is canceled
                }).setPositiveButton("Save", (dialog, which) -> {
                    // what happen if the dialog is saved
                    String fullName = formEditTextFullName.getText().toString();
                    String phoneNumber = formEditTextPhoneNumber.getText().toString();
                    listener.setDataFromCustomDialog(fullName, phoneNumber, this.initPosition);
                });

        // save to local variable
        formEditTextFullName = view.findViewById(R.id.formEditTextFullName);
        formEditTextPhoneNumber = view.findViewById(R.id.formEditTextPhoneNumber);

        formEditTextFullName.setText(initFullName);
        formEditTextPhoneNumber.setText(initPhoneNumber);

        return builder.create();
    }

    // attach dialog mirip seperti binding
    @Override
    public void onAttach(@NonNull  Context context) {
        super.onAttach(context);

        try {
            listener = (CustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CustomDialogListener");
        }
    }

    // listener untuk memngubah data item
    public interface CustomDialogListener {
        void setDataFromCustomDialog(String fullName, String phoneNumber, int position);
    }
}
