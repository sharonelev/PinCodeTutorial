package com.appsbysha.pincodetutorial

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {
    private lateinit var numTemp: String


    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.initButton).setOnClickListener(this)
        findViewById<View>(R.id.verifyButton).setOnClickListener(this)


        //create array
        val layout: LinearLayout = findViewById(R.id.codeLayout)
        for (index in 0 until (layout.childCount)) {
            val view: View = layout.getChildAt(index)
            if (view is EditText) {
                editTextArray.add(index, view)
                editTextArray[index].addTextChangedListener(this)
                editTextArray[index].setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                        //this is for backspace
                        if (index != 0) {
                            editTextArray[index - 1].requestFocus()
                            editTextArray[index - 1].setSelection(editTextArray[index - 1].length())
                        }
                    }
                    false
                }
            }
        }

        editTextArray[0].requestFocus()


    }


    private fun enableCodeEditTexts(enable: Boolean) {

        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable

    }

    private fun initCodeEditTexts() {

        for (i in 0 until editTextArray.size)
            editTextArray[i].setText("")

        editTextArray[0].requestFocus()

    }


    //Code auto set
    fun setVerificationCode(verificationCode: String) {

        for (i in 0 until editTextArray.size)
            editTextArray[i].setText(verificationCode.substring(i, i))
    }


    override fun afterTextChanged(s: Editable) {

        (0 until editTextArray.size)
            .forEach { i ->
                if (s === editTextArray[i].editableText) {
                    //if more than 1 char
                    if (s.isBlank()) {
                        return
                    }
                    if (s.length >= 2) {
                        val newTemp = s.toString().substring(s.length - 1, s.length)
                        if (newTemp != numTemp) {
                            editTextArray[i].setText(newTemp)
                        } else {
                            editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                        }
                    } else if (i != editTextArray.size - 1) { //1 char
                        editTextArray[i + 1].requestFocus()
                        editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                        return
                    } else

                    //will test code the moment the last character is inserted and all digits have a number
                        verifyCode(testCodeValidity())


                }
            }

    }


    private fun testCodeValidity(): String {
        var verificationCode = ""
        for (j in editTextArray.indices) {
            val digit = editTextArray[j].text.toString().trim { it <= ' ' }
            verificationCode += digit

        }
        if (verificationCode.trim { it <= ' ' }.length == NUM_OF_DIGITS) {
            return verificationCode
        }
        return ""
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.initButton -> {
                initCodeEditTexts()
                enableCodeEditTexts(true)
            }

            R.id.verifyButton ->
                verifyCode(testCodeValidity())
        }
    }


    private fun verifyCode(verificationCode: String) {
        if (verificationCode.isNotEmpty())
        //check code
            enableCodeEditTexts(false)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        numTemp = s.toString()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    companion object {

        const val NUM_OF_DIGITS = 4
    }

}
