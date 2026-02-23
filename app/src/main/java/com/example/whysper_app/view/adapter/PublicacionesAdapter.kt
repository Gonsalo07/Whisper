package com.example.whysper_app.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whysper_app.R
import com.example.whysper_app.data.model.Denuncia
import com.example.whysper_app.utils.tiempoRelativo
import com.example.whysper_app.view.activity.ReporteFalsedadActivity

class PublicacionesAdapter(private val listaDenuncias: List<Denuncia>) : RecyclerView.Adapter<PublicacionesAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardAlias : TextView = itemView.findViewById(R.id.tvCardAlias)
        val tvCardCategoria : TextView = itemView.findViewById(R.id.tvCardCategoria)
        val tvCardCreadaEn : TextView = itemView.findViewById(R.id.tvCardCreadaEn)
        val tvCardTitulo : TextView = itemView.findViewById(R.id.tvCardTitulo)
        val tvCardDescripcion : TextView = itemView.findViewById(R.id.tvCardDescripcion)

        val tvCardDireccion : TextView = itemView.findViewById(R.id.tvCardDireccion)


        val imgButtonOption : ImageButton = itemView.findViewById(R.id.imgButtonOption)
        val imgButtonLike : ImageButton = itemView.findViewById(R.id.imgButtonLike)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): PublicacionesAdapter.ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_denuncia, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: PublicacionesAdapter.ViewHolder, position: Int) {
        val item = listaDenuncias[position]

        holder.tvCardAlias.text = item.aliasId.alias + "#" + item.usuarioId.id
        holder.tvCardCategoria.text = item.categoriaId.nombre
        holder.tvCardCreadaEn.text = tiempoRelativo(item.creadaEn)
        holder.tvCardTitulo.text = item.titulo
        holder.tvCardDescripcion.text = item.descripcion
        holder.tvCardDireccion.text = item.ubicacion

        holder.imgButtonOption.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReporteFalsedadActivity::class.java)
            intent.putExtra("DENUNCIA_ID", item.id)
            context.startActivity(intent)
        }


        holder.imgButtonLike.setOnClickListener {
            holder.imgButtonLike.setBackgroundResource(R.drawable.icon_like_subscribe)
        }

    }

    override fun getItemCount(): Int = listaDenuncias.size
}