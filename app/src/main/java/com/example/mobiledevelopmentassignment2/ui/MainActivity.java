package com.example.mobiledevelopmentassignment2.ui;

import com.example.mobiledevelopmentassignment2.R;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText addressInput, latitudeInput, longitudeInput, idInput;
    private TextView resultView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        addressInput = findViewById(R.id.addressInput);
        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        resultView = findViewById(R.id.resultView);

        // Set up button listeners
        findViewById(R.id.queryButton).setOnClickListener(v -> queryLocation());
        findViewById(R.id.addButton).setOnClickListener(v -> addLocation());
        findViewById(R.id.updateButton).setOnClickListener(v -> updateLocation());
        findViewById(R.id.deleteButton).setOnClickListener(v -> deleteLocation());
    }

    @SuppressLint("SetTextI18n")
    private void queryLocation() {
        String address = addressInput.getText().toString();
        Cursor cursor = dbHelper.getLocation(address);
        if (cursor != null && cursor.moveToFirst()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
            resultView.setText("Latitude: " + latitude + ", Longitude: " + longitude);
            cursor.close();
        } else {
            resultView.setText("Address not found.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void addLocation() {
        String address = addressInput.getText().toString();
        double latitude = Double.parseDouble(latitudeInput.getText().toString());
        double longitude = Double.parseDouble(longitudeInput.getText().toString());
        dbHelper.addLocation(address, latitude, longitude);
        resultView.setText("Location added.");
    }

    @SuppressLint("SetTextI18n")
    private void updateLocation() {
        int id = Integer.parseInt(idInput.getText().toString());
        String address = addressInput.getText().toString();
        double latitude = Double.parseDouble(latitudeInput.getText().toString());
        double longitude = Double.parseDouble(longitudeInput.getText().toString());
        dbHelper.updateLocation(id, address, latitude, longitude);
        resultView.setText("Location updated.");
    }

    @SuppressLint("SetTextI18n")
    private void deleteLocation() {
        int id = Integer.parseInt(idInput.getText().toString());
        dbHelper.deleteLocation(id);
        resultView.setText("Location deleted.");
    }
}
