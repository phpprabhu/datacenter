package com.ar.businesscard.activity.data

import com.ar.businesscard.models.history.Movement
import com.github.mikephil.charting.data.BarEntry

object ARData {
    private var fName : String = ""
    private var lName : String = ""
    private var balance : String = ""
    private lateinit var expenseList : List<BarEntry>
    private lateinit var incomeList : List<BarEntry>
    private lateinit var monthList : List<String>
    private lateinit var transactionList : List<Movement>


    fun fName(fName: String) = apply { this.fName = fName }
    fun lName(lName: String) = apply { this.lName = lName }
    fun balance(balance: String) = apply { this.balance = balance }
    fun expenseList(expenseList: List<BarEntry>) = apply { this.expenseList = expenseList }
    fun incomeList(incomeList: List<BarEntry>) = apply { this.incomeList = incomeList }
    fun monthList(monthList: List<String>) = apply { this.monthList = monthList }
    fun transactionList(transactionList: List<Movement>) = apply { this.transactionList = transactionList }

    fun getFName(): String{
        return fName
    }

    fun getBalance(): String{
        return balance
    }

    fun getLName(): String{
        return lName
    }

    fun getExpenseList(): List<BarEntry> {
        return expenseList
    }

    fun getIncomeList(): List<BarEntry> {
        return incomeList
    }

    fun getMonthList(): List<String> {
        return monthList
    }

    fun getTransactionList(): List<Movement> {
        return transactionList
    }

}