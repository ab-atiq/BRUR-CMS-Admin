package com.atiq.cmsadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.atiq.cmsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTeacher extends AppCompatActivity {
    ImageView addTeacherImage;
    EditText addTeacherName,addTeacherEmail,addTeacherPost;
    Spinner teacherCategory;
    Button addTeacherBtn;

    String selectCategory;
    private final int REQ = 1;
    private Bitmap bitmap;
    ProgressDialog pd;
    DatabaseReference databaseReference,dbRef;
    StorageReference storageReference;
    String name, email, post, downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        addTeacherImage = findViewById(R.id.addTeacherImage);
        addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherEmail = findViewById(R.id.addTeacherEmail);
        addTeacherPost = findViewById(R.id.addTeacherPost);
        teacherCategory = findViewById(R.id.teacherCategory);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);
        pd = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("teachers");
        storageReference = FirebaseStorage.getInstance().getReference().child("teachers");

        String[] items = new String[]{"Select Category","CSE","EEE","Physics","Mathematics","Chemistry"};
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        teacherCategory.setAdapter(arrayAdapter);
        teacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCategory = teacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation(arrayAdapter);
            }
        });

    }

    private void checkValidation(SpinnerAdapter arrayAdapter) {
        name = addTeacherName.getText().toString();
        email = addTeacherEmail.getText().toString();
        post = addTeacherPost.getText().toString();
        // select child as selected category
        dbRef = databaseReference.child(selectCategory);

        if(name.isEmpty()){
            addTeacherName.setError("Empty name");
            addTeacherName.requestFocus();
        }else if(email.isEmpty()){
            addTeacherEmail.setError("Empty email");
            addTeacherEmail.requestFocus();
        }else if(post.isEmpty()){
            addTeacherPost.setError("Empty post");
            addTeacherPost.requestFocus();
        }else if(selectCategory.equals("Select Category")){
            Toast.makeText(getApplicationContext(),"Please select teacher category",Toast.LENGTH_LONG).show();
        }else if(bitmap==null){
            uploadData(arrayAdapter);
        }else{
            uploadImage(arrayAdapter);
        }
    }

    private void uploadImage(SpinnerAdapter arrayAdapter) {
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalImg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    // upload data
                                    uploadData(arrayAdapter);
                                }
                            });
                        }
                    });
                }else{
                    pd.dismiss();
                    Toast.makeText(AddTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData(SpinnerAdapter arrayAdapter) {
        final String uniqueKey = dbRef.push().getKey();
        TeacherData teacherData = new TeacherData(name, email, post, downloadUrl, uniqueKey);
        dbRef.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                addTeacherEmail.setText("");
                addTeacherName.setText("");
                addTeacherPost.setText("");
                bitmap = null;
//                addTeacherImage.setImageBitmap(bitmap);
                teacherCategory.setAdapter(arrayAdapter);
                // recreate this page
                // recreate();
                Toast.makeText(AddTeacher.this, "Data successfully added ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),UpdateFaculty.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void OpenGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    // set image in image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addTeacherImage.setImageBitmap(bitmap);
        }
    }

}