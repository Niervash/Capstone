package com.bangkit.huggingpet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.databinding.ActivityConsultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ConsultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Consultation"
        auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity    ::class.java))
            finish()
            return
        }

        db = FirebaseDatabase.getInstance()
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        binding.sendButton.setOnClickListener {
            val friendlyMessage = Message.obtain().apply {
                obj = binding.messageEditText.text.toString()
            }

            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, getString(R.string.send_error) + error.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.send_success), Toast.LENGTH_SHORT).show()
                }
            }
            binding.messageEditText.setText("")
        }
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}
