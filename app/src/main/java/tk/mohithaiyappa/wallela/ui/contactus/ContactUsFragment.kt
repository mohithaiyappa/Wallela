package tk.mohithaiyappa.wallela.ui.contactus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import tk.mohithaiyappa.wallela.databinding.ActivityContactUsBinding

class ContactUsFragment : Fragment() {

    private var binding: ActivityContactUsBinding? = null
    private val recipient = arrayOf("wallela@protonmail.com")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)

        binding!!.root.setOnClickListener(null)

        binding!!.fabSendMail.setOnClickListener {
            if (!binding!!.etMessage.text.isNullOrEmpty() || !binding!!.etSubject.text.isNullOrEmpty()) sendMail() else {
                Toast.makeText(
                    requireContext(),
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
        // todo finish()
    }
}