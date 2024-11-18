/*
    This file is used to create the fifth page of the app. The fifth page is shown when the user
    chooses the fifth option from the main menu. The fifth page allows the user to generate a random
    paragraph from the file they are trying to analyze. From this page, the user should have the
    option to enter a temperature parameter (or not), and return to the main menu.
 */
// Page5.java
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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Page5 extends AppCompatActivity {
    private TextView textView;
    private TextInputLayout input;
    private TextInputLayout temperatureInput; //should be a value from 1-100
    private Button exit5;
    private Button enter5;
    private String fileName;
    private String paragraph;
    private int temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.page5), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.textView);
        input = findViewById(R.id.input);
        temperatureInput = findViewById(R.id.temperatureInput);
        exit5 = findViewById(R.id.exit5);
        enter5 = findViewById(R.id.enter5);
        enter5.setOnClickListener(view -> {
            fileName = input.getEditText().getText().toString();
            paragraph = generateRandomParagraphFromFile(fileName);
            textView.setText(paragraph);
        });
        exit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page5.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private String generateRandomParagraphFromFile(String fileName) {
        String text = "";
        try {
            if (fileName.endsWith(".txt")) {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                text = new String(buffer);
            } else if (fileName.endsWith(".pdf")) {
                InputStream is = getAssets().open(fileName);
                PDDocument document = PDDocument.load(is);
                PDFTextStripper pdfStripper = new PDFTextStripper();
                text = pdfStripper.getText(document);
                document.close();
            } else {
                return "Unsupported file type";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file";
        }

        String[] words = text.split("\\s+");
        List<String> wordList = new ArrayList<>();
        Collections.addAll(wordList, words);

        if (temperatureInput.getEditText().getText() != null && !temperatureInput.getEditText().getText().toString().isEmpty()) {
            temperature = Integer.parseInt(temperatureInput.getEditText().getText().toString());
        } else {
            temperature = 50; // default temperature
        }

        int paragraphLength = Math.max(1, wordList.size() / 10); // example length
        Random random = new Random();
        StringBuilder paragraph = new StringBuilder();

        for (int i = 0; i < paragraphLength; i++) {
            int index = random.nextInt(wordList.size());
            paragraph.append(wordList.get(index)).append(" ");
            if (temperature > 50) {
                Collections.shuffle(wordList);
            }
        }

        return paragraph.toString().trim();
    }
}