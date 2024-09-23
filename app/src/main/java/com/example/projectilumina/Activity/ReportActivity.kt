package com.example.projectilumina.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectilumina.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconHome.setOnClickListener {
            finish()  // Fecha a ReportActivity e volta para a HomeActivity
        }
        binding.btnAdicionarDenuncia.setOnClickListener {
            val intent = Intent(this, SendReport::class.java)
            startActivity(intent)
        }
    }
}