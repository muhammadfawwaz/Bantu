package com.example.muhammadafifaf.bantu;

import android.graphics.PorterDuff;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    Button helpButton;
    EditText numberEditText;
    EditText messageEditText;
    String responseMessage;
    int j = 0;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        helpButton = (Button) findViewById(R.id.help_button_id);
        helpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        numberEditText = (EditText) findViewById(R.id.number_edit_text_id);
        numberEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("test",Integer.toString(i));
                if(i == 0) {
                    numberEditText.getText().clear();
                    i++;
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            int request = 0;
            @Override
            public void onClick(View view) {
                if(numberEditText.getText().toString().isEmpty() || numberEditText.getText().toString().equals("Fill your friends phone number") || messageEditText.getText().toString().isEmpty()
                        || messageEditText.getText().toString().equals("Fill your message")) {
                    Toast.makeText(MainActivity.this,"Number or message is empty", LENGTH_SHORT).show();
                }
                else {
                    try {
                        request = requestApi();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this,responseMessage,LENGTH_SHORT).show();
                }
            }
        });

        messageEditText = (EditText) findViewById(R.id.message_edit_text_id);
        messageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(j == 0) {
                    messageEditText.getText().clear();
                    j++;
                }
            }
        });
    }

    private int requestApi() throws IOException {
        String url = "https://api.mainapi.net/smsnotification/1.0.0/messages";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        //
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Authorization", "Bearer e171f67c3e9b279c59e3fbf4a028f468");

        String urlParameters = "msisdn=" + numberEditText.getText().toString() + "&content=" + messageEditText.getText().toString();

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        responseMessage = con.getResponseMessage();
        Log.v("result", String.valueOf(responseCode));

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return responseCode;
    }
}
