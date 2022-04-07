package com.example.graduateproject.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.NotesFragmentBinding
import com.example.graduateproject.notes.adapter.NotesAdapter
import com.example.graduateproject.notes.create.NoteCreateViewModel
import com.example.graduateproject.notes.model.Note
import javax.inject.Inject

class NotesFragment @Inject constructor(

) : Fragment(R.layout.notes_fragment), NotesAdapter.NoteClickListener {
    lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: NoteCreateViewModel
    private lateinit var binding: NotesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotesFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NoteCreateViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
        initUI()
    }

    private fun initListeners() = with(binding) {
        addNote.setOnClickListener {
            onNoteClick(null)
        }
    }

    private fun initUI() = with(binding) {
        recyclerViewNotes.setHasFixedSize(true)
        recyclerViewNotes.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        notesAdapter = NotesAdapter()
        recyclerViewNotes.adapter = notesAdapter
        notesAdapter.clickListener = this@NotesFragment
    }

    private fun initObservers() {
        viewModel.notes.observe(viewLifecycleOwner, {
            notesAdapter.updateNotes(it)
        })
    }

    override fun onNoteClick(note: Note?) {
        val direction = NotesFragmentDirections.toNoteCreateFragment(note)
        findNavController().navigate(direction)
    }

    companion object {
        fun newInstance() = NotesFragment()
    }
}