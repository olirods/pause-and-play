package com.example.pauseandplay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MyAdapter(private val imageModelArrayList: MutableList<LyricLine>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var imgView: ImageView
        var txtMsg: TextView

        init {
            imgView = layout.findViewById<View>(R.id.icon) as ImageView
            txtMsg = layout.findViewById<View>(R.id.firstLine) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = imageModelArrayList[position]

        holder.imgView.setImageResource(info.getImage_drawables())
        holder.txtMsg.setText(info.getNames())
        holder.txtMsg.setOnClickListener { v ->
            val snackbar =
                Snackbar.make(v, "You clicked on ${info.getNames()}", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }


    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }
}
