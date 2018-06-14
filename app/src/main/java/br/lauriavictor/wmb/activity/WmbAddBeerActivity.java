package br.lauriavictor.wmb.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.SQLiteHelper;

public class WmbAddBeerActivity extends AppCompatActivity {

    EditText mName, mNota, mTipo;
    Button mButtonAddBeer, mButtonListBeer;
    ImageView mPhoto;
    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper mSQLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_add_beer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Inserir Cerveja");

        mName = (EditText) findViewById(R.id.editTextName);
        mNota = (EditText) findViewById(R.id.editTextNota);
        mTipo = (EditText) findViewById(R.id.editTextTipo);
        mPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        mButtonAddBeer = (Button) findViewById(R.id.buttonAddBeer);
        mButtonListBeer = (Button) findViewById(R.id.buttonListBeer);

        //Criando database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        //Criando tabela
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR, " +
                "nota VARCHAR, " +
                "tipo VARCHAR, " +
                "image BLOB)");

        mButtonAddBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateFields();
            }
        });

        mButtonListBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WmbBeerListActivity.class));
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cógido para escolher imagem aqui.
                ActivityCompat.requestPermissions(
                        WmbAddBeerActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //gallery intent
                Intent WmbListActivity = new Intent(Intent.ACTION_GET_CONTENT);
                WmbListActivity.setType("image/*");
                startActivityForResult(WmbListActivity, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Não tem permissão", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                mPhoto.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean validateFields() {
        if (mName.getText().toString().trim().equals("")) {
            mName.setError("Informe nome para a cerveja!");
            mName.setHint("Exemplo: Heineken, Budweiser, Skol");
            mName.requestFocus();
            return false;
        } else if (mNota.getText().toString().trim().equals("")) {
            mNota.setError("Informe uma classifação!");
            mNota.setHint("Exemplo: 1, 2, 3.. ..8, 9, 10");
            mNota.requestFocus();
            return false;
        } else if (mTipo.getText().toString().trim().equals("")) {
            mTipo.setError("Informe um tipo!");
            mTipo.setHint("Exemplo: Ipa, Pale Ale, Pilsen, Chopp");
            mTipo.requestFocus();
            return false;
        }

        try {
            mSQLiteHelper.insertData(mName.getText().toString().trim(),
                                     mNota.getText().toString().trim(),
                                     mTipo.getText().toString().trim(),
                                     imageViewToByte(mPhoto));
                Toast.makeText(WmbAddBeerActivity.this, "Cerveja adicionada!", Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(WmbAddBeerActivity.this, "Erro ao adicionar, insira uma foto.", Toast.LENGTH_LONG).show();
            }

            return true;
        }
    }
