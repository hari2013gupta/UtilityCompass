/*
 * Created by Hariom.Gupta.Gurugram on 30/01/20.
 * hk.mca08@gmail.com
 * 8510887828
 */
package com.hari.utilitycompass;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import static com.hari.utilitycompass.Utility.IsInternetConnected;
import static com.hari.utilitycompass.Utility.enableVisible;
import static com.hari.utilitycompass.Utility.notNullParams;
import static com.hari.utilitycompass.Utility.showSnackMessage;

public class BaseActivity extends AppCompatActivity implements MyConstants {
    Dialog dialog;
    static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog mProgressD;

    public void hideKeyBoard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closeDatabase(SQLiteDatabase dbObject) {
        try {
            if (dbObject != null && dbObject.isOpen()) {
                dbObject.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initToolBar(final AppCompatActivity activity, String titleName, int statusColor, int bgColor, boolean showBack) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(statusColor));
        }
        toolbar.setBackgroundColor(activity.getResources().getColor(bgColor));
        toolbar.setTitle(titleName);

        activity.setSupportActionBar(toolbar);
        final ActionBar ab = activity.getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);

        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.layout_toolbar_common, null);
        TextView tv_ToolbarTitle = view.findViewById(R.id.tv_ToolbarTitle);
        tv_ToolbarTitle.setText(titleName);

        ab.setCustomView(view);
        ab.setDisplayShowCustomEnabled(true);

        if (showBack) {
            toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
            toolbar.setNavigationOnClickListener(
                    v -> activity.finish()
            );
        } else {
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart BaseActivity");
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Log.d(TAG, "interact activity");
    }

    public void showSnack(String message) {
        showSnackMessage(this, message, false);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "isMyServiceRunning?");
                return true;
            }
        }
        Log.i(TAG, "isMyServiceRunning?");
        return false;
    }

    public void showProgressD(String message) {
        if (mProgressD == null) {
            mProgressD = new ProgressDialog(this);
            mProgressD.setIndeterminate(true);
            mProgressD.setCancelable(false);
            mProgressD.setMessage(message);
        }

        mProgressD.show();
    }

    public void hideProgressD() {
        if (mProgressD != null && mProgressD.isShowing()) {
            mProgressD.dismiss();
        }
    }

    public boolean openFragment(Fragment fragment, boolean isReplace) {
        if (fragment != null) {
            String fragmentTAG = fragment.getClass().getSimpleName();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            if (isReplace) {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(FRAME_CONTAINER_ID, fragment, fragmentTAG);
            } else {
                ft.add(FRAME_CONTAINER_ID, fragment, fragmentTAG);
                ft.addToBackStack(fragmentTAG);
            }
            ft.commit();
            return true;
        } else {
            return false;
        }
    }

    public void snackBar(View view, String message, String buttonText) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).
                setAction(buttonText, v -> {
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notification_template_icon_bg, 0, 0, 0);
        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.size_margin20));
        snackbar.show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void printError(Exception e) {
        Log.w(TAG, e.getMessage(), e);
    }

    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

//    public void showErrorMessageAlert(Context context) {
//        try {
//            if (!IsInternetConnected(context)) {
//                showSnack("No Internet Connection Available!");
//            } else {
//                showSnack("Something went wrong. Please try again\" + \" in some time ");
//            }
//        } catch (Exception ex) {
//        }
//    }

    public void showProgressDialog(Context context) {
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setAttributes(wlp);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface d, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    d.dismiss();
                }
                return true;
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void SummaryDialog(Activity activity) {
        final Dialog dialog;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            dialog = new Dialog(activity, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
        dialog = new Dialog(activity, R.style.CustomDialog);
//        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("====Alert Dialog====");
        StringBuilder sb = new StringBuilder();
        String titleString = "[App Configuration]";
        RelativeLayout receiptContainer = dialog.findViewById(R.id.receiptContainer);
        enableVisible(receiptContainer);
        TextView messageTV = dialog.findViewById(R.id.messageTV);
        messageTV.setMovementMethod(new ScrollingMovementMethod());
        messageTV.setText(sb.toString());

        TextView titleTV = dialog.findViewById(R.id.titleTV);
        if (notNullParams(titleString)) {
            titleTV.setText(titleString);
            messageTV.setTextSize(14);
        } else {
            titleTV.setText("--Summary--");
        }
        Button confirmBTN = dialog.findViewById(R.id.confirmBTN);
        confirmBTN.setVisibility(View.GONE);
        Button cancelBTN = dialog.findViewById(R.id.cancelBTN);
        cancelBTN.setText("OK");
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setOnKeyListener((arg0, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
            }
            return true;
        });
        dialog.show();
    }

}
