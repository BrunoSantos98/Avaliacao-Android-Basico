package com.bruno.mingascost

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.bruno.mingascost.databinding.ActivityFindGasInformationBinding
import kotlin.random.Random


class FindGasInformation : AppCompatActivity() {

    private lateinit var binding: ActivityFindGasInformationBinding
    private lateinit var gases: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MinGasCost)
        binding = ActivityFindGasInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gases = binding.gasListInformation

        val idTxt = intent.getIntExtra("idTxt", 0)
        val gasType = intent.getStringExtra("gasType")

        val arrayGases = resources.getStringArray(R.array.gases).filter { it != gasType  }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayGases)
        gases.adapter = adapter

        gases.setOnItemClickListener{ _, _, position, _ ->
            val value = (position + 1) * Random.nextDouble(8.0)
            intent.putExtra("value", value)
            intent.putExtra("itemClicked", arrayGases[position])
            intent.putExtra("idTxt", idTxt)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}