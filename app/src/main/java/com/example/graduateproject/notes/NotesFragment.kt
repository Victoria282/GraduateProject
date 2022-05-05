package com.example.graduateproject.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.graduateproject.R
import com.example.graduateproject.databinding.NotesFragmentBinding
import com.example.graduateproject.di.utils.ViewModelFactory
import com.example.graduateproject.notes.adapter.NotesAdapter
import com.example.graduateproject.notes.model.Note
import com.example.graduateproject.shared_preferences.SharedPreferences
import com.example.graduateproject.utils.Utils
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class NotesFragment @Inject constructor(
    viewModelFactory: ViewModelFactory
) : Fragment(R.layout.notes_fragment),
    NotesAdapter.NoteClickListener {

    private val viewModel: NotesViewModel by viewModels { viewModelFactory }
    private lateinit var binding: NotesFragmentBinding

    lateinit var notesAdapter: NotesAdapter
    var arrayNotes = ArrayList<Note>()

    private val notesObserver = Observer<List<Note>> {
        notesAdapter.updateNotes(it)
        arrayNotes.addAll(it)
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
        if (!SharedPreferences.noteOnBoarding) showOnBoarding()
        initObservers()
        initListeners()
        initUI()
    }

    private fun initListeners() = with(binding) {
        addNote.setOnClickListener {
            onNoteClick(null)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val array = ArrayList<Note>()

                for (arr in arrayNotes)
                    if (arr.title!!.lowercase(Locale.getDefault()).contains(p0.toString()))
                        array.add(arr)

                notesAdapter.updateNotes(array)
                notesAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private fun initUI() = with(binding) {
        notesAdapter = NotesAdapter()
        recyclerViewNotes.setHasFixedSize(true)
        recyclerViewNotes.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewNotes.adapter = notesAdapter
        notesAdapter.clickListener = this@NotesFragment
    }

    private fun initObservers() = with(viewModel){
        notes.observe(viewLifecycleOwner, notesObserver)
    }

    override fun onNoteClick(note: Note?) {
        val direction = NotesFragmentDirections.toNoteCreateFragment(note)
        findNavController().navigate(direction)
    }

    private fun showOnBoarding() {
        Utils.showOnBoarding(
            requireActivity(),
            binding.addNote,
            R.string.welcome_subtitle_message_note,
            requireContext()
        ) {
            SharedPreferences.noteOnBoarding = true
        }
    }
}