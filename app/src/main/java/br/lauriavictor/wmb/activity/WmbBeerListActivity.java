package br.lauriavictor.wmb.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.adapter.BeerListAdapter;
import br.lauriavictor.wmb.model.Beer;

public class WmbBeerListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Beer> mBeers;
    BeerListAdapter mAdapter = null;
    ImageView imageViewIcon;
    final int REQUEST_CODE_GALLERY = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sua Lista");

        mListView = (ListView) findViewById(R.id.listViewBeer);
        mBeers = new ArrayList<>();
        mAdapter = new BeerListAdapter(this, R.layout.item_list, mBeers);
        mListView.setAdapter(mAdapter);

        Cursor cursor = WmbAddBeerActivity.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mBeers.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String note = cursor.getString(2);
            String type = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            mBeers.add(new Beer(id, name, note, type, image));
        }
        mAdapter.notifyDataSetChanged();

        if(mBeers.size() == 0) {
            Toast.makeText(this, "Sem cervejas para listar!", Toast.LENGTH_LONG).show();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Atualizar", "Excluir"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(WmbBeerListActivity.this);
                dialog.setTitle("Escolha uma ação");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            //update
                            Cursor cursor1 = WmbAddBeerActivity.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> integers = new ArrayList<Integer>();
                            while(cursor1.moveToNext()) {
                                integers.add(cursor1.getInt(0));

                            }
                            //show update dialog
                            showDialogUpdate(WmbBeerListActivity.this, integers.get(position));

                        } else if(which == 1) {
                            //delete
                            Cursor cursor2 = WmbAddBeerActivity.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> integers2 = new ArrayList<Integer>();
                            while(cursor2.moveToNext()) {
                                integers2.add(cursor2.getInt(0));
                            }
                            showDialogDelete(integers2.get(position));
                        }
                    }
                });
                dialog.show();
                //return true;
            }
        });
    }

    private void showDialogDelete(final int id) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(WmbBeerListActivity.this);
        dialogDelete.setTitle("Atenção!");
        dialogDelete.setMessage("Deseja excluir este item?");
        dialogDelete.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    WmbAddBeerActivity.mSQLiteHelper.deleteData(id);
                    Toast.makeText(WmbBeerListActivity.this, "Item excluido!", Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    Log.e("Erro ao excluir", e.getMessage());
                }

                updateRecordList();
            }
        });

        dialogDelete.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Atualizar Cerveja");

        imageViewIcon = dialog.findViewById(R.id.imaViewDialog);
        final EditText editTextName = dialog.findViewById(R.id.editTextNameDialog);
        final EditText editTextNote = dialog.findViewById(R.id.editTextNoteDialog);
        final EditText editTextType = dialog.findViewById(R.id.editTextTypeDialog);
        Button buttonUpdate = dialog.findViewById(R.id.buttonUpdateDialog);

        //set width and height of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        //in update dialog click image view to update image
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check external storage permission
                ActivityCompat.requestPermissions(
                        WmbBeerListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WmbAddBeerActivity.mSQLiteHelper.updateData(editTextName.getText().toString().trim(),
                                                                editTextNote.getText().toString().trim(),
                                                                editTextType.getText().toString().trim(),
                                                                WmbAddBeerActivity.imageViewToByte(imageViewIcon),
                                                                position);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Item atualizado!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("Erro ao atualizar", e.getMessage());
                }

                updateRecordList();
            }
        });
    }

    private void updateRecordList() {
        //get all data from database
        Cursor cursor = WmbAddBeerActivity.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mBeers.clear();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String note = cursor.getString(2);
            String type = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            mBeers.add(new Beer(id, name, note, type, image));
        }
        mAdapter.notifyDataSetChanged();
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

                imageViewIcon.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
