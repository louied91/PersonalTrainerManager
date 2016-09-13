package spcstudent.android.personaltrainermanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class FragmentLogout extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logoutPromptText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(logoutIntent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}