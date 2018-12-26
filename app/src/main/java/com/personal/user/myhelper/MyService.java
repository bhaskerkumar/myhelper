package com.personal.user.myhelper;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashMap;


public class MyService extends IntentService
{
    public MyService()
    {
        super(MyService.class.getSimpleName());
    }
   StringBuilder msg = new StringBuilder();
   String number;
   String name;
    @Override
    protected void onHandleIntent(Intent i) {
        if (i != null) {
            number = i.getStringExtra("number");
            name = i.getStringExtra("contact");
            System.out.println("Service is Started and number is" + number + "  name is " + name + "");
            loadContacts(name);
            System.out.println(msg.toString()+" is in service");
        }
    }


    private void loadContacts(String cname)
    {
        String phone = "";
        HashMap<String, String> s = new HashMap<>();
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                // System.out.println(name+"  "+hasPhoneNumber+" ");
                if (hasPhoneNumber > 0) {
                    Cursor c2 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{id}, null
                    );
                    while (c2.moveToNext()) {
                        String phoneNumber = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // s.append("Contact: ").append(name).append(", Phone Number: ").append(phoneNumber).append("\n\n");
                        s.put(phoneNumber, name);
                        //   System.out.println(name+"  "+phoneNumber+" ");
                    }
                    c2.close();
                }
            }
        }
        for (HashMap.Entry<String, String> entry : s.entrySet()) {
            //    System.out.println("Key = " + entry.getKey() +
            //          ", Value = " + entry.getValue());
            if (entry.getValue().equals(cname)) {
                phone += entry.getKey();
                phone += " ";
            }
        }
        c.close();
        System.out.println(phone + " " + cname);
        if (!phone.equals(""))
            msg.append(cname + " " + phone);
        else
            msg.append("Name not found in the contact list");
        System.out.println(msg.toString());
    }
}