/*
    This file is used to create the first page of the app. The first page is shown when the user
    chooses the first option from the main menu. The first page shows the total word count of the
    file they are trying to analyze. From this page, the user should have the option to return to
    the main menu. They should also have the option to add this statistic to one of their
    "saved statistics", which would be saved as a pdf file after the save option is pressed in the
    main menu.
 */
// Page1.java
package com.example.pset1;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Page1 extends AppCompatActivity {
    private int totalWords = 0;
    private String fileName;
    private TextInputLayout input;
    private TextView textView;
    private Button enter1;
    private Button exit1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.page1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        input = findViewById(R.id.input);
        textView = findViewById(R.id.textView);
        enter1 = findViewById(R.id.enter1);
        exit1 = findViewById(R.id.exit1);
        enter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = input.getEditText().getText().toString().trim(); //sets the file name to the user input, including the file extension
                Toast.makeText(Page1.this, "File name set to: " + fileName, Toast.LENGTH_SHORT).show();
                updateWordCountFromFile(fileName); //updates total word count based on the file name provided
                textView.setText("Total words: " + totalWords); //displays the total word count
            }
        });
        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page1.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateWordCountFromFile(String fileName) {
        if (fileName.endsWith(".txt")) {
            try {
                AssetManager assets = this.getAssets();
                InputStream is = assets.open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] words = text.split("\\s+");
                totalWords = words.length;
                Toast.makeText(this, "Total words: " + totalWords, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else if (fileName.endsWith(".pdf")) {
            try {
                InputStream is = getAssets().open(fileName);
                PDDocument document = PDDocument.load(is);
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);
                document.close();
                String[] words = text.split("\\s+");
                totalWords = words.length;
                Toast.makeText(this, "Total words: " + totalWords, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
    }
}