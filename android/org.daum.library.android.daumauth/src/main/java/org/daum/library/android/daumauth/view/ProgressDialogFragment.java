package org.daum.library.android.daumauth.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 10/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class ProgressDialogFragment extends DialogFragment {

    private static final String TEXT_LOADING = "Tentative de connexion, veuillez patienter...";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage(TEXT_LOADING);
        return pDialog;
    }
}
