package com.example.dzakiyh.simpel.perpanjangan_sim.kirim_berkas;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.model.Upload;
import com.example.dzakiyh.simpel.pembuatan_sim_baru.kirim_berkas.UploadBerkasActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadBerkasPerpanjanganActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    private AlertDialog.Builder alertDialogBuilder;

    private TextView hintKTP, hintKK, hintSuratSehat;

    private Button chooseImageButton, uploadImageButton;
    private ProgressBar uploadProgressBar;

    private Uri imageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, fUserDatabase;
    private FirebaseAuth firebaseAuth;

    private Spinner spinner;

    private String chosenItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_upload_berkas_perpanjangan);

        initComponents();

        initSpinner();

        validateUI();

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set alertdialog
                alertDialogBuilder.setMessage("Anda akan mengunggah " + chosenItem + ".");
                alertDialogBuilder.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadFile();
                    }
                });

                alertDialogBuilder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void validateUI() {
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        fUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bahanEvalKTP = dataSnapshot.child("sudahKTPPerpanj").getValue().toString();
                String bahanEvalKK = dataSnapshot.child("sudahKKPerpanj").getValue().toString();
                String bahanEvalSKSehat = dataSnapshot.child("sudahSuratSehatPerpanj").getValue().toString();

                if(bahanEvalKK.equals("TRUE")){
                    hintKK.setText("Anda telah mengunggah KK.");
                    hintKK.setTextColor(Color.parseColor("#20C456"));
                }

                if(bahanEvalKTP.equals("TRUE")){
                    hintKTP.setText("Anda telah mengunggah KTP.");
                    hintKTP.setTextColor(Color.parseColor("#20C456"));
                }

                if(bahanEvalSKSehat.equals("TRUE")){
                    hintSuratSehat.setText("Anda telah mengunggah SK Sehat.");
                    hintSuratSehat.setTextColor(Color.parseColor("#20C456"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initSpinner() {
        spinner.setOnItemSelectedListener(this);

        List<String> listUploads = new ArrayList<String>();
        listUploads.add("KTP");
        listUploads.add("Kartu Keluarga");
        listUploads.add("Surat Keterangan Sehat");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUploads);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }

    private void openFileChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void initComponents() {
        chooseImageButton = findViewById(R.id.BTN_UploadPerpanj_ChooseFile);
        uploadImageButton = findViewById(R.id.BTN_UploadPerpanj_UploadFile);
        uploadProgressBar = findViewById(R.id.Progress_UploadPerpanj_uploadProgressBar);
        hintKTP = findViewById(R.id.TV_UploadPerpanj_hintKTP);
        hintKK = findViewById(R.id.TV_UploadPerpanj_hintKK);
        hintSuratSehat = findViewById(R.id.TV_UploadPerpanj_hintSKSehat);
        spinner = findViewById(R.id.Spinner_ListUploadPerpanjangan);

        alertDialogBuilder = new AlertDialog.Builder(this);
    }

    private void uploadFile(){
        if(imageUri != null){
            final StorageReference fileReference = mStorageRef.child("Perpanjangan-" + chosenItem + "-" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                uploadProgressBar.setProgress(0);
                            }
                        }, 5000);

                        Uri downloadUri = task.getResult();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String instDate = dateFormat.format(date);

                        String username = firebaseAuth.getCurrentUser().getUid();
                        String namaFile = chosenItem + "-" + "Perpanjangan SIM";

                        Upload upload = new Upload(username, namaFile, downloadUri.toString(), "Perpanjangan SIM");

                        mDatabaseRef.child(instDate).setValue(upload);

                        updateDatabaseStatus();

                        Toast.makeText(UploadBerkasPerpanjanganActivity.this, "Anda berhasil mengunggah gambar.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadBerkasPerpanjanganActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            fileReference.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    uploadProgressBar.setProgress((int)progress);
                }
            });
        }

        else{
            Toast.makeText(this, "Anda belum memilih file. Silahkan pilih gambar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDatabaseStatus() {
        if(chosenItem.equals("KTP")){
            fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("sudahKTPPerpanj");
            fUserDatabase.setValue("TRUE");
        }

        else if(chosenItem.equals("Kartu Keluarga")){
            fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("sudahKKPerpanj");
            fUserDatabase.setValue("TRUE");
        }

        else if(chosenItem.equals("Surat Keterangan Sehat")){
            fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("sudahSuratSehatPerpanj");
            fUserDatabase.setValue("TRUE");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        chosenItem = item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
