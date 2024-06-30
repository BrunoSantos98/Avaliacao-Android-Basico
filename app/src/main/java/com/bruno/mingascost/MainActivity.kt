package com.bruno.mingascost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bruno.mingascost.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var gasType01 = ""
    private var gasType02 = ""
    private val df = DecimalFormat("0.00")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MinGasCost)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchIcon01 = binding.searchIcon01
        val searchIcon02 = binding.searchIcon02
        val calBtn = binding.btnCalcula
        val clearBtn = binding.btnClear
        val txtView01 = binding.tvResult01
        val txtView02 = binding.tvResult02

        txtView01.alpha = 0.0f
        txtView02.alpha = 0.0f

        if(gasType01.isNotEmpty()){

            txtView01.text = getString(R.string.mpg_cost, "$gasType01: $")

        }else{
            txtView01.text = getString(R.string.mpg_cost, "")
        }

        if(gasType02.isNotEmpty()){
            txtView02.text = getString(R.string.mpg_cost, "$gasType02: $")
        }else{
            txtView02.text = getString(R.string.mpg_cost, "")
        }

        searchIcon01.setOnClickListener {
            searchValues(binding.txtInput01.id)

        }

        searchIcon02.setOnClickListener {
            searchValues(binding.txtInput02.id)
        }

        binding.txtInput01.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifyEmptyField()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.txtInput02.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifyEmptyField()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.txtInputValueFuel01.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifyEmptyField()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.txtInputValueFuel02.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifyEmptyField()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        calBtn.setOnClickListener {
            val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            if(imm.isActive){
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }

            verifyFieldsAndCalculate()
        }

        clearBtn.setOnClickListener {
            clearFields()
        }

    }

    private fun verifyEmptyField() {
        val hasText = binding.txtInput01.editText!!.text.isNotEmpty() ||
                binding.txtInput02.editText!!.text.isNotEmpty() ||
                binding.txtInputValueFuel01.editText!!.text.isNotEmpty() ||
                binding.txtInputValueFuel02.editText!!.text.isNotEmpty()

        binding.btnClear.isEnabled = hasText
    }

    private fun clearFields() {
        binding.txtInput01.editText!!.setText("")
        binding.txtInput02.editText!!.setText("")
        binding.txtInputValueFuel01.editText!!.setText("")
        binding.txtInputValueFuel02.editText!!.setText("")
    }

    private fun verifyFieldsAndCalculate() {
        val arrayFields = arrayOf(binding.txtInput01,binding.txtInput02,
                                  binding.txtInputValueFuel01, binding.txtInputValueFuel02)

        for(field in arrayFields){
            if(field.editText?.text?.isEmpty() == true){
                field.error = getString(R.string.emptyField)
                field.requestFocus()
                return
            }else{
                field.error = null
            }
        }

        calculateMostCheaper()
    }

    private fun calculateMostCheaper() {
        val firstCost: Double = binding.txtInputValueFuel01.editText!!.text.toString().replace(",", ".").toDouble() /
                binding.txtInput01.editText!!.text.toString().replace(",", ".").toDouble()
        val secondCost: Double = binding.txtInputValueFuel02.editText!!.text.toString().replace(",", ".").toDouble() /
                binding.txtInput02.editText!!.text.toString().replace(",", ".").toDouble()

        val firstCostFormated = df.format(firstCost)
        val secondCostFormated = df.format(secondCost)

        if(Locale.getDefault().language == "pt"){
            binding.tvResult01.text = getString(R.string.mpg_cost, "$gasType01: R$ ${firstCostFormated.toString().replace(".", ",")}")
            binding.tvResult02.text = getString(R.string.mpg_cost, "$gasType02: R$ ${secondCostFormated.toString().replace(".", ",")}")
        }else{
            binding.tvResult01.text = getString(R.string.mpg_cost, "$gasType01: $ ${binding.txtInput01.editText!!.text} dollars")
            binding.tvResult02.text = getString(R.string.mpg_cost, "$gasType02: $ ${binding.txtInput02.editText!!.text} dollars")
        }



        binding.tvResult01.alpha = 1f
        binding.tvResult02.alpha = 1f
    }


    private fun searchValues(textInput: Int) {
        val intent = Intent(this, FindGasInformation::class.java)
        intent.putExtra("idTxt", textInput)

        if(findViewById<TextInputLayout>(textInput) == binding.txtInput01 && gasType02.isNotEmpty()){
            intent.putExtra("gasType", gasType02)
        }else{
            intent.putExtra("gasType", gasType01)
        }

        getResult.launch(intent)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK && it.data != null) {
            val textLayout = findViewById<TextInputLayout>(it.data!!.getIntExtra("idTxt", 0))
            val doubleResult = it.data!!.getDoubleExtra("value", 0.00)

            textLayout.editText?.setText(df.format(doubleResult).toString())

            val itemClicked = it.data!!.getStringExtra("itemClicked")

            if(textLayout == binding.txtInput01){
                gasType01 = itemClicked.toString()
            }else{
                gasType02 = itemClicked.toString()
            }
        }
    }
}
