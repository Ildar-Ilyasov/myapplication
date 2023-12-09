package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class NoteEditActivity extends AppCompatActivity {

    EditText NameEditText;
    EditText SurnameEditText;
    int Position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        NameEditText = findViewById(R.id.nameEditText);
        SurnameEditText = findViewById(R.id.surnameEditText);

        Intent fromMainActivityIntent = getIntent();
        NameEditText.setText(fromMainActivityIntent.getExtras().getString("name"));
        SurnameEditText.setText(fromMainActivityIntent.getExtras().getString("surname"));
        Position = fromMainActivityIntent.getIntExtra(MainActivity.KEY_POSITION,-1);

        if(Position == -1) {
            Log.d("Note activity","Invalid position");
        }
    }

    public void OnBackButtonClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name",NameEditText.getText().toString());
        returnIntent.putExtra("surname",SurnameEditText.getText().toString());
        returnIntent.putExtra(MainActivity.KEY_POSITION,Position);
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}
