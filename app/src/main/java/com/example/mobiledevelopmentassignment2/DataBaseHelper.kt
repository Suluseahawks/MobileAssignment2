package com.example.mobiledevelopmentassignment2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "locations.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "locations"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    // SQL statement to create the table
    private val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ADDRESS TEXT PRIMARY KEY,
            $COLUMN_LATITUDE REAL,
            $COLUMN_LONGITUDE REAL
        )
    """

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)  // Execute the create table statement
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")  // Drop the old table if it exists
        onCreate(db)  // Create the new table
    }

    // Method to add a new location
    fun addLocation(address: String, latitude: Double, longitude: Double) {
        val db = writableDatabase  // Get writable database
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
        }
        db.insert(TABLE_NAME, null, values)  // Insert the new location
        db.close()  // Close the database
    }

    // Method to retrieve a location based on the address
    fun getLocation(address: String): Cursor? {
        val db = readableDatabase  // Get readable database
        return db.query(
            TABLE_NAME,
            arrayOf(COLUMN_LATITUDE, COLUMN_LONGITUDE),
            "$COLUMN_ADDRESS = ?",
            arrayOf(address),
            null, null, null
        )  // Query for the latitude and longitude
    }

    // Method to update an existing location
    fun updateLocation(address: String, latitude: Double, longitude: Double) {
        val db = writableDatabase  // Get writable database
        val values = ContentValues().apply {
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ADDRESS = ?", arrayOf(address))  // Update the location
        db.close()  // Close the database
    }

    // Method to delete a location
    fun deleteLocation(address: String) {
        val db = writableDatabase  // Get writable database
        db.delete(TABLE_NAME, "$COLUMN_ADDRESS = ?", arrayOf(address))  // Delete the location
        db.close()  // Close the database
    }
}
