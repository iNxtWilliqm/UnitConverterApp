package com.inxtwilliqm.unitconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

interface IConversion {
    // Convert a value from the source unit to the base unit
    double toBaseUnit(double value, String srcUnit);

    // Convert a value from the base unit to the destination unit
    double fromBaseUnit(double value, String dstUnit);
}

class LengthConversion implements IConversion {
    @Override
    public double toBaseUnit(double value, String srcUnit) {
        double result;
        switch (srcUnit) {
            case "Centimeter":
                result = value;
                break;
            case "Kilometer":
                result = value * 100000;
                break;
            case "Inch":
                result = value * 2.54;
                break;
            case "Foot":
                result = value * 30.48;
                break;
            case "Yard":
                result = value * 91.44;
                break;
            case "Mile":
                result = value * 160934;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + srcUnit);
        }
        return result;
    }

    @Override
    public double fromBaseUnit(double value, String dstUnit) {
        double result;
        switch (dstUnit) {
            case "Centimeter":
                result = value;
                break;
            case "Kilometer":
                result = value / 100000;
                break;
            case "Inch":
                result = value / 2.54;
                break;
            case "Foot":
                result = value / 30.48;
                break;
            case "Yard":
                result = value / 91.44;
                break;
            case "Mile":
                result = value / 160934;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + dstUnit);
        }
        return result;
    }
}

class WeightConversion implements IConversion {
    @Override
    public double toBaseUnit(double value, String srcUnit) {
        double result;
        switch (srcUnit) {
            case "Gram":
                result = value;
                break;
            case "Kilogram":
                result = value * 1000;
                break;
            case "Pound":
                result = value * 453.592;
                break;
            case "Ounce":
                result = value * 28.3495;
                break;
            case "Ton":
                result = value * 907185;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + srcUnit);
        }
        return result;
    }

    @Override
    public double fromBaseUnit(double value, String dstUnit) {
        double result;
        switch (dstUnit) {
            case "Gram":
                result = value;
                break;
            case "Kilogram":
                result = value / 1000;
                break;
            case "Pound":
                result = value / 453.592;
                break;
            case "Ounce":
                result = value / 28.3495;
                break;
            case "Ton":
                result = value / 907185;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + dstUnit);
        }
        return result;
    }
}

class TemperatureConversion implements IConversion {
    @Override
    public double toBaseUnit(double value, String srcUnit) {
        double result;
        switch (srcUnit) {
            case "Celsius":
                result = value;
                break;
            case "Kelvin":
                result = value - 273.15;
                break;
            case "Fahrenheit":
                result = (value - 32) / 1.8;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + srcUnit);
        }
        return result;
    }

    @Override
    public double fromBaseUnit(double value, String dstUnit) {
        double result;
        switch (dstUnit) {
            case "Celsius":
                result = value;
                break;
            case "Kelvin":
                result = value + 273.15;
                break;
            case "Fahrenheit":
                result = (value * 1.8) + 32;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + dstUnit);
        }
        return result;
    }
}

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Button convertButton;
    private TextView result;
    private Spinner typeSpinner;
    private Spinner srcSpinner;
    private Spinner dstSpinner;
    private IConversion converter;

    private void initComponents() {
        typeSpinner = findViewById(R.id.type_spinner);
        srcSpinner = findViewById(R.id.src_spinner);
        dstSpinner = findViewById(R.id.dst_spinner);
        input = findViewById(R.id.input);
        convertButton = findViewById(R.id.convert_button);
        result = findViewById(R.id.result);
    }

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

        initComponents();

        typeSpinner.setAdapter(createAdapter(R.array.conversion_type));

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = adapterView.getItemAtPosition(i).toString();
                switch (type) {
                    case "Length":
                        converter = new LengthConversion();
                        srcSpinner.setAdapter(createAdapter(R.array.length_units));
                        dstSpinner.setAdapter(createAdapter(R.array.length_units));
                        break;
                    case "Weight":
                        converter = new WeightConversion();
                        srcSpinner.setAdapter(createAdapter(R.array.weight_units));
                        dstSpinner.setAdapter(createAdapter(R.array.weight_units));
                        break;
                    case "Temperature":
                        converter = new TemperatureConversion();
                        srcSpinner.setAdapter(createAdapter(R.array.temperature_units));
                        dstSpinner.setAdapter(createAdapter(R.array.temperature_units));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputString = input.getText().toString();

                if (inputString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double value = Double.parseDouble(inputString);

                    String srcUnit = srcSpinner.getSelectedItem().toString();
                    String dstUnit = dstSpinner.getSelectedItem().toString();

                    double baseValue = converter.toBaseUnit(value, srcUnit);
                    double convertedValue = converter.fromBaseUnit(baseValue, dstUnit);

                    result.setText(String.format("Result: %.5f %s", convertedValue, dstUnit));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayAdapter<CharSequence> createAdapter(int arrayResId) {
        return ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
    }
}