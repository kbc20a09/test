package com.websarva.wings.android.testproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MakeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adbuilder = new AlertDialog.Builder(getActivity());
        adbuilder.setMessage(R.string.dl_msg);
        adbuilder.setPositiveButton(R.string.dl_btn_ok, new DialogButtonClickListener());
        adbuilder.setNegativeButton(R.string.dl_btn_ng, new DialogButtonClickListener());
        adbuilder.setNeutralButton(R.string.dl_btn_nu, new DialogButtonClickListener());
        AlertDialog dialog = adbuilder.create();
        return dialog;
    }


    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String filepath = getArguments().getString("filepath", "");
            MainActivity ma = new MainActivity();
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent_rr = new Intent(getContext(),RecipeReader.class);
                intent_rr.putExtra("filepath",filepath);
                startActivity(intent_rr);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                Intent intent_fc = new Intent(getContext(),FileEditer.class);
                intent_fc.putExtra("filepath",filepath);
                startActivity(intent_fc);
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                break;
        }
        }
    }
}