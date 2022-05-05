package com.example.graduateproject.notes.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduateproject.databinding.NoteItemBinding
import com.example.graduateproject.notes.model.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.DataViewHolder>() {
    private var notes: ArrayList<Note> = ArrayList()
    var clickListener: NoteClickListener? = null

    interface NoteClickListener {
        fun onNoteClick(note: Note?)
    }

    class DataViewHolder(
        private val binding: NoteItemBinding,
        private val listener: NoteClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note?) {
            with(binding) {
                tittle.text = note?.title
                subtitle.text = note?.subTitle
                date.text = note?.dateTime

                noteItem.setOnClickListener {
                    listener?.onNoteClick(note)
                }
                val bitmap = BitmapFactory.decodeFile(note?.imgPath)
                imgNote.visibility = View.VISIBLE
                imgNote.setImageBitmap(bitmap)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteItemBinding.inflate(
            inflater,
            parent,
            false
        )
        return DataViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    fun updateNotes(notes: List<Note>) {
        this.notes.apply {
            clear()
            addAll(notes)
        }
        notifyDataSetChanged()
    }
}