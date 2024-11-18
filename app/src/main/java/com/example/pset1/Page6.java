/*
    This file is used to create the sixth page of the app. The sixth page is shown when the user
    chooses the sixth option from the main menu. The sixth page allows the user to save chosen
    statistics from the file they are trying to analyze. From this page, the user should have the
    option to save or return to the main menu. The saved statistics should be named by the user and
    be saved as a pdf file.
 */
// Page6.java
package com.example.pset1;

import android.content.Intent;
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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Page6 extends AppCompatActivity {
    private TextView textView;
    private TextInputLayout saveFileInput;
    private TextInputLayout targetFileInput;
    private TextInputLayout tempInput;
    private TextInputLayout actionInput;
    private Button addToFile;
    private Button saveToPDF;
    private Button exit6;
    private String fileName;
    private String PDFContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page6);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.page6), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.textView);
        saveFileInput = findViewById(R.id.saveFileInput);
        targetFileInput = findViewById(R.id.targetFileInput);
        actionInput = findViewById(R.id.actionInput);
        tempInput = findViewById(R.id.tempInput);
        addToFile = findViewById(R.id.addToFile);
        saveToPDF = findViewById(R.id.saveToPDF);
        exit6 = findViewById(R.id.exit6);
        exit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page6.this, MainActivity.class);
                startActivity(intent);
            }
        });
        addToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = targetFileInput.getEditText().getText().toString();
                String action = actionInput.getEditText().getText().toString();
                switch (action) {
                    case "1":
                        PDFContents += "Total words: " + getTotalWords(fileName) + "\n";
                        break;
                    case "2":
                        PDFContents += "Total sentences: " + getTotalSentences(fileName) + "\n";
                        break;
                    case "3":
                        PDFContents += "Unique words: " + getUniqueWords(fileName) + "\n";
                        break;
                    case "4":
                        PDFContents += "Top 5 words: " + getTopFiveWords(fileName) + "\n";
                        break;
                    case "5":
                        int temp = Integer.parseInt(tempInput.getEditText().getText().toString());
                        PDFContents += "Random paragraph: " + generateRandomParagraphFromFile(fileName, temp) + "\n";
                        break;
                    default:
                        textView.setText("Invalid action");
                        return;
                }
                textView.setText("Content added to PDF");
            }
        });
        saveToPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = saveFileInput.getEditText().getText().toString();
                saveToPDF(fileName, PDFContents);
            }
        });
    }

    private int getTotalWords(String fileName) {
        int totalWords = 0;
        if (fileName.endsWith(".txt")) {
            try {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] words = text.split("\\s+");
                totalWords = words.length;
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
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
        return totalWords;
    }

    private int getTotalSentences(String fileName) {
        int totalSentences = 0;
        if (fileName.endsWith(".txt")) {
            try {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] sentences = text.split("[.!?]\\s*");
                totalSentences = sentences.length;
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
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
        return totalSentences;
    }

    private String getUniqueWords(String fileName) {
        Set<String> uniqueWords = new HashSet<>();
        if (fileName.endsWith(".txt")) {
            try {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] words = text.split("\\s+");
                uniqueWords.addAll(Arrays.asList(words));
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
                uniqueWords.addAll(Arrays.asList(words));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
        return String.join(", ", uniqueWords);
    }


    private String getTopFiveWords(String fileName) {
        Map<String, Integer> wordCount = new HashMap<>();
        if (fileName.endsWith(".txt")) {
            try {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] words = text.split("\\s+");
                for (String word : words) {
                    word = word.toLowerCase().replaceAll("[^a-z]", "");
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
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
                for (String word : words) {
                    word = word.toLowerCase().replaceAll("[^a-z]", "");
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }

        return wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private String generateRandomParagraphFromFile(String fileName, int temperature) {
        StringBuilder paragraph = new StringBuilder();
        if (fileName.endsWith(".txt")) {
            try {
                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String text = new String(buffer, "UTF-8");
                String[] sentences = text.split("[.!?]\\s*");
                for (int i = 0; i < temperature && i < sentences.length; i++) {
                    paragraph.append(sentences[i]).append(". ");
                }
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
                for (int i = 0; i < temperature && i < sentences.length; i++) {
                    paragraph.append(sentences[i]).append(". ");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
        }
        return paragraph.toString();
    }

    private void saveToPDF(String fileName, String content) {
        try {
            File file = new File(getFilesDir(), fileName + ".pdf");
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.add(new Paragraph(content));
            document.close();
            textView.setText("PDF saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            textView.setText("Error saving PDF");
        }
    }
}