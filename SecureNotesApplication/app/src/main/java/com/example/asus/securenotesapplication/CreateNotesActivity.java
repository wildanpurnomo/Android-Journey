package com.example.asus.securenotesapplication;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class CreateNotesActivity extends AppCompatActivity {
    private Button btnCreate;
    private EditText noteTitle, noteContent;

    private FirebaseAuth fAuth;
    private DatabaseReference fNotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        btnCreate = (Button)findViewById(R.id.new_note_btn_create);
        noteTitle = (EditText)findViewById(R.id.new_note_title);
        noteContent = (EditText)findViewById(R.id.new_note_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitle.getText().toString().trim();
                String content = noteContent.getText().toString().trim();
                content = encodeString(content);
                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content))
                {
                    createNote(title, content);
                }
                else
                {
                    Snackbar.make(v, "Mohon diisi", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNote(String title, String content)
    {
        if(fAuth.getCurrentUser() != null)
        {
            final DatabaseReference newNoteRef = fNotesDatabase.push();

            final Map noteMap = new HashMap();
            noteMap.put("title", title);
            noteMap.put("content", content);

            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(CreateNotesActivity.this, "Notes berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(CreateNotesActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.start();
        }
        else
        {
            Toast.makeText(this, "Pengguna belum login", Toast.LENGTH_SHORT).show();
        }
    }

    public static String encodeString(String text)
    {
        byte[] encodedBytes = Base64.getEncoder().encode(text.getBytes());
        return new String(encodedBytes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case android.R.id.home:
                Intent backIntent = new Intent(this, MainActivity.class);
                startActivity(backIntent);
                finish();
                break;
        }
        return true;
    }
}
