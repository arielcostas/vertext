package dev.costas.vertext.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.costas.vertext.R
import dev.costas.vertext.adapters.AboutItemAdapter
import dev.costas.vertext.databinding.FragmentAboutBinding
import dev.costas.vertext.models.AboutItem

class AboutFragment : Fragment(R.layout.fragment_about) {
	private var _binding: FragmentAboutBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAboutBinding.inflate(inflater, container, false)
		val view = binding.root

		binding.aboutRecycler.adapter = AboutItemAdapter(getAboutItems())
		binding.aboutRecycler.layoutManager = LinearLayoutManager(context)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.aboutToolbar.setNavigationOnClickListener { v: View? -> requireActivity().onBackPressed() }
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun getAboutItems(): List<AboutItem> {
		val items: MutableList<AboutItem> = ArrayList()
		items.add(
			AboutItem(
				getString(R.string.about_sourcecode),
				"https://framagit.org/arielcostas/vertext",
				AppCompatResources.getDrawable(requireContext(), R.drawable.ic_code_24)!!
			) {
				val website = Uri.parse("https://framagit.org/arielcostas/vertext")
				val intent = Intent(Intent.ACTION_VIEW, website)
				startActivity(intent)
			})
		return items.toList()
	}
}