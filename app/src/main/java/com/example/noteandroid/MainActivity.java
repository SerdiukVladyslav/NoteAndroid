package com.example.noteandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import androidx.annotation.NonNull;

import com.example.noteandroid.data.*;
import com.example.noteandroid.adapters.*;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private NoteDatabaseHelper dbHelper;
    private EditText noteTitleEditText;
    private EditText noteContentEditText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ініціалізація бази даних
        dbHelper = new NoteDatabaseHelper(this);

        // Ініціалізація компонентів
        recyclerView = findViewById(R.id.recyclerView);
        Button addButton = findViewById(R.id.addButton);
        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);

        // Налаштування RecyclerView
        noteList = dbHelper.getAllNotes();
        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);

        // Обробник кнопки додавання нотатки
        addButton.setOnClickListener(view -> {
            String title = noteTitleEditText.getText().toString().trim();
            String content = noteContentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            // Додати нотатку до бази даних
            Note newNote = new Note(title, content);
            dbHelper.addNote(newNote);

            // Оновити список
            noteList.add(newNote);
            noteAdapter.notifyItemInserted(noteList.size() - 1);

            // Очистити поля вводу
            noteTitleEditText.setText("");
            noteContentEditText.setText("");

            Toast.makeText(this, getString(R.string.note_added), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_theme) {
            toggleTheme();
            return true;
        } else if (id == R.id.menu_language) {
            showLanguageDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Метод для перемикання теми
    private void toggleTheme() {
        boolean isDarkTheme = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    // Метод для вибору мови
    private void showLanguageDialog() {
        // Отримуємо локалізовані назви мов
        String[] languages = {
                getString(R.string.ukrainian),
                getString(R.string.english),
                getString(R.string.russian)
        };

        // Створюємо діалог вибору мови
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.choose_language))
                .setItems(languages, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            setLocale("uk");
                            break;
                        case 1:
                            setLocale("en");
                            break;
                        case 2:
                            setLocale("ru");
                            break;
                    }
                })
                .show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        recreate();
    }
}
