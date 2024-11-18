package com.example.pset1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout numberInput;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        numberInput = findViewById(R.id.numberInput);
        enter = findViewById(R.id.button);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = numberInput.getEditText().getText().toString();
                System.out.println(number);
                if (number.isEmpty()) {
                    numberInput.setError("Please enter a number.");
                } else {
                    numberInput.setError(null);
                    int num = Integer.parseInt(number);
                    System.out.println(num);
                    Class<?> targetActivity;
                    switch (num) {
                        case 1:
                            System.out.println("case 1");
                            targetActivity = Page1.class;
                            break;
                        case 2:
                            targetActivity = Page2.class;
                            break;
                        case 3:
                            targetActivity = Page3.class;
                            break;
                        case 4:
                            targetActivity = Page4.class;
                            break;
                        case 5:
                            targetActivity = Page5.class;
                            break;
                        case 6:
                            targetActivity = Page6.class;
                            break;
                        default:
                            numberInput.setError("Invalid number. Please enter a number between 1 and 5.");
                            return;
                    }
                    Intent intent = new Intent(MainActivity.this, targetActivity);
                    startActivity(intent);
                }
            }
        });
    }
}