package com.example.dreadmane

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.data.RdvData
import kotlinx.android.synthetic.main.rdv_list_item.view.*

class RdvAdapter(private val items: ArrayList<RdvData>, private val context: Context) : RecyclerView.Adapter<ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.rdv_list_item, parent, false)
        return ViewHolder(view).listen { pos, type ->
            val item = items[pos]
            Toast.makeText(context,"Date : ${item.date}",Toast.LENGTH_SHORT).show()
        }
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.planingType.text = items[position].date
        holder.rdvInfoDate.text = items[position].heure
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val planingType: TextView = view.rdv_info
    val rdvInfoDate : TextView = view.rdv_info_date


}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}