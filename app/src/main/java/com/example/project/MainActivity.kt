package com.example.project

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityMainBinding
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    object Main{
        val SharedPreferenceFile:String = "Pref"
        val FIRST = "notfirst"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel:ItemViewModel by viewModels{ItemViewModelFactory(
        (this.application as ItemApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getItemsList()
        viewModel.data.observe(this, {newItemList ->
            binding.recyclerView.adapter = ItemAdapter(newItemList)
            Log.d("main", "In  observer")
        })
    }

    override fun onPause() {
        super.onPause()
        val sharedPreference = this.getSharedPreferences(Main.SharedPreferenceFile,Context.MODE_PRIVATE)
        val sharedPreferenceEditor:SharedPreferences.Editor = sharedPreference.edit()
        sharedPreferenceEditor.putBoolean(Main.FIRST, true)
        sharedPreferenceEditor.apply()
    }
}