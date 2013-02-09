package com.miteco.util;

import java.util.Calendar;


public class Holidays
{


    /* == 2012 ==
    1 stycznia  (niedziela)	Nowy Rok, Świętej Bożej Rodzicielki
    6 stycznia  (piątek)	Trzech Króli (Objawienie Pańskie)
    8 kwietnia  (niedziela)	Wielkanoc
    9 kwietnia  (poniedziałek)	Poniedziałek Wielkanocny
    1 maja  (wtorek)	Święto Pracy
    3 maja  (czwartek)	Święto Konstytucji 3 Maja
    27 maja  (niedziela)	Zesłanie Ducha Świętego (Zielone Świątki)
    7 czerwca  (czwartek)	Boże Ciało
    15 sierpnia  (Środa)	Święto Wojska Polskiego, Wniebowzięcie Najświętszej Maryi Panny
    1 listopada  (czwartek)	Wszystkich Świętych
    11 listopada  (niedziela)	Święto Niepodległości
    25 grudnia  (wtorek)	Boże Narodzenie (pierwszy dzień)
    26 grudnia  (Środa)	Boże Narodzenie (drugi dzień)
    */


    public static java.util.Calendar AbrahamLincolnsBirthday (int nYear){
        int nMonth = 1; // February
        // February 12th

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 12);
        return cal;
    }

    public static java.util.Calendar ChristmasDay (int nYear){
        int nMonth = 11; // Decmeber
        // December 25th
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 25);
        return cal;
    }

    public static java.util.Calendar ChristmasDayObserved (int nYear)
    {
        int nX;
        int nMonth = 11; // December
        java.util.Calendar cal;

        cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 25);
        nX = cal.get(Calendar.DAY_OF_WEEK);
        switch(nX)
        {
            case 0 : {// Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 26);
                return cal;
            }
            case 1 : // Monday
            case 2 : // Tuesday
            case 3 : // Wednesday
            case 4 : // Thrusday
            case 5 :{ // Friday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 25);
                return cal;
            }
            default :{
                // Saturday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 24);
                return cal;
            }
        }
    }
    public static java.util.Calendar ColumbusDayObserved (int nYear)
    {
        // Second Monday in October
        int nX;
        int nMonth = 9; // October
        java.util.Calendar cal;

        cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);
        nX = cal.get(Calendar.DAY_OF_WEEK);
        switch(nX)
        {
            case 0 : {// Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 9);
                return cal;
            }
            case 1 : {// Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 15);
                return cal;
            }
            case 2 : // Tuesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 14);
                return cal;
            }
            case 3 : // Wednesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 13);
                return cal;
            }
            case 4 : // Thrusday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 12);
                return cal;
            }
            case 5 : // Friday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 11);
                return cal;
            }
            default : // Saturday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 10);
                return cal;
            }
        }

    }
    public static java.util.Calendar EasterMonday (int nYear)
    {
        int nEasterMonth = 0;
        int nEasterDay   = 0;
        int nMonthMarch  = 2; // March
        int nMonthApril  = 3; // April
        java.util.Calendar cEasterSunday = EasterSunday(nYear);
        nEasterMonth = cEasterSunday.get(Calendar.MONTH);
        nEasterDay = cEasterSunday.get(Calendar.DAY_OF_MONTH);
        if (nEasterMonth == nMonthMarch || nEasterDay == 31){
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(nYear, nMonthApril, 1);
            return cal;
        }else{
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(nYear, nEasterMonth, ++nEasterDay);
            return cal;
        }
    }
    public static java.util.Calendar EasterSunday(int nYear)
    {
/*  Calculate Easter Sunday

  Written by Gregory N. Mirsky

  Source: 2nd Edition by Peter Duffett-Smith. It was originally from
  Butcher's Ecclesiastical Calendar, published in 1876. This
  algorithm has also been published in the 1922 book General
  Astronomy by Spencer Jones; in The Journal of the British
  Astronomical Association (Vol.88, page 91, December 1977); and in
  Astronomical Algorithms (1991) by Jean Meeus.

  This algorithm holds for any year in the Gregorian Calendar, which
  (of course) means years including and after 1583.

        a=year%19
        b=year/100
        c=year%100
        d=b/4
        e=b%4
        f=(b+8)/25
        g=(b-f+1)/3
        h=(19*a+b-d-g+15)%30
        i=c/4
        k=c%4
        l=(32+2*e+2*i-h-k)%7
        m=(a+11*h+22*l)/451
        Easter Month =(h+l-7*m+114)/31  [3=March, 4=April]
        p=(h+l-7*m+114)%31
        Easter Date=p+1     (date in Easter Month)

  Note: Integer truncation is already factored into the
  calculations. Using higher percision variables will cause
  inaccurate calculations.
*/

        int nA      = 0;
        int nB      = 0;
        int nC      = 0;
        int nD      = 0;
        int nE      = 0;
        int nF      = 0;
        int nG      = 0;
        int nH      = 0;
        int nI      = 0;
        int nK      = 0;
        int nL      = 0;
        int nM      = 0;
        int nP      = 0;
        int nEasterMonth  = 0;
        int nEasterDay    = 0;

        // Calculate Easter
        if (nYear < 1900)
        {
            // if year is in java format put it into standard
            // format for the calculation
            nYear += 1900;
        }
        nA = nYear % 19;
        nB = nYear / 100;
        nC = nYear % 100;
        nD = nB / 4;
        nE = nB % 4;
        nF = (nB + 8) / 25;
        nG = (nB - nF + 1) / 3;
        nH = (19 * nA + nB - nD - nG + 15) % 30;
        nI = nC / 4;
        nK = nC % 4;
        nL = (32 + 2 * nE + 2 * nI - nH - nK) % 7;
        nM=  (nA + 11 * nH + 22 * nL) / 451;

        //  [3=March, 4=April]
        nEasterMonth = (nH + nL - 7 * nM + 114) / 31;
        --nEasterMonth;
        nP = (nH + nL - 7 * nM + 114) % 31;

        // Date in Easter Month.
        nEasterDay = nP + 1;

        // Uncorrect for our earlier correction.
        nYear -= 1900;

        // Populate the date object...
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nEasterMonth, nEasterDay);
        return cal;
    }
    public static java.util.Calendar GoodFridayObserved(int nYear)
    {
        // Get Easter Sunday and subtract two days
        int nEasterMonth  = 0;
        int nEasterDay    = 0;
        int nGoodFridayMonth  = 0;
        int nGoodFridayDay  = 0;
        java.util.Calendar cEasterSunday;

        cEasterSunday = EasterSunday(nYear);
        nEasterMonth = cEasterSunday.get(Calendar.MONTH);
        nEasterDay = cEasterSunday.get(Calendar.DAY_OF_MONTH);
        if (nEasterDay <= 3 && nEasterMonth == 3){ // Check if <= April 3rd

            switch(nEasterDay){
                case 3 :
                    nGoodFridayMonth = nEasterMonth - 1;
                    nGoodFridayDay   = nEasterDay - 2;
                    break;
                case 2 :
                    nGoodFridayMonth = nEasterMonth - 1;
                    nGoodFridayDay   = 31;
                    break;
                case 1 :
                    nGoodFridayMonth = nEasterMonth - 1;
                    nGoodFridayDay   = 31;
                    break;
                default:
                    nGoodFridayMonth = nEasterMonth;
                    nGoodFridayDay   = nEasterDay - 2;
            }
        }else{
            nGoodFridayMonth = nEasterMonth;
            nGoodFridayDay   = nEasterDay - 2;
        }

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nGoodFridayMonth, nGoodFridayDay);
        return cal;
    }
    public static java.util.Calendar Halloween (int nYear){
        int nMonth = 9;
        // October 31st

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 31);
        return cal;
    }

    public static java.util.Calendar IndependenceDay (int nYear)
    {
        int nMonth = 6; // July
        // July 4th

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 4);
        return cal;
    }
    public static java.util.Calendar IndependenceDayObserved (int nYear){
        int nX;
        int nMonth = 6; // July

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 4);

        nX = cal.get(Calendar.DAY_OF_WEEK);
        switch(nX){
            case 0 : // Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 5);
                return cal;
            case 1 : // Monday
            case 2 : // Tuesday
            case 3 : // Wednesday
            case 4 : // Thrusday
            case 5 : // Friday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 4);
                return cal;
            default :
                // Saturday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 3);
                return cal;
        }
    }
    public static java.util.Calendar LaborDayObserved (int nYear){
        // The first Monday in September
        int nX;
        int nMonth = 8; // September
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, 9, 1);

        nX = cal.get(Calendar.DAY_OF_WEEK);

        switch(nX){
            case 0 : // Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 2);
                return cal;
            case 1 : // Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 7);
                return cal;
            case 2 : // Tuesday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 6);
                return cal;
            case 3 : // Wednesday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 5);
                return cal;
            case 4 : // Thrusday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 4);
                return cal;
            case 5 : // Friday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 3);
                return cal;
            default : // Saturday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 2);
                return cal;
        }
    }
    public java.util.Calendar MartinLutherKingObserved (int nYear){
        // Third Monday in January
        int nX;
        int nMonth = 0; // January
        java.util.Calendar cal;

        cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);
        nX = cal.get(Calendar.DAY_OF_WEEK);

        switch(nX) {
            case 0 : {// Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 16);
                return cal;
            }
            case 1 : {// Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 15);
                return cal;
            }
            case 2 : // Tuesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 21);
                return cal;
            }
            case 3 : // Wednesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 20);
                return cal;
            }
            case 4 : // Thrusday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 19);
                return cal;
            }
            case 5 : // Friday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 18);
                return cal;
            }
            default : // Saturday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 17);
                return cal;
            }

        }
    }
    public static java.util.Calendar MemorialDayObserved (int nYear){
        // Last Monday in May
        int nX;
        int nMonth = 4; //May
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 31);

        nX = cal.get(Calendar.DAY_OF_WEEK);


        switch(nX){
            case 0 : // Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 25);
                return cal;
            case 1 : // Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 31);
                return cal;
            case 2 : // Tuesday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 30);
                return cal;
            case 3 : // Wednesday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 29);
                return cal;
            case 4 : // Thrusday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 28);
                return cal;
            case 5 : // Friday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 27);
                return cal;
            default : // Saturday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 26);
                return cal;

        }
    }
    public static java.util.Calendar  NewYearsDay (int nYear){
        // January 1st
        int nMonth = 0; // January

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);

        return cal;
    }
    public static java.util.Calendar NewYearsDayObserved (int nYear){
        int nX;
        int nMonth = 0;     // January
        int nMonthDecember = 11;  // December

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);

        nX = cal.get(Calendar.DAY_OF_WEEK);

        if (nYear > 1900)
            nYear -= 1900;

        switch(nX){
            case 0 : // Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 2);
                return cal;
            case 1 : // Monday
            case 2 : // Tuesday
            case 3 : // Wednesday
            case 4 : // Thrusday
            case 5 : // Friday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 1);
                return cal;
            default :
                // Saturday, then observe on friday of previous year
                cal = java.util.Calendar.getInstance();
                cal.set(--nYear, nMonthDecember, 31);
                return cal;
        }
    }
    public static java.util.Calendar PresidentsDayObserved (int nYear){
        // Third Monday in February
        int nX;
        int nMonth = 1; // February

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);

        nX = cal.get(Calendar.DAY_OF_WEEK);

        switch(nX){
            case 0 : {// Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 16);
                return cal;
            }
            case 1 : {// Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 15);
                return cal;
            }
            case 2 : // Tuesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 21);
                return cal;
            }
            case 3 : // Wednesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 20);
                return cal;
            }
            case 4 : // Thrusday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 19);
                return cal;
            }
            case 5 : // Friday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 18);
                return cal;
            }
            default : // Saturday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 17);
                return cal;
            }
        }
    }
    public static java.util.Calendar ThanksgivingObserved(int nYear){
        int nX;
        int nMonth = 10; // November
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);

        nX = cal.get(Calendar.DAY_OF_WEEK);
        switch(nX){

            case 0 : {// Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 26);
                return cal;
            }
            case 1 : {// Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 25);
                return cal;
            }
            case 2 : // Tuesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 24);
                return cal;
            }
            case 3 : // Wednesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 23);
                return cal;
            }
            case 4 : // Thrusday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 22);
                return cal;
            }
            case 5 : // Friday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 28);
                return cal;
            }
            default : // Saturday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 27);
                return cal;
            }
        }
    }
    public static java.util.Calendar USElectionDay (int nYear){
        // First Tuesday in November
        int nX;
        int nMonth = 10; // November
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 1);
        nX = cal.get(Calendar.DAY_OF_WEEK);
        switch(nX){
            case 0 : {// Sunday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 3);
                return cal;
            }
            case 1 : {// Monday
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 2);
                return cal;
            }
            case 2 : // Tuesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 1);
                return cal;
            }
            case 3 : // Wednesday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 7);
                return cal;
            }
            case 4 : // Thrusday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 6);
                return cal;
            }
            case 5 : // Friday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 5);
                return cal;
            }
            default : // Saturday
            {
                cal = java.util.Calendar.getInstance();
                cal.set(nYear, nMonth, 4);
                return cal;
            }
        }
    }
    public static java.util.Calendar ValentinesDay (int nYear){
        int nMonth = 1; // February
        // February 14th

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 14);
        return cal;
    }
    public static java.util.Calendar VeteransDayObserved (int nYear){
        //November 11th
        int nMonth = 10; // November
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(nYear, nMonth, 11);
        return cal;
    }
    public static String getClassInfo()
    {
        return ("Name: Holidays\r\n" +
                "Author: Gregory N. Mirsky\r\n" +
                "Updated: John D. Mitchell\r\n" +
                "Version 1.02\r\n" +
                "Copyright 1997, All rights reserved.");
    }
}


/*

   public static int getWorkingDays(Date fromDate, Date toDate) {
      Calendar calFrom = Calendar.getInstance();
      Calendar calTo = Calendar.getInstance();
      calFrom.setTime(fromDate);
      calTo.setTime(toDate);
      calTo.add(Calendar.DATE, 1);
      int noOfWorkingDays = 0;
      do {
         if (calFrom.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
               && calFrom.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            noOfWorkingDays += 1;

         }
         calFrom.add(Calendar.DATE, 1);
      } while (calFrom.getTime().before(calTo.getTime()));
      return noOfWorkingDays;
   }

*/