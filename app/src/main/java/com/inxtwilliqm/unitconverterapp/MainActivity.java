package com.inxtwilliqm.unitconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import kotlin.Result;
import kotlin.reflect.KType;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Button convertButton;
    TextView result;

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

        input = findViewById(R.id.input);
        convertButton = findViewById(R.id.convertButton);
        result = findViewById(R.id.result);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputString = input.getText().toString();

                Toast.makeText(MainActivity.this, inputString, Toast.LENGTH_SHORT).show();

                double parsedInput = Double.parseDouble(inputString);

                String type = "celsius"; //TODO

                switch (type) {
                    case "celsius":
                        double f = (parsedInput * 1.8) + 32;

                        result.setText(String.format("Result %.2f F", f));
                        break;

                    default:
                        break;
                }
            }
        });
    }
}