package com.example.coroutinesexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            main()
        }
    }

    private fun main() {
        CoroutineScope(Main).launch {
            println("Starting job in Thread: ${Thread.currentThread().name}")
            val result1 = getResult()
            println("result1: $result1")
            val result2 = getResult()
            println("result1: $result2")
            val result3 = getResult()
            println("result1: $result3")
            val result4 = getResult()
            println("result1: $result4")
            val result5 = getResult()
            println("result1: $result5")
        }
        CoroutineScope(Main).launch {
            delay(1000)
            runBlocking {
                println("Blocking Thread: ${Thread.currentThread().name}")
                delay(4000)
                println("Done blocking Thread: ${Thread.currentThread().name}")
            }
        }
    }

    private suspend fun getResult(): Int {
        delay(1000)
        return Random.nextInt(0, 99)
    }

    private fun println(message: String) {
        Log.d(TAG, message)
    }
}
