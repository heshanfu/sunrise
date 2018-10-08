package com.saitanwgpha.sunshine

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun GetSunset(view: View){
        //Todo: URL
        var city = edCityName.text.toString()
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String,String,String>() {

        override fun onPreExecute() {
            //Todo: Before Task Start
        }

        override fun doInBackground(vararg p0: String?): String {
            //Todo: http call
            try {
                //URL
                val url = URL(p0[0])
                //Connection
                val urlConnect = url.openConnection() as HttpURLConnection
                //Time out
                urlConnect.connectTimeout = 7000
                //Convert to string
                var inString = ConvertSreamToString(urlConnect.inputStream)
                //Cannot access to ui
                publishProgress(inString)
            }catch (ex: Exception){}
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            //Todo: Update UI
            try {

                //Json
                var json = JSONObject(values[0])
                val query = json.getJSONObject("query")
                val results = query.getJSONObject("results")
                val channel = results.getJSONObject("channel")
                val astronomy = channel.getJSONObject("astronomy")
                val sunset = astronomy.getString("sunrise")
                    tvSunset.text = "Sunrise time is $sunset."

            }catch (ex: Exception){}
        }

        override fun onPostExecute(result: String?) {
            //Todo: After Task Start

        }
    }

    fun ConvertSreamToString(inputStream: InputStream): String {
        //Todo: Convert To String
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var AllString: String = ""

        try {

            do {
                line = bufferReader.readLine()
                if (line != null){
                    AllString+= line
                }
            }while(line != null)
            inputStream.close()

        }catch (ex: java.lang.Exception){}
        return AllString
    }

}
