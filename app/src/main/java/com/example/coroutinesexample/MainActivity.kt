package com.example.coroutinesexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MainActivity : AppCompatActivity() {
    private val TAG = "Debug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main()
    }

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Exception throw in one of the children: $throwable")
    }

    fun main() {
        val parentJob = CoroutineScope(IO).launch(handler) {
            // --- JOB A ---
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA: $resultA")
            }
            jobA.invokeOnCompletion {
                if (it != null) {
                    println("Error getting resultA: $it")
                }
            }
            // --- JOB B ---
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB: $resultB")
            }
            jobB.invokeOnCompletion {
                if (it != null) {
                    println("Error getting resultB: $it")
                }
            }
            // --- JOB C ---
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC: $resultC")
            }
            jobC.invokeOnCompletion {
                if (it != null) {
                    println("Error getting resultC: $it")
                }
            }
        }
        parentJob.invokeOnCompletion {
            if (it != null) {
                println("Parent Job failed: $it")
            } else {
                println("Parent Job SUCCESS")
            }
        }
    }

    suspend fun getResult(number: Int): Int {
        delay(number * 500L)
        if (number == 2) {
            //throw Exception("Error getting result for number $number")
            //cancel(CancellationException("Error getting result for number: $number"))
            throw CancellationException("Error getting result for number: $number")
        }
        return number * 2
    }

    private fun println(message: String) {
        Log.d(TAG, message)
    }
}
