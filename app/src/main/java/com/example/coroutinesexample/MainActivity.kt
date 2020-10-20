package com.example.coroutinesexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val TAG = "Debug"
    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main()
        button.setOnClickListener {
            parentJob.cancel()
        }
    }

    suspend fun work(i: Int) {
        delay(3000)
        println("Work $i done. ${Thread.currentThread().name}")
    }

    private fun main() {
        val startTime = System.currentTimeMillis()
        println("Starting parent job...")
        parentJob = CoroutineScope(Main).launch {
            GlobalScope.launch {
                work(1)
            }
            GlobalScope.launch {
                work(2)
            }
            parentJob.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Job was cancelled after ${System.currentTimeMillis() - startTime} ms")
                } else {
                    println("Done in ${System.currentTimeMillis() - startTime} ms")
                }
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
