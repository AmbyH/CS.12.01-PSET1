/*
    This file is used to create the fourth page of the app. The fourth page is shown when the user
    chooses the fourth option from the main menu. The fourth page shows the top five most common
    words of the file they are trying to analyze. From this page, the user should have the option to
    return to the main menu.
 */
// Page4.java
package com.example.pset1;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page4 extends AppCompatActivity {
    private TextInputLayout input;
    private TextView textView;
    private Button enter4;
    private Button exit4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.page4), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        input = findViewById(R.id.input);
        textView = findViewById(R.id.textView);
        enter4 = findViewById(R.id.enter4);
        exit4 = findViewById(R.id.exit4);
        enter4.setOnClickListener(view -> {
            String fileName = input.getEditText().getText().toString();
            displayTopFiveWords(fileName);
        });
        exit4.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void displayTopFiveWords(String fileName) {
        String fileContent = readFileFromAssets(fileName);
        if (fileContent != null) {
            Map<String, Integer> wordOccurrences = getWordOccurrences(fileContent);
            List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordOccurrences.entrySet());
            sortedWords.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < Math.min(5, sortedWords.size()); i++) {
                Map.Entry<String, Integer> entry = sortedWords.get(i);
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            textView.setText("Top 5 most common words:\n" + result.toString());
        } else {
            textView.setText("Error reading file.");
        }
    }

    private Map<String, Integer> getWordOccurrences(String content) {
        Map<String, Integer> wordOccurrences = new HashMap<>();
        String[] words = content.split("[\\s\\p{Punct}]+");
        for (String word : words) {
            word = word.toLowerCase();
            wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + 1);
        }
        return wordOccurrences;
    }

    private String readFileFromAssets(String fileName) {
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}