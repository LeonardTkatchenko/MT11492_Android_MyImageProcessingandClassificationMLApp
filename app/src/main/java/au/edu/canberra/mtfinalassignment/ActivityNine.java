package au.edu.canberra.mtfinalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ActivityNine extends AppCompatActivity {
    String ItemName;
    String ClassifiedResult;
    String ImageFileName;
    Uri uri;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nine);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        ItemName = extras.getString("ItemName");
        ClassifiedResult = extras.getString("ClassifiedResult");
        ImageFileName = extras.getString("ImageFileName");
        key=extras.getString("key");


        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText(ItemName);


        try {
            final File localFile = File.createTempFile("temp", ".jpg");
            setImage(ImageFileName,localFile);
        } catch (IOException ex) {
            notifyUser(ex.getMessage());
        }

        tv = (TextView) findViewById(R.id.textView2);
        tv.setText(ClassifiedResult);

    }
    public void setImage(String imageFileName, final File file) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images");
        final StorageReference fileRef = storageRef.child(imageFileName);
        try {
            final File localFile = File.createTempFile("temp", ".jpg");
            fileRef.getFile(localFile)
                    .addOnSuccessListener(
                            new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    uri = Uri.fromFile(localFile);
                                    ImageView imageView = (ImageView)findViewById(R.id.imageView);
                                    imageView.setImageURI(uri);
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
        } catch (IOException ex) {
            ex.getMessage();
        }
    }

    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void edit(View view){
//        editicontent
        Intent intent = new Intent(getApplicationContext(),ActivitySeven.class);
        intent.putExtra("uri",uri);
        intent.putExtra("key",key);
        intent.putExtra("itemName",ItemName);
        intent.putExtra("classifiedResult",ClassifiedResult);
        intent.putExtra("imageFileName",ImageFileName);
        startActivity(intent);
    }
    public void delete(View view){
        deleteDataItemFromFirebaseDatabase(key);
        Intent intent = new Intent(getApplicationContext(),ActivityEight.class);
        startActivity(intent);
    }
    public void deleteDataItemFromFirebaseDatabase(String key)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference("classified_item");
        if(key != null) {
            dbRef.child(key).removeValue();
        }
        Intent intent = new Intent(getApplicationContext(),ActivityEight.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.list) {
            Intent intent = new Intent(this, ActivityEight.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.add) {
            Intent intent = new Intent(this, ActivityThree.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}