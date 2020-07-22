package au.edu.canberra.mtfinalassignment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// Please do not change this class name
public class ClassifiedItemAdapter extends ArrayAdapter<ClassifiedItem> {
    ArrayList<ClassifiedItem> items;

    public ClassifiedItemAdapter(Context context, int resource, ArrayList<ClassifiedItem> objects) {
        super(context, resource, objects);
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.my_listview_item, parent, false);
        }

        ClassifiedItem item = items.get(position);

        ImageView icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
        setIconImage(item.getImageFileName(),convertView);
        TextView title = (TextView) convertView.findViewById(R.id.textViewTitle);
        title.setText(item.getItemName());

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        return convertView;
    }

    public void setIconImage(String imageFileName, final View view) {
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
                                    Uri uri = Uri.fromFile(localFile);
                                    ImageView icon = (ImageView) view.findViewById(R.id.imageViewIcon);
                                    icon.setImageURI(uri);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
        } catch (IOException ex) {
            ex.getMessage();
        }
    }
}

