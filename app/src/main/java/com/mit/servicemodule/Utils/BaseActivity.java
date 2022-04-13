package com.mit.servicemodule.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mit.servicemodule.R;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    Dialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void showLoading() {
        if (isFinishing()) return;
        progressDialog = new Dialog(this);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
    }
    @Override
    public void showLoading(Context context) {

    }
    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSnackBarGreen(String msg) {
        if (null != msg) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    msg, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.accepted_snackbar));

            snackbar.show();
        }
    }
    @Override
    public void showSnackBarRed(String msg) {
        if (null != msg) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    msg, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.rejected_snackbar));

            snackbar.show();
        }
    }
    @Override
    public void setTitle(String msg) {

    }
}
