package com.google.anime1_downloader

import android.os.Bundle
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

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
            get_urls(title.text.toString())
        }
        cancel.setOnClickListener{
            title.text = ""
        }

        return view
    }

    fun get_urls(animeUrl:String){
        val url_list = mutableListOf<String>()

        if (animeUrl.contains(Regex("anime1.me/category/(.*?)", RegexOption.IGNORE_CASE))) {
            url_list.addAll(animeSeason(animeUrl))
        } else if (animeUrl.contains(Regex("anime1.me/[0-9]", RegexOption.IGNORE_CASE))) {
            url_list += animeUrl
        }
    }

    fun animeSeason(animeUrl:String): Collection<String> {
        val urls = mutableListOf<String>()
        val headersJson = JSONObject(getString(R.string.headers))
        val headersMap = headersJson.keys().asSequence().associateWith { headersJson.getString(it) }

        val connection = URL(animeUrl).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"

        for ((key, value) in headersMap) {
            connection.setRequestProperty(key, value)
        }
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val document = Jsoup.parse(response)
        val h2Elements = document.select("h2.entry-title")

        for (element in h2Elements) {
            val url = element.select("a[rel=bookmark]").attr("href")
            urls.add(url)
        }

        val navPrevious = document.select("div.nav-previous")
        if (navPrevious.isNotEmpty()) {
            val nextUrl = navPrevious.select("a").attr("href")
            urls.addAll(animeSeason(nextUrl))
        }

        urls.reverse()
        return urls
    }
}

