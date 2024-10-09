package com.example.projectilumina.Activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectilumina.R
import com.example.projectilumina.data.Denuncia
import com.example.projectilumina.databinding.ItemDenunciaBinding

class DenunciaAdapter(private val denunciaList: List<Denuncia>) :
    RecyclerView.Adapter<DenunciaAdapter.DenunciaViewHolder>() {

    inner class DenunciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCidade = itemView.findViewById<TextView>(R.id.tvCidade)
        private val tvBairro = itemView.findViewById<TextView>(R.id.tvBairro)
        private val tvTipoManutencao = itemView.findViewById<TextView>(R.id.tvTipoManutencao)
        private val tvDataHora = itemView.findViewById<TextView>(R.id.tvDataHora)
        private val tvDescricao = itemView.findViewById<TextView>(R.id.tvDescricao)
        private val tvLocalizacao = itemView.findViewById<TextView>(R.id.tvLocalizacao)
        private val tvImagem = itemView.findViewById<ImageView>(R.id.tvImagem)

        fun bind(denuncia: Denuncia) {
            tvCidade.text = "Cidade: ${denuncia.cidade}"
            tvBairro.text = "Bairro: ${denuncia.bairro}"
            tvTipoManutencao.text = "Tipo Manutenção: ${denuncia.tipoManutencao}"
            tvDataHora.text = "Data e Hora: ${denuncia.dataHora}"
            tvDescricao.text = "Descrição: ${denuncia.descricao}"
            tvLocalizacao.text = "Localização: ${denuncia.latitude}, ${denuncia.longitude}"

            // Carregar a imagem se existir
            if (denuncia.imagemUrl != null) {
                // Use uma biblioteca como Glide ou Picasso para carregar a imagem
                // Glide.with(itemView.context).load(denuncia.imagemUrl).into(tvImagem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenunciaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_denuncia, parent, false)
        return DenunciaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DenunciaViewHolder, position: Int) {
        holder.bind(denunciaList[position])
    }

    override fun getItemCount() = denunciaList.size
}
