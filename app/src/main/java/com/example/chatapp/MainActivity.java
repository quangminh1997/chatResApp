package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;

import com.android.volley.VolleyError;


public class MainActivity extends AppCompatActivity {
    Button sendButton;
    EditText text;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<UserMessage> messageList = new ArrayList<UserMessage>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // get RecyclerView by id
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        // create a message adapter
        mMessageAdapter = new MessageListAdapter(this, messageList);

        // create a layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        // set up adapter and layout manager for recycler view
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(layoutManager);

        // get text and button by id
        text = (EditText) findViewById(R.id.edittext_chatbox);
        sendButton = (Button) findViewById(R.id.button_chatbox_send);

        // set event for button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                final String user_text = text.getText().toString();
                mMessageAdapter.appendMessage(new UserMessage(1, user_text, dateFormat.format(date)));
                text.setText("");

                String URL = "http://34.87.20.31:8080/run_model";

                RequestQueue request_queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest myRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject chatbot_respone = new JSONObject(response);
                                    JSONArray action = chatbot_respone.getJSONArray("actions");
                                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                    Date date = new Date();
                                    mMessageAdapter.appendMessage(new UserMessage(2, action.toString(), dateFormat.format(date)));
                                    seen_last_message();

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                Date date = new Date();
                                mMessageAdapter.appendMessage(new UserMessage(2, error.toString(), dateFormat.format(date)));
                                seen_last_message();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("userinput", user_text);

                        return params;
                    }

                };
                request_queue.add(myRequest);
                seen_last_message();
            }
        });

    }
    private void seen_last_message(){
        mMessageRecycler.getLayoutManager().scrollToPosition(mMessageAdapter.getItemCount()-1);
    }
}
