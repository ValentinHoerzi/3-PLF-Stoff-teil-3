package com.example.a3_plf_stoff_teil_3;

public class Contact {
    private final String contact_name;
    private final String contact_TelefonNr;
    private final int contact_id;

    public Contact(String contact_name, String contact_TelefonNr, int contact_id) {
        this.contact_name = contact_name;
        this.contact_TelefonNr = contact_TelefonNr;
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_TelefonNr() {
        return contact_TelefonNr;
    }

    public int getContact_id() {
        return contact_id;
    }

    @Override
    public String toString() {
        return contact_name+";"+contact_TelefonNr+";"+contact_id;
    }
}
