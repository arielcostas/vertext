package dev.costas.vertext.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import dev.costas.vertext.FragmentChangeListener
import dev.costas.vertext.R
import dev.costas.vertext.databinding.FragmentMainBinding
import dev.costas.vertext.viewmodels.ContentViewModel
import java.io.IOException
import java.io.InputStream
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {
	private var textView: TextView? = null

	private var _binding: FragmentMainBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentMainBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		textView = binding.principal
		binding.mainToolbar.setOnMenuItemClickListener { item: MenuItem ->
			if (item.itemId == R.id.mainmenu_abrir) {
				openFile()
				true
			} else if (item.itemId == R.id.mainmenu_settings) {
				// Change to the SettingsFragment
				(activity as FragmentChangeListener?)!!.setFragment(SettingsFragment())
				true
			} else if (item.itemId == R.id.mainmenu_about) {
				(activity as FragmentChangeListener?)!!.setFragment(AboutFragment())
				true
			} else {
				false
			}
		}
		val intent = requireActivity().intent
		val action = intent.action
		val type = intent.type
		val uri: Uri?
		when (action) {
			Intent.ACTION_EDIT, Intent.ACTION_VIEW -> {
				val entrada = intent.getStringExtra(Intent.EXTRA_TEXT)
				uri = Uri.parse(intent.getStringExtra(Intent.EXTRA_ORIGINATING_URI))
				setTitle(getFilenameFromUri(uri))
				setContent(Html.escapeHtml(entrada))
			}
			Intent.ACTION_SEND -> {
				uri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
				val text = intent.getStringExtra(Intent.EXTRA_TEXT)
				if (uri != null) {
					try {
						setTitle(getFilenameFromUri(uri))
						val afd = requireActivity().contentResolver.openTypedAssetFileDescriptor(
							uri,
							type!!,
							null
						)
						val sb = StringBuilder()
						val scanner = Scanner(afd!!.createInputStream())
						while (scanner.hasNextLine()) {
							sb.append(scanner.nextLine())
							sb.append("\n")
						}
						setContent(sb.toString())
					} catch (e: IOException) {
						setContent(Arrays.toString(e.stackTrace))
						return
					}
				} else {
					setTitle(getString(R.string.file_unknown))
					setContent(text!!)
				}
			}
			else -> {
				val contentViewModel: ContentViewModel by viewModels()
				val title = contentViewModel.title
				val content = contentViewModel.content
				if (title.isEmpty() || title.trim { it <= ' ' }
						.isEmpty() || content.isEmpty() || content.trim { it <= ' ' }.isEmpty()) {
					clearView()
				} else {
					setToolbarTitle("$title - VerText")
					setTextviewContent(content)
				}
			}
		}
	}

	private fun openFile() {
		val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
		intent.type = "text/*"
		mStartForResult.launch(intent)
	}

	var mStartForResult = registerForActivityResult<Intent, ActivityResult>(
		ActivityResultContracts.StartActivityForResult()
	) { result: ActivityResult ->
		if (result.resultCode == Activity.RESULT_OK) {
			val data = result.data!!
			val fileUri = data.data
			val `is`: InputStream?
			val sb = StringBuilder()
			try {
				`is` = requireActivity().contentResolver.openInputStream(fileUri!!)
				val scanner = Scanner(`is`)
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine())
					sb.append("\n")
				}
			} catch (e: IOException) {
				setContent(Arrays.toString(e.stackTrace))
				return@registerForActivityResult
			}
			setTitle(getFilenameFromUri(fileUri))
			setContent(sb.toString())
		}
	}

	fun getFilenameFromUri(uri: Uri?): String {
		val cursor = requireActivity().contentResolver.query(uri!!, null, null, null, null)
			?: return getString(R.string.file_unknown)
		val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
		cursor.moveToFirst()
		val name = cursor.getString(nameIndex)
		cursor.close()
		return name
	}

	private fun setToolbarTitle(title: CharSequence) {
		val toolbar = requireView().findViewById<MaterialToolbar>(R.id.main_toolbar)
		toolbar.title = title
	}

	fun setTitle(title: CharSequence) {
		val contentViewModel =
			ViewModelProvider(requireActivity()).get(ContentViewModel::class.java)
		if (title === "") {
			contentViewModel.title = ""
			setToolbarTitle(requireContext().applicationInfo.name)
		} else {
			contentViewModel.title = title.toString()
			setToolbarTitle("$title - VerText")
		}
	}

	private fun setTextviewContent(content: CharSequence) {
		textView!!.text = content.toString().replace("\t".toRegex(), "    ")
		textView!!.setLines(content.toString().split("\n").toTypedArray().size)
	}

	fun setContent(content: CharSequence) {
		val contentViewModel =
			ViewModelProvider(requireActivity()).get(ContentViewModel::class.java)
		contentViewModel.content = content.toString()
		setTextviewContent(content)
	}

	private fun clearView() {
		setToolbarTitle("VerText")
		setTextviewContent(getString(R.string.none_open))
	}
}