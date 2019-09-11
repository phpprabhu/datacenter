package com.ar.businesscard.models.history;

public class ResultItem {
        String date;
        float totalDebit;
        float totalCredit;
        public ResultItem(String date, float totalDebit, float totalCredit){

                this.date = date;
                this.totalDebit = totalDebit;
                this.totalCredit = totalCredit;
        }

public String getDate() {
        return date;
        }

public void setDate(String date) {
        this.date = date;
        }

public float getTotalDebit() {
        return totalDebit;
        }

public void setTotalDebit(float totalDebit) {
        this.totalDebit = totalDebit;
        }

public float getTotalCredit() {
        return totalCredit;
        }

public void setTotalCredit(float totalCredit) {
        this.totalCredit = totalCredit;
        }
        }
