package tk.mohithaiyappa.wallela

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tk.mohithaiyappa.wallela.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {

    private var binding: ActivityContactUsBinding? = null
    private val recipient = arrayOf("wallela@protonmail.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.fabSendMail.setOnClickListener {
            if (!binding!!.etMessage.text.isNullOrEmpty() || !binding!!.etSubject.text.isNullOrEmpty()) sendMail() else {
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
        intent.putExtra(Intent.EXTRA_SUBJECT, binding!!.etSubject.text.toString())
        intent.putExtra(Intent.EXTRA_TEXT, binding!!.etMessage.text.toString())
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Choose an email client"))
        finish()
    }
}