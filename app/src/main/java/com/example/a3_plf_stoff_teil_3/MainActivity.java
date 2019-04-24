package com.example.a3_plf_stoff_teil_3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    CustomAdapter adapter;
    Button button;
    CheckBox checkBox;
    int LOCATION_PERMISSION_CODE = 1;
    List<Contact> contacts;
    private final String CONTACT_KEY = "CONTACTS";
    private final String BUTTON_KEY = "BUTTON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.b);
        checkBox = findViewById(R.id.checkBox);
        button.setOnClickListener(l -> requestPermission());
        contacts = new ArrayList<>();

        //Checkbox stayes checked after rotating, for some reason?
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<String> stringList = new ArrayList<>();
        for(Contact c : contacts){
            stringList.add(c.toString());
        }
        outState.putStringArrayList(CONTACT_KEY,stringList);
        outState.putString(BUTTON_KEY,String.valueOf(button.isEnabled()));


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ArrayList<String> cont = savedInstanceState.getStringArrayList(CONTACT_KEY);
        for(String c : cont){
            String[] cc = c.split(";");
            contacts.add(new Contact(cc[0],cc[1],Integer.valueOf(cc[2])));
        }
        adapter = new CustomAdapter(this,R.layout.my_listview_layout,contacts);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        button.setEnabled(Boolean.valueOf(String.valueOf(savedInstanceState.get(BUTTON_KEY))));
    }

    private void requestPermission() {
        //ContextCompat.checkSelfPermission checks if the user already granted the permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            readContacts();
            checkCheckbox();
        } else {
            //Activity.requestPermisson asks the User for permission, onRequestPermissionsResult is called, if the Permission is granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            }
        }
    }

    private void readContacts() {
        Cursor cursor = null;
        ContentResolver contentResolver = getContentResolver();

        cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI
                , null
                , null
                , null
                , null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = "";

                boolean hasNumber = (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) == 1 ? true : false);

                if (hasNumber) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();
                }
                contacts.add(new Contact(name, number, Integer.parseInt(id)));
            }
        }

        adapter = new CustomAdapter(getApplicationContext(), R.layout.my_listview_layout, contacts);
        listView.setAdapter(adapter);

    }

    private void checkCheckbox() {
        checkBox.setChecked(true);
        button.setEnabled(false);
    }
}
