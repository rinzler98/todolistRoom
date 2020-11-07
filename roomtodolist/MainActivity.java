package com.rinzler.roomtodolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        //noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecyclerView
              //  Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
                noteAdapter.submitList(notes);
            }
        });

        FloatingActionButton fabButton = findViewById(R.id.fab_add_button);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveUpdateNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });

        //todo :swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Item deleted..", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //handling the cardview click event
        noteAdapter.setOnNoteItemClickListener(new NoteAdapter.NoteItemClickListener() {
            //the note here is note we pressed and it is passed here with the position of the note clicked / adapter
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, SaveUpdateNoteActivity.class);
                //we need the primary key to update the note
                intent.putExtra(SaveUpdateNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(SaveUpdateNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(SaveUpdateNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(SaveUpdateNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });

    }

    //oncreateoptionsmenu to delete all the notes


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;

    }
    
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_All:
                noteViewModel.deleteAll();
                Toast.makeText(this, "All Notes Deleted..", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Todo : handling add note
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(SaveUpdateNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(SaveUpdateNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(SaveUpdateNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();

            //Todo Handling update note
        } else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(SaveUpdateNoteActivity.EXTRA_ID, -1);
            //something is wrong or id is note passed properly
            if (id == -1 ){
                Toast.makeText(this, "Cannot Update Note.", Toast.LENGTH_SHORT).show();
            }
            String title = data.getStringExtra(SaveUpdateNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(SaveUpdateNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(SaveUpdateNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            //Todo : Important : Room cannot update if there is no ID
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated Succesfully", Toast.LENGTH_SHORT).show();
            //Snackbar.make(this,"",Snackbar.LENGTH_SHORT).show();
        }
    }
}