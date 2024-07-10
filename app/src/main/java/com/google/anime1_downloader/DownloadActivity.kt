package com.google.anime1_downloader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DownloadActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_download, container, false)

        val ok = view.findViewById<Button>(R.id.button_ok)
        val cancel = view.findViewById<Button>(R.id.button_cancel)
        val title = view.findViewById<TextView>(R.id.anime_url)


        ok.setOnClickListener {
            Toast.makeText(context, title.text, Toast.LENGTH_SHORT).show()
        }
        cancel.setOnClickListener{
            title.text = ""
        }

        return view
    }
}
