package com.example.aibcoroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var TV:TextView
    lateinit var BTN:Button
    val adviceUrl = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TV=findViewById(R.id.tv_advice)
        BTN=findViewById(R.id.btn)

        BTN.setOnClickListener(){
            requestApi()
        }

    }

    private fun requestApi()
    {
        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                fetchRandomAdvice()

            }.await()

            if (data.isNotEmpty())
            {

                updateAdviceText(data)
            }

        }

    }

    private fun fetchRandomAdvice():String{

        var response=""
        try {
            response = URL(adviceUrl).readText(Charsets.UTF_8)

        }catch (e: Exception)
        {
            println("Error $e")

        }
        return response

    }

    private suspend fun updateAdviceText(data:String)
    {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")
            TV.text = advice

        }

    }

}