package com.example.whysper_app.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whysper_app.R
import com.example.whysper_app.data.model.CategoriaDropdown
import com.example.whysper_app.data.model.Denuncia
import com.example.whysper_app.data.network.ApiClient
import com.example.whysper_app.view.adapter.PublicacionesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PublicacionesFragment : Fragment() {

    private val listaDenuncia = mutableListOf<Denuncia>()
    private lateinit var adapter: PublicacionesAdapter
    private lateinit var recyclerView : RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_publicaciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerDenuncias)
        adapter = PublicacionesAdapter(listaDenuncia)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        listarDenuncias()
    }

    private fun listarDenuncias() {

        ApiClient.apiService.obtenerDenuncias()
            .enqueue(object : Callback<List<Denuncia>> {

                override fun onResponse(
                    call: Call<List<Denuncia>>,
                    response: Response<List<Denuncia>>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let { lista ->

                            Log.d("DENUNCIAS_DEBUG", "Cantidad: ${lista.size}")
                            Log.d("DENUNCIAS_DEBUG", "Contenido: $lista")

                            listaDenuncia.clear()
                            listaDenuncia.addAll(lista)

                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Denuncia>>, t: Throwable) {
                    context?.let { ctx ->
                        Toast.makeText(ctx, "Error de conexión", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

}