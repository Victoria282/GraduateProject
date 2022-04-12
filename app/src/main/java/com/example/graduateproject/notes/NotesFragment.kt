package com.example.graduateproject.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.NotesFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.notes.adapter.NotesAdapter
import com.example.graduateproject.notes.create.NoteCreateViewModel
import com.example.graduateproject.notes.model.Note
import javax.inject.Inject

class NotesFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.notes_fragment), NotesAdapter.NoteClickListener {

    private val viewModel: NoteCreateViewModel by viewModels { viewModelFactory }
    private lateinit var binding: NotesFragmentBinding
    lateinit var notesAdapter: NotesAdapter

    private val notesObserver = Observer<List<Note>> {
        notesAdapter.updateNotes(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotesFragmentBinding.inflate(inflater, container, false)
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
        notesAdapter = NotesAdapter()
        recyclerViewNotes.setHasFixedSize(true)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        recyclerViewNotes.adapter = notesAdapter
        notesAdapter.clickListener = this@NotesFragment
    }

    private fun initObservers() {
        viewModel.notes.observe(viewLifecycleOwner, notesObserver)
    }

    override fun onNoteClick(note: Note?) {
        val direction = NotesFragmentDirections.toNoteCreateFragment(note)
        findNavController().navigate(direction)
    }
}