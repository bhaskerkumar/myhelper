package com.personal.user.myhelper;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

  private EditText e1;
  private Button b1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        e1=(EditText)findViewById(R.id.e1);
        b1=(Button)findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                String s1=e1.getText().toString();
                SharedPreferences prefs= getSharedPreferences("passcode", Context.MODE_PRIVATE);  //to store the data
                SharedPreferences.Editor e=prefs.edit();
                e.putString("name","habib");
                e.putString("key",s1);
                e.commit();

        Toast.makeText(MainActivity.this,"Passcode saved Successfully ", Toast.LENGTH_SHORT).show();
                SharedPreferences pre= getSharedPreferences("passcode",Context.MODE_PRIVATE);
                final String DEFAULT="N/A";
                String name=pre.getString("name",DEFAULT);
                String password=pre.getString("key",DEFAULT);
          //      System.out.println(password+" "+name+" ");
            }
        });

    }
}
