package com.nwhacks2020.myapplication.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.nwhacks2020.myapplication.R
import com.nwhacks2020.myapplication.models.Constants
import com.nwhacks2020.myapplication.services.AppService

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val parentView = inflater.inflate(R.layout.fragment_info, container, false)

        val searchBox = parentView.findViewById<EditText>(R.id.info_fragment_search_box)
        val btn = parentView.findViewById<Button>(R.id.info_fragment_submit_button)
        val detailText = parentView.findViewById<TextView>(R.id.info_detail_text)

        btn.setOnClickListener {
            val textToSearch = searchBox.text.toString()
            AppService.getService().getKeywordMatch(textToSearch) {
                when (it) {
                    "water" -> {
                        detailText.setText(Constants.FILTER_WATER)
                    }
                    "earthquake" -> {
                        detailText.setText(Constants.EARTHQUAKE)
                    }
                    "shelter" -> {
                        detailText.setText(Constants.SHELTER)
                    }
                    "none" -> {
                        detailText.setText("No Results Found")
                    }
                }
            }
            searchBox.setText("")
        }

        return parentView

    }
}
