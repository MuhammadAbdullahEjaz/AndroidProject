package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityMainBinding
import androidx.activity.viewModels
import com.example.project.utils.SharedPref

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
        viewModel.getItemsList().observe(this, {newItemList ->
            binding.recyclerView.adapter = ItemAdapter(newItemList)
        })
    }

    override fun onStart() {
        super.onStart()
        SharedPref.setPrefNotFirst(true)
    }
}