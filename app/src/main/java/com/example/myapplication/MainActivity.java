package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    final public static String KEY_POSITION = "position";
    ListView ThemesListView;
    SimpleCursorAdapter noteAdapter;
    DataBaseAccessor db;

    // создание launcher для получения данных из дочерней активити
    ActivityResultLauncher<Intent> NotesLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // все ли хорошо при получении данных из дочерней активити?
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        //получить данные
                        Intent returnedIntent = result.getData();
                        int id = returnedIntent.getIntExtra(KEY_POSITION,-1);
                        String name = returnedIntent.getStringExtra("name");
                        String surname = returnedIntent.getStringExtra("surname");
                        //обновить БД и интерфейс
                        db.updateNote(id,name,surname);
                        noteAdapter = AdapterUpdate();
                    }
                    else
                    {
                        Log.d("MainActivity" ,"Invalid note activity result");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //создание аксессора к бд
        db = new DataBaseAccessor(this);
        setContentView(R.layout.activity_main);
        ThemesListView = findViewById(R.id.ListView);
        noteAdapter = AdapterUpdate();
        Intent NoteIntent = new Intent(this, NoteEditActivity.class);
        // обработка клика по listView
        ThemesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Добыть данные из адаптера
                String name = ((Cursor) noteAdapter.getItem(position)).getString(1);
                String surname = ((Cursor) noteAdapter.getItem(position)).getString(2);
                //отправить данные в дочернюю акливити
                NoteIntent.putExtra("name", name);
                NoteIntent.putExtra("surname", surname);
                //id - идентификатор записи в БД
                NoteIntent.putExtra(KEY_POSITION, (int) id);
                //запустить дочернюю активити
                NotesLauncher.launch(NoteIntent);
            }
        });
    }
        private SimpleCursorAdapter AdapterUpdate() {
            // получить адаптер из класса
            SimpleCursorAdapter adapter = db.getCursorAdapter(this,
                    android.R.layout.simple_list_item_1, // Разметка одного элемента ListView
                    new int[]{android.R.id.text1}); // текст этого элемента

            // установить адаптер в listview
            ThemesListView.setAdapter(adapter);
            return adapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрыть БД
        db.close();
    }
}