package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Date;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class MainActivity extends AppCompatActivity {
    Button sendButton;
    EditText text;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<UserMessage> messageList = new ArrayList<UserMessage>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        mMessageAdapter = new MessageListAdapter(this, messageList);

        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        mMessageAdapter.notifyDataSetChanged();
        text = (EditText) findViewById(R.id.edittext_chatbox);
        sendButton = (Button) findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                mMessageAdapter.appendMessage(new UserMessage(2, text.getText().toString(), dateFormat.format(date)));
                mMessageAdapter.appendMessage(new UserMessage(1, "lo lo cc", dateFormat.format(date)));
                text.setText("");
            }
        });
    }
}
