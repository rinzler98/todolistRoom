package com.rinzler.roomtodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SaveUpdateNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.rinzler.roomtodolist.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.rinzler.roomtodolist.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.rinzler.roomtodolist.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.rinzler.roomtodolist.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);

        editTextTitle  = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        //back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


        Intent intent = getIntent();
        //menu bar text
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,0));
        }else{
            setTitle("Add Note");
        }
    }

    private void saveNote(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Cannot omit fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //Todo : Send the entered note to the main activity
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRIORITY, priority);

        //Todo : Send the edited note to the main activity
        //we will need to pass the id so we can update it in the main activity
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        //a -1 value cannot exist in the database
        if (id != -1){
            //get the id and pass it
            intent.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
    //both the methods are menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}