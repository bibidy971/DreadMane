package com.example.dreadmane

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreadmane.data.RdvData
import kotlinx.android.synthetic.main.rdv_list_item.view.*

class RdvAdapterProfil(private val items: ArrayList<RdvData>, private val context: Context, private val itemClickListener: OnItemClickListenerProfil) : RecyclerView.Adapter<ViewHolderProfil>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProfil {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rdv_list_item, parent, false)
        return ViewHolderProfil(view)
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolderProfil, position: Int) {
        if (items[position].client == null){
            holder.layoutEtat.setBackgroundColor(Color.GREEN)
        }else {
            holder.layoutEtat.setBackgroundColor(Color.RED)
        }

        holder.planingType.text = items[position].date
        holder.rdvInfoDate.text = items[position].heure

        val rdv = items[position]
        holder.bind(rdv,itemClickListener)
    }

}

class ViewHolderProfil (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val planingType: TextView = view.rdv_info
    val rdvInfoDate : TextView = view.rdv_info_date
    val layoutEtat: LinearLayout = view.layout_rdv_etat


    fun bind(rdv : RdvData, clickListener: OnItemClickListenerProfil){
        itemView.setOnClickListener{
            clickListener.onItemClicked(rdv)
        }
    }

}


interface OnItemClickListenerProfil{
    fun onItemClicked(rdvData: RdvData)
}
