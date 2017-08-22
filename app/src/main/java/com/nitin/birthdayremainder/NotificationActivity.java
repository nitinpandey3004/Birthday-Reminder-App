package com.nitin.birthdayremainder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotificationActivity extends AppCompatActivity {

    EditText etWishes;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        etWishes = (EditText) findViewById(R.id.etWishes);
        btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(etWishes.getText().toString().length() == 0){
                    etWishes.setError("Enter Yoyr wish");
                    etWishes.requestFocus();
                    return;
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,etWishes.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
}
