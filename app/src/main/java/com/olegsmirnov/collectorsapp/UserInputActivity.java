package com.olegsmirnov.collectorsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
        Button buttonOK = (Button) findViewById(R.id.button_confirm);
        final EditText et1 = (EditText) findViewById(R.id.edittext_description);
        final EditText et2 = (EditText) findViewById(R.id.edittext_price);
        final Intent i = getIntent();
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("description", et1.getText().toString());
                intent.putExtra("price", Double.parseDouble(et2.getText().toString()));
                intent.putExtra("filePath", i.getStringExtra("filePath"));
                setResult(2, intent);
                finish();
            }
        });
    }

}
