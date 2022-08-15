package dev.costas.vertext.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.costas.vertext.databinding.ItemAboutitemBinding
import dev.costas.vertext.models.AboutItem

class AboutItemHolder(view: View) : RecyclerView.ViewHolder(view) {
	private val binding = ItemAboutitemBinding.bind(view)

	fun render(item: AboutItem) {

		binding.aiIcon.setImageDrawable(item.icon)
		binding.aiTitle.text = item.title
		binding.aiSubtitle.text = item.subtitle
		binding.root.setOnClickListener(item.onClickListener)
	}
}