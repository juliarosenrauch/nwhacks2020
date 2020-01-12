package com.nwhacks2020.myapplication.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.nwhacks2020.myapplication.R
import com.nwhacks2020.myapplication.models.Offer
import com.nwhacks2020.myapplication.services.AppService

class OfferFormActivity : AppCompatActivity() {
    private lateinit var locationProvider: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_form)

        val textBox = findViewById<EditText>(R.id.offer_form_edittext)
        val saveButton = findViewById<Button>(R.id.offer_form_save_button)

        val spinner = findViewById<Spinner>(R.id.offer_form_spinner)
        val spinnerData = Offer.allTypes
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, spinnerData
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter

        saveButton.setOnClickListener {
            val notes = textBox.text.toString()
            val selectedType = spinner.selectedItem as String
            // Get Location
            AppService.getService().getLocation(this) { location ->
                AppService.getService().saveOfferToFirebase(
                    Offer(
                        notes,
                        selectedType,
                        location.latitude,
                        location.longitude
                    )
                )
                val successToast = Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_SHORT)
                successToast.show()
            }
        }
    }
}
