/*
    This file is used to create the third page of the app. The third page is shown when the user
    chooses the third option from the main menu. The third page shows the unique words and their
    occurrences in the file they are trying to analyze. From this page, the user should have the
    option to return to the main menu.
 */
// Page3.java
package com.example.pset1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Page3 extends AppCompatActivity {
    private Button exit3;
    private Button enter3;
    private TextView textView;
    private TextInputLayout input;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.page3), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.textView);
        exit3 = findViewById(R.id.exit3);
        enter3 = findViewById(R.id.enter3);
        input = findViewById(R.id.input);
        exit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page3.this, MainActivity.class);
                startActivity(intent);
            }
        });
        enter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = input.getEditText().getText().toString();
                if (fileName.isEmpty()) {
                    input.setError("Please enter a file name.");
                } else {
                    input.setError(null);
                    displayUniqueWords(fileName);
                }
            }
        });
    }
    private void displayUniqueWords(String fileName) {
        String fileContent = readFileFromAssets(fileName);
        if (fileContent != null) {
            Map<String, Integer> wordOccurrences = getUniqueWords(fileContent);
            StringBuilder result = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, Integer> entry : wordOccurrences.entrySet()) {
                if (entry.getValue() == 1) {
                    result.append(entry.getKey()).append(",");
                    count++;
                    if (count >= 50) {
                        break;
                    }
                }
            }
            textView.setText("The most unique words in your file are: " + result.toString());
        } else {
            textView.setText("Error reading file.");
        }
    }

    private String readFileFromAssets(String fileName) {
        StringBuilder fileContent = new StringBuilder();
        try (InputStream is = getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileContent.toString();
    }

    private Map<String, Integer> getUniqueWords(String content) {
        Map<String, Integer> wordOccurrences = new HashMap<>();
        String[] words = content.split("\\W+");
        for (String word : words) {
            word = word.toLowerCase();
            wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + 1);
        }
        return wordOccurrences;
    }
}