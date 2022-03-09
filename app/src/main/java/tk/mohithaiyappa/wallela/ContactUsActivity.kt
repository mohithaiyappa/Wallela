package tk.mohithaiyappa.wallela

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ContactUsActivity : AppCompatActivity() {
    private val recipient = arrayOf("wallela@protonmail.com")
    private var etSubject: EditText? = null
    private var etMessage: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        val fab_mail = findViewById<FloatingActionButton>(R.id.fab_send_mail)
        etSubject = findViewById(R.id.editText)
        etMessage = findViewById(R.id.editText2)
        fab_mail.setOnClickListener {
            if (etMessage?.getText().toString().trim { it <= ' ' } != null || etSubject?.getText()
                    .toString().trim { it <= ' ' } != null) sendMail() else {
                Toast.makeText(
                    this@ContactUsActivity,
                    "Subject or Message cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendMail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, recipient)
        intent.putExtra(Intent.EXTRA_SUBJECT, etSubject!!.text.toString())
        intent.putExtra(Intent.EXTRA_TEXT, etMessage!!.text.toString())
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Choose an email client"))
        finish()
    }
}