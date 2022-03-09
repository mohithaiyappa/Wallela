package tk.mohithaiyappa.wallela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactUsActivity extends AppCompatActivity {


    private String[] recipient ={"wallela@protonmail.com"};
    private EditText etSubject,etMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        FloatingActionButton fab_mail = findViewById(R.id.fab_send_mail);
        etSubject=findViewById(R.id.editText);
        etMessage=findViewById(R.id.editText2);
        fab_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMessage.getText().toString().trim()!=null||etSubject.getText().toString().trim()!=null)
                    sendMail();
                else {
                    Toast.makeText(ContactUsActivity.this, "Subject or Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.putExtra(Intent.EXTRA_SUBJECT,etSubject.getText().toString() );
        intent.putExtra(Intent.EXTRA_TEXT,etMessage.getText().toString());

        intent.setType("message/rfc822");

        startActivity(Intent.createChooser(intent, "Choose an email client"));
        finish();
    }


}
