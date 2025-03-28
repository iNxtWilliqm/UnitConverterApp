package com.inxtwilliqm.unitconverterapp;

import android.annotation.SuppressLint;
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
        switch (srcUnit) {
            case "Centimeter": return value;
            case "Kilometer": return value * 100000;
            case "Inch": return value * 2.54;
            case "Foot": return value * 30.48;
            case "Yard": return value * 91.44;
            case "Mile": return value * 160934;
            default: throw new IllegalArgumentException("Invalid unit: " + srcUnit);
        }
    }

    @Override
    public double fromBaseUnit(double value, String dstUnit) {
        switch (dstUnit) {
            case "Centimeter": return value;
            case "Kilometer": return value / 100000;
            case "Inch": return value / 2.54;
            case "Foot": return value / 30.48;
            case "Yard": return value / 91.44;
            case "Mile": return value / 160934;
            default: throw new IllegalArgumentException("Invalid unit: " + dstUnit);
        }
    }
}

class WeightConversion implements IConversion {
    @Override
    public double toBaseUnit(double value, String srcUnit) {
        switch (srcUnit) {
            case "Gram": return value;
            case "Kilogram": return value * 1000;
            case "Pound": return value * 453.592;
            case "Ounce": return value * 28.3495;
            case "Ton": return value * 907185;
            default: throw new IllegalArgumentException("Invalid unit: " + srcUnit);
        }
    }

    @Override
    public double fromBaseUnit(double value, String dstUnit) {
        switch (dstUnit) {
            case "Gram": return value;
            case "Kilogram": return value / 1000;
            case "Pound": return value / 453.592;
            case "Ounce": return value / 28.3495;
            case "Ton": return value / 907185;
            default: throw new IllegalArgumentException("Invalid unit: " + dstUnit);
        }
    }
}

class TemperatureConversion implements IConversion {
    @Override
    public double toBaseUnit(double value, String srcUnit) {
        switch (srcUnit) {
            case "Celsius": return value;
            case "Kelvin": return value - 273.15;
            case "Fahrenheit": return (value - 32) / 1.8;
            default: throw new IllegalArgumentException("Invalid unit: " + srcUnit);
        }
    }

    @Override
    public double fromBaseUnit(double value, String dstUnit) {
        switch (dstUnit) {
            case "Celsius": return value;
            case "Kelvin": return value + 273.15;
            case "Fahrenheit": return (value * 1.8) + 32;
            default: throw new IllegalArgumentException("Invalid unit: " + dstUnit);
        }
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
            @SuppressLint("DefaultLocale")
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

                    result.setText(String.format("Result: %.5f %s", convertedValue, getUnitCode(dstUnit)));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getUnitCode(String unitName) {
        switch (unitName) {
            case "Centimeter": return "cm";
            case "Kilometer": return "km";
            case "Inch": return "in";
            case "Foot": return "ft";
            case "Yard": return "yd";
            case "Mile": return "mi";

            case "Gram": return "g";
            case "Kilogram": return "kg";
            case "Pound": return "lb";
            case "Ounce": return "oz";
            case "Ton": return "t";

            case "Celsius": return "°C";
            case "Fahrenheit": return "°F";
            case "Kelvin": return "K";

            default: return unitName;
        }
    }

    private ArrayAdapter<CharSequence> createAdapter(int arrayResId) {
        return ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
    }
}