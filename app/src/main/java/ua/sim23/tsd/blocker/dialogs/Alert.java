package ua.sim23.tsd.blocker.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import ua.sim23.tsd.blocker.R;

public class Alert {
    protected String Title = "";
    protected String Message = "";

    protected Context context;

    private TextView titleView;
    private TextView messageView;

    protected AlertDialog.Builder alert;
    View alertView;

    public Alert(Context context){
        this.context = context;
        alert = new AlertDialog.Builder(context);

        LayoutInflater li = LayoutInflater.from(context);
        alertView = li.inflate(R.layout.alert_dialog,null);
        alert.setView(alertView);
        setMessageView(R.id.alert_message);
        setTitleView(R.id.alert_title);
    }

    public void setCancelable(boolean cancel){
        alert = alert.setCancelable(cancel);
    }
    public void setPositiveButton(@StringRes int res, DialogInterface.OnClickListener cl){
        alert = alert.setPositiveButton(res, cl);
    }
    public void setPositiveButton(String res, DialogInterface.OnClickListener cl){
        alert = alert.setPositiveButton(res, cl);
    }
    public void setNegativeButton(@StringRes int res, DialogInterface.OnClickListener cl){
        alert = alert.setNegativeButton(res, cl);
    }
    public void setNegativeButton(String res, DialogInterface.OnClickListener cl){
        alert = alert.setNegativeButton(res, cl);
    }
    public void setNeutralButton(@StringRes int res, DialogInterface.OnClickListener cl){
        alert = alert.setNeutralButton(res, cl);
    }
    public void setNeutralButton(String res, DialogInterface.OnClickListener cl){
        alert = alert.setNeutralButton(res, cl);
    }
    public void setTitle(String title){
        Title = title;
    }
    public void setMessage(String message){
        Message = message;
    }
    protected void setTitleView(@IdRes int res){
        titleView = alertView.findViewById(res);
    }
    protected void setMessageView(@IdRes int res){
        messageView = alertView.findViewById(res);
    }
    public void setTitle(@StringRes int title){
        titleView.setText(title);
        Title = titleView.getText().toString();
    }
    public void setMessage(@StringRes int message){
        messageView.setText(message);
        Message = messageView.getText().toString();
    }
    public void show(){
        titleView.setText(Title);
        messageView.setText(Message);
        AlertDialog alertD = alert.create();
        alertD.show();
    }

}
