package au.edu.canberra.mtfinalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;


public class ActivitySeven extends AppCompatActivity {
    ArrayList<ClassifiedItem> ClassifiedItem = new ArrayList<ClassifiedItem>();
    private TextView editText;
    private TextView editText2;
    private Bitmap photoBitmap;
    private ImageView imageView;
    private Uri outputFileUri;
    String imageFileName;
    String itemName;
    String classifiedResult;
    String key;
    String company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven);

        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        classifiedResult = intent.getStringExtra("classifiedResult");
        imageFileName=intent.getStringExtra("imageFileName");
        outputFileUri=intent.getParcelableExtra("uri");
        key=intent.getStringExtra("key");
        company = intent.getStringExtra("company");
        setTitle(company);
        try {
            photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputFileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editText=findViewById(R.id.editText);
        editText.setText("");
        editText.append(itemName);
        editText2=findViewById(R.id.editText2);
        editText2.setText("");
        editText2.append(classifiedResult);
        imageView=findViewById(R.id.imageView);
        imageView.setImageBitmap(photoBitmap);
    }
    public void cancel(View view){
        Intent intent = new Intent(getApplicationContext(),ActivityEight.class);
        startActivity(intent);
    }
    public void save(View view){

        if(key!=null) {
            ClassifiedItem item = new ClassifiedItem(editText.getText().toString(), editText2.getText().toString(), imageFileName);
            updateDataItemFromFirebaseDatabase(item, key);
        }
        else{
            addItemToRealtimeDB();
            uploadFileToFirebaseStorate(imageFileName,outputFileUri);}
        Intent intent = new Intent(getApplicationContext(),ActivityEight.class);
        startActivity(intent);
    }
    public void addItemToRealtimeDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("classified_item");

        ClassifiedItem Item = new ClassifiedItem(editText.getText().toString(), editText2.getText().toString(), imageFileName);
        String key = dbRef.push().getKey();
        dbRef.child(key).child("ItemName").setValue(Item.getItemName());
        dbRef.child(key).child("ClassifiedResult").setValue(Item.getClassifiedResult());
        dbRef.child(key).child("ImageFileName").setValue(Item.getImageFileName());

    }
    public void uploadFileToFirebaseStorate(String filename, Uri photoUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("images");
        if (photoUri != null) {
            UploadTask uploadTask = imagesRef.child(filename).putFile(photoUri);

            uploadTask.addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Upload failed: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Upload complete",
                                            Toast.LENGTH_LONG).show();
                                }
                            })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                }
                            });
        } else {
            Toast.makeText(getApplicationContext(), "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }
    }
    public String updateDataItemFromFirebaseDatabase(ClassifiedItem item,String key)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("classified_item");
        dbRef.child(key).child("ItemName").setValue(item.getItemName());
        dbRef.child(key).child("ClassifiedResult").setValue(item.getClassifiedResult());
        dbRef.child(key).child("ImageFileName").setValue(item.getImageFileName());
        return key;
    }
    public void deleteDataItemFromFirebaseDatabase(String key)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("classified_item");
        if(key != null) {
            dbRef.child(key).removeValue();
        }
        Intent intent = new Intent(getApplicationContext(),ActivityEight.class);
        intent.putExtra("company", company);
        startActivity(intent);
    }
}
