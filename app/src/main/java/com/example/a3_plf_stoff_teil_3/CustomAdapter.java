package com.example.a3_plf_stoff_teil_3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater inflater;
    private final int resource;
    private final List<Contact> contacts;

    public CustomAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Contact c = contacts.get(position);
        View listItem = (view==null)
                ?inflater.inflate(resource,null)
                : view;

        ((TextView)listItem.findViewById(R.id.lwID)).setText(String.valueOf(c.getContact_id()));
        ((TextView)listItem.findViewById(R.id.lwNAME)).setText(c.getContact_name());
        ((TextView)listItem.findViewById(R.id.lwNUMBER)).setText(c.getContact_TelefonNr());
        return listItem;
    }
}
