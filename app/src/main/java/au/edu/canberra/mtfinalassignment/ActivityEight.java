package au.edu.canberra.mtfinalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityEight extends AppCompatActivity {
    ArrayList<ClassifiedItem> ClassifiedItem = new ArrayList<ClassifiedItem>();

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = db.getReference("classified_item");
    ArrayList<String> keys = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eight);
        setTitle("IBM and Google Image Classification");

        final ClassifiedItemAdapter adapter = new ClassifiedItemAdapter(
                this, R.layout.activity_eight, ClassifiedItem);
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ClassifiedItem item = new ClassifiedItem(
                        (String) dataSnapshot.child("ItemName").getValue(),
                        (String)(dataSnapshot.child("ClassifiedResult").getValue()),
                        ((String) dataSnapshot.child("ImageFileName").getValue())
                );
                adapter.add(item);
                keys.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key=dataSnapshot.getKey();
                int index=keys.indexOf(key);

                if(index!=-1)
                {
                    ClassifiedItem.remove(index);
                    keys.remove(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String key=keys.get(position);
                        ClassifiedItem dtm = ClassifiedItem.get(position);
                        Intent intent = new Intent(view.getContext(), ActivityNine.class);
                        intent.putExtra("ItemName", dtm.getItemName());
                        intent.putExtra("classified_item", dtm.getClassifiedResult());
                        intent.putExtra("ImageFileName", dtm.getImageFileName());
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("images");
        try {
            final File localFile = File.createTempFile("temp", ".jpg");

        } catch (IOException ex) {
            notifyUser(ex.getMessage());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void downloadFile(StorageReference fileRef, final File file) {
        if (file != null) {
            fileRef.getFile(file)
                    .addOnSuccessListener(
                            new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Uri uri = Uri.fromFile(file);
                                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                    //imageView.setImageURI(uri);
                                    notifyUser("Download complete");
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    notifyUser("Unable to download");
                                }
                            });
        }
    }

    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void openCap(View v) {
        Intent intent = new Intent(getApplicationContext(), ActivityTwo.class);
        startActivity(intent);
    }
}
