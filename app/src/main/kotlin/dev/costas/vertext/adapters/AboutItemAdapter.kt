package dev.costas.vertext.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.costas.vertext.R
import dev.costas.vertext.holders.AboutItemHolder
import dev.costas.vertext.models.AboutItem

class AboutItemAdapter(private val dataSet: List<AboutItem>) :
	RecyclerView.Adapter<AboutItemHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutItemHolder {
		val view =
			LayoutInflater.from(parent.context).inflate(R.layout.item_aboutitem, parent, false)
		return AboutItemHolder(view)
	}

	override fun onBindViewHolder(holder: AboutItemHolder, position: Int) {
		holder.render(dataSet[position])
	}

	override fun getItemCount(): Int = dataSet.size
}