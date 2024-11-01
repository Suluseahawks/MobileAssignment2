package com.example.mobiledevelopmentassignment2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobiledevelopmentassignment2.ui.theme.MobileDevelopmentAssignment2Theme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DataBaseHelper

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DataBaseHelper(this)

        setContent {
            MobileDevelopmentAssignment2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    LocationFinderApp(dbHelper)
                }
            }
        }
    }
}

@Composable
fun LocationFinderApp(dbHelper: DataBaseHelper) {
    var address by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Result will appear here.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Address Input
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        // Latitude Input
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        // Longitude Input
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Query Button
        Button(
            onClick = {
                val cursor = dbHelper.getLocation(address)
                if (cursor != null && cursor.moveToFirst()) {
                    val lat = cursor.getDouble(0)
                    val lon = cursor.getDouble(1)
                    result = "Latitude: $lat, Longitude: $lon"
                    cursor.close()
                } else {
                    result = "Address not found."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Query Location")
        }

        // Add Button
        Button(
            onClick = {
                val lat = latitude.toDoubleOrNull() ?: 0.0
                val lon = longitude.toDoubleOrNull() ?: 0.0
                dbHelper.addLocation(address, lat, lon)
                result = "Location added."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Location")
        }

        // Update Button
        Button(
            onClick = {
                val lat = latitude.toDoubleOrNull() ?: 0.0
                val lon = longitude.toDoubleOrNull() ?: 0.0
                dbHelper.updateLocation(address, lat, lon)
                result = "Location updated."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Location")
        }

        // Delete Button
        Button(
            onClick = {
                dbHelper.deleteLocation(address)
                result = "Location deleted."
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Location")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Result
        Text(text = result, modifier = Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLocationFinderApp() {
    MobileDevelopmentAssignment2Theme {
        LocationFinderApp(DataBaseHelper(null))  // Pass a dummy DatabaseHelper for preview purposes
    }
}
