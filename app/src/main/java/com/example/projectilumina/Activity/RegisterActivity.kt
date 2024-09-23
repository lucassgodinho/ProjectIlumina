package com.example.projectilumina.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectilumina.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var binding: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        binding?.buttonRegister?.setOnClickListener{
            val email:String = binding?.editTextEmail?.text.toString()
            val password: String = binding?.editTextPassword?.text.toString()
            val confirmpassword: String = binding?.editTextConfirmPassword?.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()){
                if (password == confirmpassword){
                    createUserWithEmailAndPassword(email, password)
                }else{
                    Toast.makeText(this@RegisterActivity,"Senhas incompativeis.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@RegisterActivity,"Por favor preencha os campos.",Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun createUserWithEmailAndPassword(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d(TAG, "createUserWithEmalAndPassword:Sucess")
                val user = auth.currentUser
            }else {
                Log.w(TAG, "createUserWithEmailAndPassword:Failure",task.exception)
                Toast.makeText(baseContext,"Authentication Failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object{
        private var TAG = "EmailAndPassword"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
