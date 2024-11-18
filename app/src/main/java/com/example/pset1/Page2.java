/*
    This file is used to create the second page of the app. The second page is shown when the user
    chooses the second option from the main menu. The second page shows the total sentence count of
    the file they are trying to analyze. From this page, the user should have the option to return
    to the main menu. They should also have the option to add this statistic to one of their
    "saved statistics", which would be saved as a pdf file after the save option is pressed in the
    main menu.
 */
// Page2.java
package com.example.pset1;

import android.annotation.SuppressLint;
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

import java.io.IOException;
import java.io.InputStream;

public class Page2 extends AppCompatActivity {
    private TextInputLayout input;
    private TextView textView;
    private Button enter2;
    private Button exit2;
    private String fileName;
    private int totalSentences = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.page2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        input = findViewById(R.id.input);
        textView = findViewById(R.id.textView);
        enter2 = findViewById(R.id.enter2);
        exit2 = findViewById(R.id.exit2);
        enter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = input.getEditText().getText().toString();
                updateSentenceCountFromFile(fileName);
                textView.setText("Total sentences: " + totalSentences);
            }
        });
        exit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateSentenceCountFromFile(String fileName) {
        if (fileName.endsWith(".txt")) {
            try {
                AssetManager assets = this.getAssets();
                InputStream is = assets.open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] sentences = text.split("[.!?]\\s*");
                totalSentences = sentences.length;
                Toast.makeText(this, "Total sentences: " + totalSentences, Toast.LENGTH_SHORT).show();
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
                String[] sentences = text.split("[.!?]\\s*");
                totalSentences = sentences.length;
                Toast.makeText(this, "Total sentences: " + totalSentences, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
    }
}