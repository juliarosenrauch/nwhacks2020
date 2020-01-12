package com.nwhacks2020.myapplication.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.nwhacks2020.myapplication.R
import com.nwhacks2020.myapplication.models.Offer
import com.nwhacks2020.myapplication.services.AppService

class OfferFormActivityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val parentView = inflater.inflate(R.layout.fragment_offer_form, container, false)
        val context = context!!

        val textBox = parentView.findViewById<EditText>(R.id.offer_form_edittext)
        val saveButton = parentView.findViewById<Button>(R.id.offer_form_save_button)

        val spinner = parentView.findViewById<Spinner>(R.id.offer_form_spinner)
        val spinnerData = Offer.allTypes
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, spinnerData
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter

        saveButton.setOnClickListener {
            val notes = textBox.text.toString()
            val selectedType = spinner.selectedItem as String
            // Get Location
            AppService.getService().getLocation(context) { location ->
                Log.i("OfferForm", notes)
                Log.i("OfferForm", selectedType)
                Log.i("OfferForm", location.longitude.toString())
                AppService.getService().saveOfferToFirebase(
                    Offer(
                        notes,
                        selectedType,
                        location.latitude,
                        location.longitude
                    )
                )
                val successToast = Toast.makeText(context, "Posted Successfully!", Toast.LENGTH_SHORT)
                successToast.show()
            }
        }

        return parentView
    }
}
