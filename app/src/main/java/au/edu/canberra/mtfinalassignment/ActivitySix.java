package au.edu.canberra.mtfinalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class ActivitySix extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    private static final int REQUEST_PERMISSION = 3000;
    private TextView textView1;
    private TextView textView2;
    private ImageView imageView;
    private Bitmap photoBitmap;
    private Uri outputFileUri;
    String imageFileName;
    String classifiedResult;
    String itemName;
    String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six);

        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        classifiedResult = intent.getStringExtra("classifiedResult");
        imageFileName=intent.getStringExtra("imageFileName");
        outputFileUri=intent.getParcelableExtra("uri");
        company = intent.getStringExtra("company");
        setTitle(company);
        try {
            photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputFileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView1=findViewById(R.id.textView1);
        textView1.setText("");
        textView1.append(itemName);

        textView2=findViewById(R.id.textView2);
        textView2.setText("");
        if (company.equals("Google Firebase ML Cloud Services")){
            textView2.append("Firebase ML: ");
        }
        else if(company.equals("IBM Watson ML Cloud Services")){
            textView2.append("IBM Watson: ");
        }
        textView2.append(classifiedResult);
        imageView=findViewById(R.id.imageView);
        imageView.setImageBitmap(photoBitmap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    public void edit(View view)
    {
        Intent intent = new Intent(getApplicationContext(),ActivitySeven.class);
        String itemName=textView1.getText().toString();
        String classifiedResult=textView2.getText().toString();
        intent.putExtra("uri",outputFileUri);
        intent.putExtra("itemName",itemName);
        intent.putExtra("classifiedResult",classifiedResult);
        intent.putExtra("imageFileName",imageFileName);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
