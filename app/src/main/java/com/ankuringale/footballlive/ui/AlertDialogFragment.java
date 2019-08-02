package com.ankuringale.footballlive.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment{
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sorry!").setMessage("There was an error. Press OK to continue.").setPositiveButton("OK",null);
        return builder.create();
        }
}

