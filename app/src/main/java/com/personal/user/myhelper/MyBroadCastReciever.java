package com.personal.user.myhelper;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class MyBroadCastReciever extends BroadcastReceiver
{
        StringBuilder msg= new StringBuilder();
        final SmsManager SM=SmsManager.getDefault();
        private AudioManager myAm;
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Toast.makeText(context,"Broadcast Receiver Triggered", Toast.LENGTH_SHORT).show();
            final Bundle bundle=intent.getExtras();
            try
            {
                if (bundle != null)
                {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++)
                    {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                        String [] words=message.split("\\s");
                        SharedPreferences pre= context.getSharedPreferences("passcode",Context.MODE_PRIVATE);
                        final String DEFAULT="N/A";
                        String name=pre.getString("name",DEFAULT);
                        String password=pre.getString("key",DEFAULT);
                        if(words[0].equals(password.trim()))
                        {
                                System.out.println(words[1].equals("nor"));
                                if(words[1].equals("nor"))
                                {
                                    myAm=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                    myAm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                    int duration=Toast.LENGTH_LONG;
                                    Toast t = Toast.makeText(context,"profile change",duration);
                                    t.show();
                                }
                                else if(words[1].equals("location"))
                                {
                                    Intent intent1= new Intent(context, MyService2.class);
                                    intent1.putExtra("number",senderNum);
                                    context.startService(intent1);
                                    int duration=Toast.LENGTH_LONG;
                                    Toast t = Toast.makeText(context,"send location",duration);
                                    t.show();
                                }
                                else if(words[1].equals("lock"))
                                {
                                    int duration=Toast.LENGTH_LONG;
                                    Toast t = Toast.makeText(context,"lock the phone",duration);
                                    t.show();
                                }
                                else
                                {
                                    StringBuilder contact= new StringBuilder(words[1]);
                                    for(int it=2; it<words.length; it++)
                                        contact.append(" "+words[i]);
                                    contact.trimToSize();
                                    loadContacts(new String(contact),context);
                                    System.out.println(msg+" " +contact+ " in broadcast receiver");
                                    SM.sendTextMessage(senderNum, null,msg.toString(), null, null );
                                }
                        }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }


    private void loadContacts(String cname,Context context)
    {
        String phone = "";
        HashMap<String, String> s = new HashMap<>();
        ContentResolver cr = context.getContentResolver();
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