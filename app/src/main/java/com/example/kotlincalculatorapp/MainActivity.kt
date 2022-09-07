package com.example.kotlincalculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation= false
    private var canAddDecimal= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View) {
        if(view is Button)
        {
            if (view.text==".")
            {
                if(canAddDecimal)
                    workingTV.append(view.text)
                canAddDecimal=false
            }

            else
                workingTV.append(view.text)
            canAddOperation=true
        }
    }

    fun operationAction(view: View) {
        if(view is Button && canAddOperation)
        {
            workingTV.append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }


    fun allClearAction(view: View) {
        workingTV.text=""
        resultsTV.text=""

    }

    fun backspaceAction(view: View) {
        val length=workingTV.length()
        if(length>0)
            workingTV.text=workingTV.text.subSequence(0,length-1)
    }

    fun equalsAction(view: View) {
        resultsTV.text=calculateResults()
    }

    private fun calculateResults(): String {
        val digitOperators=digitOperator()
        if (digitOperators.isEmpty())
            return ""

        val timeDivision=timeDivisionCalc(digitOperators)
        if (timeDivision.isEmpty())
            return ""

        val result=addsubtractcalc(timeDivision)

        return result.toString()
    }

    private fun addsubtractcalc(passedList: MutableList<Any>): Float {
        var result=passedList[0] as Float
        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i!=passedList.lastIndex)
            {
                val operator=passedList[i]
                val nextdigit=passedList[i+1] as Float
                if (operator =='+')
                    result += nextdigit
                if (operator =='-')
                    result -= nextdigit
            }
        }

        return result
    }

    private fun timeDivisionCalc(passedlist: MutableList<Any>): MutableList<Any> {
        var list=passedlist
        while(list.contains('x') || list.contains('/'))
        {
            list=calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList= mutableListOf<Any>()
        var restartIndex=passedList.size

        for (i in passedList.indices)
        {
            if(passedList[i] is Char && i!=passedList.lastIndex && i<restartIndex)
            {
                val operator=passedList[i]
                val prevdigit=passedList[i-1] as Float
                val nextdigit=passedList[i+1] as Float
                when(operator)
                {
                    'x'->
                    {
                        newList.add(prevdigit * nextdigit)
                        restartIndex=i+1
                    }

                    '/'->
                    {
                        newList.add(prevdigit / nextdigit)
                        restartIndex=i+1
                    }
                    else ->
                    {
                        newList.add(prevdigit)
                    }
                }
            }
            if(i>restartIndex)
            newList.add(passedList[i])
        }

        return newList
    }


    private fun digitOperator(): MutableList<Any>
    {
        val list= mutableListOf<Any>()
        var currDigit=""
        for(character in workingTV.text)
        {
            if(character.isDigit() || character=='.')
                currDigit+=character
            else
            {
                list.add(currDigit.toFloat())
                currDigit=""
                list.add(character)
            }
        }

        if (currDigit!="")
            list.add(currDigit.toFloat())

        return list
    }
}