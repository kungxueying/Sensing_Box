package com.example.sensingbox;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class firebase_upload extends AppCompatActivity {
    private static final int PICKER = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    private TextView uploadInfoText;
    private TextView downloadInfoText;
    private Button pickImgButton;
    private StorageReference mStorageRef;
    private Button uploadImgButton;
    private Button downloadImgButton;
    private Button deleteImgButton;
    private ImageView pickImg;
    private ImageView downloadImg;
    private String imgPath;
    private ProgressBar imgUploadProgress;
    private StorageReference riversRef;
    TextView t1,t2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdb_uploadtest);

        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference myRef= database.getReference("CCCC");

        myRef.addChildEventListener(new ChildEventListener() {//讀取
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                t1.setText(""+dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e("新增",snapshot.child("name").toString());
                }

            }
        });
        initData();
        initView();
    }
    public void b2(View v){//刪除
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CCCC");
        myRef.child("ABCD").removeValue();
    }
    public void deletedb(){//刪除
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CCCC");
        myRef.child("ABCD").removeValue();
    }
    public void b3(View v){//新增
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        //DatabaseReference myRef= database.getReference("CCCC");
       // myRef.child("ABCD").setValue("224466");
        DatabaseReference myRef= database.getReference("user");
        //myRef.child("111").setValue("");
        myRef.child("111").child("id").setValue("111");
        myRef.child("111").child("name").setValue("Amyy");
        myRef.child("111").child("email").setValue("fsiduhfgi");
        myRef.child("111").child("pwd").setValue("filsduzhtrl");
    }
    public void insertuser(DS_user newuser){//新增
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference myRef= database.getReference("user");
        myRef.child(newuser.id).child("id").setValue(newuser.id);
        myRef.child(newuser.id).child("name").setValue(newuser.name);
        myRef.child(newuser.id).child("email").setValue(newuser.email);
        myRef.child(newuser.id).child("pwd").setValue(newuser.pwd);
    }
    public void insertsensorall(DS_sensorall newsensor){//新增
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference myRef= database.getReference("sensorall");
        myRef.child("name").setValue(newsensor.sensorName);

    }
    public void insertdata(DS_dataset newdata){//新增
        final FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference myRef= database.getReference("dataset");
       /* myRef.child(newdata.sensor).child(newdata.time).child("sensor").setValue(newdata.sensor);
        myRef.child(newdata.sensor).child(newdata.time).child("time").setValue(newdata.time);
        myRef.child(newdata.sensor).child(newdata.time).child("userID").setValue(newdata.userID);
        myRef.child(newdata.sensor).child(newdata.time).child("locate").setValue(newdata.locate);
        myRef.child(newdata.sensor).child(newdata.time).child("data").setValue(newdata.data);
        myRef.child(newdata.sensor).child(newdata.time).child("boxID").setValue(newdata.boxID);*/
        String timekey = newdata.time.substring(0,10);//節點以小時為最小單位
        myRef.child(newdata.sensor).child(newdata.time).push().setValue(newdata);
    }

    public void b4(View v){//更新
        FirebaseDatabase database = FirebaseDatabase.getInstance();//取得資料庫連結
        DatabaseReference myRef = null;
        myRef= database.getReference("CCCC");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("DCBA","123456777");//前面的字是child後面的字是要修改的value值
        myRef.updateChildren(childUpdates);
    }

    private void initData() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void checkPermission(){
        int permission = ActivityCompat.checkSelfPermission(firebase_upload.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            getLocalImg();
        }

    }

    private void initView() {
        uploadInfoText = (TextView) findViewById(R.id.upload_info_text);
        downloadInfoText = (TextView) findViewById(R.id.download_info_text);
        pickImgButton = (Button) findViewById(R.id.pick_button);
        pickImg = (ImageView) findViewById(R.id.pick_img);
        uploadImgButton = (Button) findViewById(R.id.upload_button);
        downloadImg = (ImageView) findViewById(R.id.download_img);
        downloadImgButton = (Button) findViewById(R.id.download_button);
        imgUploadProgress = (ProgressBar) findViewById(R.id.upload_progress);
        deleteImgButton = (Button) findViewById(R.id.delete_button);

        pickImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        uploadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(imgPath)) {
                    imgUploadProgress.setVisibility(View.VISIBLE);
                    uploadImg(imgPath);
                } else{
                    Toast.makeText(firebase_upload.this, R.string.plz_pick_img, Toast.LENGTH_SHORT).show();
                }
            }
        });
        downloadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImg(riversRef);
            }
        });
        deleteImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImg(riversRef);
            }
        });
    }

    private void deleteImg(final StorageReference ref){
        if(ref == null){
            Toast.makeText(firebase_upload.this, R.string.plz_upload_img, Toast.LENGTH_SHORT).show();
            return;
        }
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(firebase_upload.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(firebase_upload.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadImg(final StorageReference ref){
        if(ref == null){
            Toast.makeText(firebase_upload.this, R.string.plz_upload_img, Toast.LENGTH_SHORT).show();
            return;
        }
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(firebase_upload.this)
                        .using(new FirebaseImageLoader())
                        .load(ref)
                        .into(downloadImg);
                downloadInfoText.setText(R.string.download_success);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                downloadInfoText.setText(exception.getMessage());
            }
        });
    }

    public void uploadImg(String path){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(path));
        System.out.println("圖片的路徑:");
        System.out.println(path);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentDisposition("universe")
                .setContentType("image/jpg")
                .build();
        riversRef = mStorageRef.child(file.getLastPathSegment());

        UploadTask uploadTask = riversRef.putFile(file, metadata);
        /*
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadInfoText.setText(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadInfoText.setText(R.string.upload_success);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                imgUploadProgress.setProgress(progress);
                if(progress >= 100){
                    imgUploadProgress.setVisibility(View.GONE);
                }
            }
        });

         */
    }

    private void getLocalImg(){
        Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
        picker.setType("image/*");
        picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        Intent destIntent = Intent.createChooser(picker, null);
        startActivityForResult(destIntent, PICKER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocalImg();
                } else {
                    Toast.makeText(firebase_upload.this, R.string.do_nothing, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                imgPath = getPath(firebase_upload.this, uri);
                System.out.println("圖片的路徑2:");
                System.out.println(uri);
                System.out.println(imgPath);

                if(!TextUtils.isEmpty(imgPath)) {
                    Toast.makeText(firebase_upload.this, imgPath, Toast.LENGTH_SHORT).show();
                    Glide.with(firebase_upload.this).load(imgPath).into(pickImg);
                } else{
                    Toast.makeText(firebase_upload.this, R.string.load_img_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
