package ua.sim23.tsd.blocker.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ua.sim23.tsd.blocker.R;

public class Prompt extends Alert {

    EditText prompt;

    public Prompt(Context context) {
        super(context);

        LayoutInflater li = LayoutInflater.from(context);
        alertView = li.inflate(R.layout.prompt_dialog,null);
        alert.setView(alertView);
        super.setMessageView(R.id.prompt_message);
        super.setTitleView(R.id.prompt_title);
        prompt = alertView.findViewById(R.id.prompt_data);
    }

    public void SetPromptType (int inputType){
        prompt.setInputType(inputType);
    }
    public String GetValue(){
        return prompt.getText().toString();
    }
    @Override
    public void show() {
//        super.show();
        ((TextView)alertView.findViewById(R.id.prompt_title)).setText(Title);
        ((TextView)alertView.findViewById(R.id.prompt_message)).setText(Message);
        AlertDialog alertD = alert.create();
        alertD.show();
    }
}
