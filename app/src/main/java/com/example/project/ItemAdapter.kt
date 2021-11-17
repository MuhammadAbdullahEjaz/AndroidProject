package com.example.project

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.ItemBinding
import com.example.project.network.PropertyDC
import com.squareup.picasso.Picasso
import java.lang.Exception

class ItemAdapter(private val data: List<PropertyDC>):RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private lateinit var binding: ItemBinding
    class ViewHolder(private  val view: View): RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.image_title)
        val imageView: ImageView = view.findViewById(R.id.image_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = data[position]
        holder.textView.setTextColor(Color.parseColor("#ff0000ff"))
        holder.textView.text = property.id.toString()
        val picasso = Picasso.get()
        picasso.setIndicatorsEnabled(true)
        Log.d("fetch","${property.img_src}")
        picasso
            .load(property.img_src)
            .placeholder(R.drawable.place_holder)
            .error(R.drawable.error_image)
            .into(holder.imageView, object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.d("fetch", "image loaded")
                }

                override fun onError(e: Exception?) {
                    Log.d("fetch", "image loading failed ${e}")
                }

            })
    }

    override fun getItemCount(): Int {
        return data.size
    }
}