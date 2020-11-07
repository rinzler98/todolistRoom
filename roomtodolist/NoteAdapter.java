package com.rinzler.roomtodolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


//todo : we will use the Diff-utl listadapter class to compare the lists and make the delete animation

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {
    private Context context;
    //private List<Note> allNotes = new ArrayList<>();

    private NoteItemClickListener listener;

    //we will create our own parameter here itself
    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        //here we compare the lists

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                            oldItem.getPriority() == newItem.getPriority();
        }
    };
    //a constructor is not needed as we arent passing the notelist to the noteadapter from the main activity
   /* public NoteAdapter(Context context) {
        this.context = context;
    }

    */
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note,parent,false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        //getitem is a methood of the Listadapter class
        Note currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }
/*
    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    //this helper method helps us get the notes we created inside the call back to show in the recyclerview
    public void setNotes(List<Note> notes){
        this.allNotes = notes;
        notifyDataSetChanged();
    }

 */

    //method to get the position of the note
    public Note getNoteAt(int position){
        return getItem(position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onNoteClick(getItem(position));

                    }
                }
            });
        }

    }

    public interface NoteItemClickListener{
        //the notes position is passed into the method parameters
        void onNoteClick(Note note);
    }

    public void setOnNoteItemClickListener (NoteItemClickListener listener){
        this.listener = listener;
    }
}
