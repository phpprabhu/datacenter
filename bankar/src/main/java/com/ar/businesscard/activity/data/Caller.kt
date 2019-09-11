package com.ar.businesscard.activity.data

import android.content.Context
import android.util.Log
import com.ar.businesscard.data.AccountHistoryData
import com.ar.businesscard.models.history.AccountHistoryResponse
import com.ar.businesscard.models.history.Movement
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class Caller {

    companion object {
        val DEBIT = "debit"
        val CREDIT = "credit"
        val DATE_FORMAT = "yyyyMMdd"
        val MONTH_FORMAT = "MMM"
        val DATE_ONLY_FORMAT = "dd"
    }

    suspend fun parserHistory(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = context.assets.open("history.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val accountHistoryResponse = gson.fromJson(response, AccountHistoryResponse::class.java)
            withContext(Dispatchers.Main) {
                if (accountHistoryResponse.value != null) {
                    val accountHistoryData = getAccountHistoryData(accountHistoryResponse.value.movements)

                    val aggregate = accountHistoryData.groupingBy(AccountHistoryData::executionDate)
                        .aggregate { _, accumulator: AccountHistoryData?, element: AccountHistoryData, _ ->
                            accumulator?.let {
                                it.copy(debit = it.debit + element.debit, credit = it.credit + element.credit)
                            } ?: element
                        }

                    Log.d("aggregate", aggregate.size.toString())

                    fillARAccountData("2000",aggregate, accountHistoryResponse.value.movements)
                    fillARUserData("Ananth KS")
                }
            }
        }
    }

    private fun fillARAccountData(balance: String, aggregate: Map<String, AccountHistoryData>, moment: List<Movement>) {
        var expenseList: MutableList<BarEntry> = ArrayList()
        var incomeList: MutableList<BarEntry> = ArrayList()
        var monthList: MutableList<String> = ArrayList()
        var transactionList: MutableList<Movement> = ArrayList()
        var i = 1f;

        for ((key, value) in aggregate) {
            monthList.add(value.executionDate)
            expenseList.add(BarEntry(i, value.debit))
            incomeList.add(BarEntry(i, value.credit))
            i++
        }

        moment.forEachIndexed { index, element ->
            if (index < 3)
                transactionList.add(element)
        }

        ARData.balance(balance).expenseList(expenseList).incomeList(incomeList).monthList(monthList).transactionList(transactionList)
    }

    private fun fillARUserData(name: String){
        ARData.fName(name)
    }

    private fun getMonth(date: String) : String{
        val date = SimpleDateFormat(DATE_FORMAT).parse(date)
        val format = SimpleDateFormat(MONTH_FORMAT)
        val month = format.format(date)
        return month
    }

    private fun getAccountHistoryData(movement: List<Movement>): List<AccountHistoryData> {
        val myList: MutableList<AccountHistoryData> = mutableListOf<AccountHistoryData>()

        for ((index, item) in movement.withIndex()) {
            myList.add(
                AccountHistoryData(
                    getMonth(item.executionDate),
                    getAmount(item, DEBIT),
                    getAmount(item, CREDIT)
                )
            )
        }

        return myList
    }

    private fun getAmount(item: Movement, type: String): Float {
        if(type.equals(DEBIT) && item.amount.startsWith("-"))
            return item.amount.replace(".", "").replace(",", ".").toFloat()
        else if(type.equals(CREDIT) && !item.amount.startsWith("-"))
            return item.amount.replace(".", "").replace(",", ".").toFloat()
        else
            return 0F
    }
}