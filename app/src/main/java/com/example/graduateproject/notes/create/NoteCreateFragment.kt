package com.example.graduateproject.notes.create

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.graduateproject.R
import com.example.graduateproject.databinding.NoteCreateFragmentBinding
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.utils.Utils
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteCreateFragment @Inject constructor(

) : Fragment(R.layout.note_create_fragment) {
    private lateinit var viewModel: NoteCreateViewModel
    private lateinit var binding: NoteCreateFragmentBinding
    private val args by navArgs<NoteCreateFragmentArgs>()
    private lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteCreateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.notesVal?.let {
            setNoteInfo(it)
        }
        viewModel = ViewModelProvider(this)[NoteCreateViewModel::class.java]
        initListeners()
        setNoteDate()
    }

    private fun initListeners() = with(binding) {
        saveNote.setOnClickListener {
            val noteTittle = noteTittle.text?.trim().toString()
            val noteSubTittle = noteSubTittle.text?.trim().toString()
            val date = date.text?.trim().toString()
            val noteText = noteText.text?.trim().toString()

            if (noteTittle == "" || noteSubTittle == "" || noteText == "")
                Utils.showMessage(R.string.message_input_empty_fields, requireContext())
            else {
                val id = if (args.notesVal == null) 0 else args.notesVal!!.id
                note = Note(
                    id = id,
                    title = noteTittle,
                    subTitle = noteSubTittle,
                    dateTime = date,
                    noteText = noteText
                )
                if (id == 0) viewModel.insertNote(note)
                else viewModel.updateNote(note)

                backToNotesFragment()
            }
        }

        bottomSheetMenu.setOnClickListener {
            setDialog()
        }
    }

    private fun setDialog() = with(binding) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        val deleteNoteButton = dialog.delete_text
        val uploadImage = dialog.upload_image

        deleteNoteButton.setOnClickListener {
            val note = args.notesVal
            note?.let {
                viewModel.deleteNote(it)
            }
            dialog.dismiss()
            backToNotesFragment()
        }

        uploadImage.setOnClickListener {
            uploadImage()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.bottomSheetAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun uploadImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        openGallery.launch(intent)
    }

    private val openGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImageUrl = it.data?.data
        }
    }

    private fun backToNotesFragment() {
        val direction = NoteCreateFragmentDirections.toNotesFragment()
        findNavController().navigate(direction)
    }

    private fun setNoteDate() = with(binding) {
        val currentDate = sdf.format(Date())
        date.text = currentDate
    }

    private fun setNoteInfo(note: Note) = with(binding) {
        noteTittle.setText(note.title)
        noteSubTittle.setText(note.subTitle)
        date.text = note.dateTime
        noteText.setText(note.noteText)
    }

    companion object {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        fun newInstance() = NoteCreateFragment()
    }
}