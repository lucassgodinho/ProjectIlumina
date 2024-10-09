package com.example.projectilumina.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projectilumina.data.Denuncia
import com.example.projectilumina.databinding.ActivitySendReportBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SendReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendReportBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: Location? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa a referência do Firebase
        database = FirebaseDatabase.getInstance().getReference("denuncias")
        storage = FirebaseStorage.getInstance().reference

        // Inicializa o FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verifica permissões e obtém a localização
        obterLocalizacao()

        // Listener para selecionar imagem
        binding.btnSelecionarImagem.setOnClickListener {
            selecionarImagem()
        }

        // Listener para enviar denúncia
        binding.btnConcluir.setOnClickListener {
            enviarDenuncia()
        }

        binding.iconDenuncia.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
        binding.iconHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            userLocation = location
            if (location != null) {
                Toast.makeText(this, "Localização obtida: ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selecionarImagem() {
        // Intent para selecionar imagem da galeria
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            Toast.makeText(this, "Imagem selecionada!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarDenuncia() {
        val cidade = binding.edtCidade.text.toString().trim()
        val bairro = binding.edtBairro.text.toString().trim()
        val tipoManutencao = binding.edtTipoManutencao.text.toString().trim()
        val dataHora = binding.edtDataHora.text.toString().trim()
        val descricao = binding.edtDescricao.text.toString().trim()

        // Validação de campos
        if (cidade.isEmpty() || bairro.isEmpty() || tipoManutencao.isEmpty() || dataHora.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val denunciaId = database.push().key ?: return // Garantindo que o denunciaId não seja nulo
        val latitude = userLocation?.latitude ?: 0.0
        val longitude = userLocation?.longitude ?: 0.0

        // Verifica se há uma imagem selecionada
        if (imageUri != null) {
            val filePath = storage.child("images/${denunciaId}.jpg")
            filePath.putFile(imageUri!!)
                .addOnSuccessListener {
                    filePath.downloadUrl.addOnSuccessListener { uri ->
                        val denuncia = Denuncia(
                            id = denunciaId,
                            cidade = cidade,
                            bairro = bairro,
                            tipoManutencao = tipoManutencao,
                            dataHora = dataHora,
                            descricao = descricao,
                            latitude = latitude,
                            longitude = longitude,
                            imagemUrl = uri.toString() // URL da imagem
                        )
                        salvarDenuncia(denuncia)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao enviar imagem.", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Se não há imagem, cria a denúncia sem a URL da imagem
            val denuncia = Denuncia(
                id = denunciaId,
                cidade = cidade,
                bairro = bairro,
                tipoManutencao = tipoManutencao,
                dataHora = dataHora,
                descricao = descricao,
                latitude = latitude,
                longitude = longitude,
                imagemUrl = null // Se não há imagem, passar null
            )
            salvarDenuncia(denuncia)
        }
    }

    private fun salvarDenuncia(denuncia: Denuncia) {
        denuncia.id?.let { id ->
            database.child(id).setValue(denuncia).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Denúncia enviada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao enviar a denúncia.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}