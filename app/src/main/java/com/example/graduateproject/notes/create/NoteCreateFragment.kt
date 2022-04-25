package com.example.graduateproject.notes.create

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.graduateproject.R
import com.example.graduateproject.databinding.NoteCreateFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.utils.Utils
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteCreateFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.note_create_fragment) {

    private val viewModel: NoteCreateViewModel by viewModels { viewModelFactory }
    private lateinit var binding: NoteCreateFragmentBinding
    private val args by navArgs<NoteCreateFragmentArgs>()

    private lateinit var note: Note
    private var selectedImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteCreateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.notesVal?.let { setNoteInfo(it) }
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
                val imgPath =
                    if (selectedImageUrl == "" || selectedImageUrl == null) args.notesVal?.imgPath else selectedImageUrl
                note = Note(
                    id = id,
                    title = noteTittle,
                    subTitle = noteSubTittle,
                    dateTime = date,
                    noteText = noteText,
                    imgPath = imgPath
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

        val deleteNoteButton = dialog.delete_account
        val uploadImage = dialog.change_img

        deleteNoteButton.setOnClickListener {
            val note = args.notesVal
            note?.let { viewModel.deleteNote(it) }
            dialog.dismiss()
            backToNotesFragment()
        }

        uploadImage.setOnClickListener {
            takePhotoFromGallery()
            dialog.dismiss()
        }

        with(dialog) {
            show()
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.bottomSheetAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) getPhotoFromGallery()
        }

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUrl = result.data?.data?.toFilePath(requireContext())
                Glide.with(requireContext()).load(selectedImageUrl).into(binding.imgNote)
                binding.imgNote.visibility = View.VISIBLE
            }
        }

    private fun takePhotoFromGallery() = if (checkPermission()) getPhotoFromGallery()
    else permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun getPhotoFromGallery() {
        val imageFromGalleryIntent = Intent(Intent.ACTION_PICK)
        imageFromGalleryIntent.type = "image/*"
        galleryResultLauncher.launch(imageFromGalleryIntent)
    }

    private fun checkPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

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

        val bitmap = BitmapFactory.decodeFile(note.imgPath)
        imgNote.visibility = View.VISIBLE
        imgNote.setImageBitmap(bitmap)
    }

    private fun Uri.toFilePath(context: Context): String {
        val mimeType: String? = if (this.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            context.contentResolver.getType(this)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                this.toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase()
            )
        }
        val type = mimeType?.split("/")?.get(1)
        val filename = this.lastPathSegment?.split(".")?.first()?.split("/")?.last()
            ?: this.lastPathSegment?.split("/")?.last()
        val outputFile = File("${context.filesDir}", "$filename.$type")
        val inputStream = context.contentResolver.openInputStream(this)
        inputStream?.use { input ->
            try {
                val outputStream = FileOutputStream(outputFile)
                outputStream.use { output ->
                    val buffer = ByteArray(BUFFER_SIZE)
                    while (true) {
                        val byteCount = input.read(buffer)
                        if (byteCount < 0) break
                        output.write(buffer, 0, byteCount)
                    }
                    output.flush()
                }
            } catch (e: FileNotFoundException) {
            }
        }
        return outputFile.absolutePath
    }

    companion object {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        private const val BUFFER_SIZE = 1024 * 1024
    }
}