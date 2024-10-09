package com.example.projectilumina.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectilumina.R
import com.example.projectilumina.data.Denuncia
import com.example.projectilumina.databinding.ActivityHomeBinding
import com.example.projectilumina.databinding.ActivityReportBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ReportActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var denunciaAdapter: DenunciaAdapter
    private lateinit var denunciaList: ArrayList<Denuncia>
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdicionarDenuncia.setOnClickListener {
            val intent = Intent(this, SendReportActivity::class.java)
            startActivity(intent)
        }
        binding.iconHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Inicializa o RecyclerView e a lista de denúncias
        recyclerView = findViewById(R.id.recyclerViewDenuncias)
        recyclerView.layoutManager = LinearLayoutManager(this)
        denunciaList = ArrayList()
        denunciaAdapter = DenunciaAdapter(denunciaList)

        recyclerView.adapter = denunciaAdapter

        // Inicializa o Firebase Database
        database = FirebaseDatabase.getInstance().getReference("denuncias")

        // Carrega as denúncias
        carregarDenuncias()

    }

    private fun carregarDenuncias() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                denunciaList.clear() // Limpa a lista antes de adicionar novos itens
                for (denunciaSnapshot in snapshot.children) {
                    val denuncia = denunciaSnapshot.getValue(Denuncia::class.java)
                    denuncia?.let { denunciaList.add(it) } // Adiciona denúncia à lista
                }
                denunciaAdapter.notifyDataSetChanged() // Atualiza o adaptador
            }

            override fun onCancelled(error: DatabaseError) {
                // Lida com erros
            }
        })
    }
}