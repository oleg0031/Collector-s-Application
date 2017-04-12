package com.olegsmirnov.collectorsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mAdaper;

    private Realm realm;

    FloatingActionButton fab;

    private TextView tvHint;
    private ImageView ivHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvHint = (TextView) findViewById(R.id.textview_hint);
        ivHint = (ImageView) findViewById(R.id.imageview_hint);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
        realm = Realm.getDefaultInstance();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
                Toast.makeText(getApplicationContext(), "Выберите изображение", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdaper = new MyAdapter(realm.where(Collection.class).findAll(), this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdaper);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (realm.where(Collection.class).count() != 0) {
            ivHint.setVisibility(View.GONE);
            tvHint.setVisibility(View.GONE);
        }
    }

    private void write(Realm realm, final String filePath, final String description, final double price) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //realm.delete(Collection.class);
                Collection collection = realm.createObject(Collection.class);
                collection.setDescription(description);
                collection.setPrice(price);
                collection.setPath(filePath);
                ivHint.setVisibility(View.GONE);
                tvHint.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            Intent in = new Intent(
                    MainActivity.this,
                    UserInputActivity.class);
            in.putExtra("filePath", path);
            startActivityForResult(in, 2);
            Toast.makeText(getApplicationContext(), "Введите информацию о предмете коллекции", Toast.LENGTH_SHORT).show();
            cursor.close();
        }
        if (requestCode == 2 && resultCode == 2 && data != null) {
            Log.d("DESCRIPTION", data.getStringExtra("description"));
            write(realm, data.getStringExtra("filePath"), data.getStringExtra("description"), data.getDoubleExtra("price", 0));
            Toast.makeText(this, "Прeдмет коллекции добавлен!", Toast.LENGTH_SHORT).show();
        }
    }
}