package com.example.testai

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun testApiAi() {
        val aiService = AiService()
        val response = aiService.processRequest("test prompt")
        Log.d("AiService", "Response: $response")
    }


    fun test() {
        Log.d("MainActivity2", "test")
    }



    class AiService {


    fun processRequest(prompt: String): String {
        return "Processed: '$prompt', Result: Success"
    }


        fun test() {
            Log.d("AiService", "test")
        }
        }
}