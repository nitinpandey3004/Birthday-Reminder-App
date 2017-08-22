package com.nitin.birthdayremainder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    EditText etName, etDate, etPhone;
    Button btnUploadPhoto, btnSave, btnDelete;
    ImageView ivPhoto;
    String imagePath, date = "";
    DatabaseHandler databaseHandler;
    ImageButton imageButton;
    Intent GalIntent, CamIntent, CropIntent;
    Uri gal_uri , cam_uri;
    String userChoosenTask;

    int year_x = 0, month_x, day_x;
    static final int DIALOG_ID = 0;

    Context context ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        databaseHandler = new DatabaseHandler(AddFriendActivity.this);
        final int id = getIntent().getIntExtra("id", -1);

        final Calendar cal;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cal = Calendar.getInstance();
            year_x = cal.get(Calendar.YEAR);
            month_x = cal.get(Calendar.MONTH);
            day_x = cal.get(Calendar.DAY_OF_MONTH);
        }


        etName = (EditText) findViewById(R.id.etName);
        etDate = (EditText) findViewById(R.id.etDate);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        context = getApplicationContext();

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 123);

                */
                selectImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String date = etDate.getText().toString();
                String phone = etPhone.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_LONG).show();
                    etName.setError("Enter Correct name");
                    etName.requestFocus();
                    return;
                }
                if (date.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Date", Toast.LENGTH_LONG).show();
                    etDate.setError("Enter correct date");
                    etDate.requestFocus();
                    return;
                }
                if (phone.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter Valid  Phone No", Toast.LENGTH_LONG).show();
                    etPhone.setError("Enter correct phone number");
                    etPhone.requestFocus();
                    return;
                }

                if (year_x != 0) {

                    date = day_x + "-" + month_x + "-" + year_x;
                }

                //Toast.makeText(getApplicationContext(), "id is :" + date, Toast.LENGTH_LONG).show();

                Friend friend = new Friend();
                friend.setName(name);
                friend.setDob(date);
                friend.setPhone(phone);
                friend.setPhoto(imagePath);

                databaseHandler.addFriend(friend);

                Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);

                startActivity(intent);
                finish();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseHandler.delete(id);

                Intent i = new Intent(AddFriendActivity.this, MainActivity.class);

                startActivity(i);
                finish();

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_ID);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerlisterner, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerlisterner
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {

            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            //Toast.makeText(getApplicationContext(), year_x + "-" + month_x + "-" + day_x, Toast.LENGTH_LONG).show();
            etDate.setText(day_x + "-" + month_x + "-" + year_x);
        }

    };


    public void GalleryOpen() {

        GalIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent, "Select Image from Gallery"), 2);
    }

    public void CameraOpen() {


        CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(CamIntent, 1);

        //Toast.makeText(getApplicationContext(), "next step 1", Toast.LENGTH_LONG).show();


/*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Wysie_Soh: Create path for temp file
        cam_uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "tmp_contact_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cam_uri);

        try {
            intent.putExtra("return-data", true);
            //PICK_FROM_CAMERA 1101
            startActivityForResult(intent, 101);
        } catch (ActivityNotFoundException e) {
            //Do nothing for now
        }*/
        /*
        CamIntent = new Intent("com.android.camera.action.CROP");
        CamIntent.setClassName("com.android.camera", "com.android.camera.CropImage");

        CamIntent.setData(mImageCaptureUri);
        CamIntent.putExtra("outputX", 96);
        CamIntent.putExtra("outputY", 96);
        CamIntent.putExtra("aspectX", 1);
        CamIntent.putExtra("aspectY", 1);
        CamIntent.putExtra("scale", true);
        CamIntent.putExtra("return-data", true);
        startActivityForResult(CamIntent, 102);*/


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
/*
        if (resultCode == RESULT_OK && requestCode == 123) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();

                imagePath = destination.getPath();
                ivPhoto.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
            } catch (Exception e) {

            }


        }
        */

        if (requestCode == 1) {

            if (data != null) {
                //Toast.makeText(getApplicationContext(), "next step ", Toast.LENGTH_LONG).show();
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = bundle.getParcelable("data");
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destnination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destnination.createNewFile();
                    fo = new FileOutputStream(destnination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    imagePath = destnination.getPath();
                    ivPhoto.setImageBitmap(bitmap);
                    Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                }


            }
        } else if (requestCode == 2) {

            if (data != null) {
                //Toast.makeText(getApplicationContext(), "next step 786 ", Toast.LENGTH_LONG).show();

                gal_uri = data.getData();
                CropImage(gal_uri);

            }
        }
        if (requestCode == 111) {

            if (data != null) {
                //Toast.makeText(getApplicationContext(), "next step 78677 ", Toast.LENGTH_LONG).show();
                cam_uri = data.getData();
                CropImage(cam_uri);

            }
        }
        //setMap.setImageResource(R.drawable.usamap);

/*
        switch (requestCode) {

            case 102: {
                //Wysie_Soh: After a picture is taken, it will go to PICK_FROM_CAMERA, which will then come here
                //after the image is cropped.

                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");

                    //mPhoto = photo;
                    //mPhotoChanged = true;
                    ivPhoto.setImageBitmap(photo);
                    //setPhotoPresent(true);
                }

                //Wysie_Soh: Delete the temporary file
                File f = new File(cam_uri.getPath());
                if (f.exists()) {
                    f.delete();
                }

                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(ivPhoto, InputMethodManager.SHOW_IMPLICIT);

                break;
            }

            case 101: {
                //Wysie_Soh: After an image is taken and saved to the location of mImageCaptureUri, come here
                //and load the crop editor, with the necessary parameters (96x96, 1:1 ratio)

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setClassName("com.android.camera", "com.android.camera.CropImage");

                intent.setData(cam_uri);
                intent.putExtra("outputX", 96);
                intent.putExtra("outputY", 96);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                //CROP_FROM_CAMERA 102
                startActivityForResult(intent, 102);

                break;

            }
        }
        */


    }

    private void CropImage(Uri uri) {

        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("ScaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException ex) {

            ex.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        Intent back = new Intent(AddFriendActivity.this, MainActivity.class);
        startActivity(back);
        finish();
    }

    ///
/*
    public static Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    context.getString(R.string.pick_image_intent_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }
    */
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddFriendActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    //if (result)
                        //cameraIntent();
                        CameraOpen();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        //galleryIntent();
                        GalleryOpen();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }


        });
        builder.show();
    }
/*
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }

    /*
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1)
                    onSelectFromGalleryResult(data);
                else if (requestCode == 0)
                    onCaptureImageResult(data);
            }
        }
    */
  /*
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivPhoto.setImageBitmap(bm);
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivPhoto.setImageBitmap(thumbnail);
    }

*/
}


