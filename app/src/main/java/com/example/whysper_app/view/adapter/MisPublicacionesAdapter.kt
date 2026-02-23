package com.example.whysper_app.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whysper_app.R
import com.example.whysper_app.data.model.Denuncia
import com.example.whysper_app.data.model.DenunciaUser
import com.example.whysper_app.utils.tiempoRelativo

class MisPublicacionesAdapter(private val listaDenuncias: List<DenunciaUser>) : RecyclerView.Adapter<MisPublicacionesAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardCategoria : TextView = itemView.findViewById(R.id.tvMyCardCategoria)
        val tvCardCreadaEn : TextView = itemView.findViewById(R.id.tvMyCardCreadaEn)
        val tvCardTitulo : TextView = itemView.findViewById(R.id.tvMyCardTitulo)
        val tvCardDescripcion : TextView = itemView.findViewById(R.id.tvMyCardDescripcion)
        val tvCardDireccion : TextView = itemView.findViewById(R.id.tvMyCardDireccion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MisPublicacionesAdapter.ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mi_denuncia, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: MisPublicacionesAdapter.ViewHolder,
        position: Int
    ) {
        val item = listaDenuncias[position]

        holder.tvCardCategoria.text = item.categoriaNombre
        holder.tvCardCreadaEn.text = tiempoRelativo(item.creadaEn)
        holder.tvCardTitulo.text = item.titulo
        holder.tvCardDescripcion.text = item.descripcion
        holder.tvCardDireccion.text = item.ubicacion
    }

    override fun getItemCount(): Int = listaDenuncias.size

}