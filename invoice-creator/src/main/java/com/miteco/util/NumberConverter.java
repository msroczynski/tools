package com.miteco.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/* TODO replace with ICU4j
/*
        double num = 7523718.28;
        NumberFormat formatter = new RuleBasedNumberFormat(new Locale("pl"), RuleBasedNumberFormat.CURRENCYSTYLE);
        String result = formatter.format(num);

*/

public class NumberConverter {

    static String[] Units =
            {
                    "zero", "jeden", "dwa", "trzy", "cztery", "pięć", "sześć",
                    "siedem", "osiem", "dziewięć", "dziesięć", "jedenaście",
                    "dwanaście", "trzynaście", "czternaście", "piętnaście",
                    "szesnaście", "siedemnaście", "osiemnaście", "dziewiętnaście"
            };

    static String[] Tens =
            {
                    "dwadzieścia", "trzydzieści", "czterdzieści", "pięćdziesiąt",
                    "sześćdziesiąt", "siedemdziesiąt", "osiemdziesiąt",
                    "dziewięćdziesiąt"
            };

    static String[] Hundreds =
            {
                    "", "sto", "dwieście", "trzysta", "czterysta", "pięćset",
                    "sześćset", "siedemset", "osiemset", "dziewięćset"
            };

    static String[][] OtherUnits =
            {
                    {"tysiąc", "tysiące", "tysięcy"},
                    {"milion", "miliony", "milionów"},
                    {"miliard", "miliardy", "miliardów"}
            };

    // Konwersja małych liczb
    static String SmallValueToWords(int n) {
        if (n == 0) {
            return null;
        }

        StringBuilder valueInWords = new StringBuilder();

        // Konwertuj setki.

        int temp = n / 100;
        if (temp > 0) {
            valueInWords.append(Hundreds[temp]);
            n -= temp * 100;
        }

        // Konwertuj dziesiątki i jedności.

        if (n > 0) {
            if (valueInWords.length() > 0) {
                valueInWords.append(" ");
            }

            if (n < 20) {
                //  Liczby poniżej 20 przekonwertuj na podstawie tablicy jedności.
                valueInWords.append(Units[n]);
            } else {
                //  Większe liczby przekonwertuj łącząc nazwy krotności dziesiątek z nazwami jedności.
                valueInWords.append(Tens[(n / 10) - 2]);
                int lastDigit = n % 10;

                if (lastDigit > 0) {
                    valueInWords.append(" ");
                    valueInWords.append(Units[lastDigit]);
                }
            }
        }
        return valueInWords.toString();
    }

    // Obliczenia dla dużych liczb ... i odmiana prawidłowa ich wartości..
    static int GetBigUnitIndex(long n) {
        int lastDigit = (int) n % 10;

        if ((n >= 10 && (n <= 20 || lastDigit == 0)) ||
                (lastDigit > 4)) {
            return 2;
        }
        return (lastDigit == 1) ? 0 : 1;
    }

    static long ToWords(StringBuilder valueInWords, long n, int level) {
        int smallValue = 0;
        long divisor = (long) Math.pow(1000, (long) level + 1);

        if (divisor <= n) {
            //  Jeśli liczbę da się podzielić przez najbliższą potęgę 1000, kontynuuj rekurencję.
            n = ToWords(valueInWords, n, level + 1);
            smallValue = (int) (n / divisor);

            if (valueInWords.length() > 0) {
                valueInWords.append(" ");
            }

            if (smallValue > 1) {
                valueInWords.append(SmallValueToWords(smallValue));
                valueInWords.append(" ");
            }
            valueInWords.append(OtherUnits[level][GetBigUnitIndex(smallValue)]);
        }

        return n - smallValue * divisor;
    }

    static String ToWords(long value) {
        if (value == 0) {
            // Zero.
            return Units[0];
        }
        StringBuilder valueInWords = new StringBuilder();
        int smallValue = (int) ToWords(valueInWords, value, 0);
        if (smallValue > 0) {
            if (valueInWords.length() > 0) {
                valueInWords.append(" ");
            }
            valueInWords.append(SmallValueToWords(smallValue));
        }
        return valueInWords.toString();
    }


    static long value_pln(double amount) {

        String kwotaString = "" + amount;
        kwotaString = kwotaString.substring(0, kwotaString.indexOf("."));
        Long dzlote = new Long(kwotaString);
        return (long) dzlote;
    }

    static long value_gr(double amount) {
        // Format zmiennych dla uzyskania liczby w formie tekstowej
        DecimalFormat dfx = new DecimalFormat("0.00");
        String szlote = dfx.format(amount);
        // Bez groszy
        String bgzlote = (String) szlote.substring(0, szlote.length() - 3);
        Double dzlote = new Double(bgzlote);
        //Od kwoty z groszami odejmuję kwotę bez.
        Long groszy = (long) (amount * 100 - dzlote * 100);
        return groszy;
    }

    public static String valueToWords(double amount) {
        if (amount < 0) {
            amount = amount * -1;
        }
        String strKwotaSl = "";
        strKwotaSl = ToWords(value_pln(amount)) + " zł " + ToWords(value_gr(amount)) + " gr";
        return strKwotaSl;
    }

    public static String convertValueToString(double value, boolean floatingFraction) {
        if (floatingFraction)
            return convertValueToString("###,###.##", value);
        else
            return convertValueToString("###,###.00", value);
    }


    public static String convertMonthValueToString(int month) {
        return convertValueToString("00", month);
    }

    static String convertValueToString(String pattern, double value) {
        return convertValueToString(pattern, value, Locale.FRANCE);
    }

    static String convertValueToString(String pattern, double value, Locale loc) {
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern(pattern);
        return df.format(value);
    }

    public static String convertValueToString(double value, boolean floatingFraction, String postfix) {
        return convertValueToString(value, floatingFraction) + postfix;
    }


}
