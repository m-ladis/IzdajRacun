package hr.ml.izdajracun.utils;

import androidx.annotation.NonNull;

import java.math.BigInteger;

public class InputFieldValidator {
    public static boolean isOib(@NonNull String oib) {
        try {
            Long.parseLong(oib);
        } catch (NumberFormatException e) {
            return false;
        }

        if (oib.length() != 11) {
            return false;
        } else {

            int oibControlNumber = Integer.parseInt(oib.substring(10, 11));

            int generatedControlNumber = 10;
            for (int i = 0; i < 10; i++) {
                generatedControlNumber = generatedControlNumber + Integer.parseInt(oib.substring(i, i + 1));
                generatedControlNumber = generatedControlNumber % 10;
                if (generatedControlNumber == 0) generatedControlNumber = 10;
                generatedControlNumber *= 2;
                generatedControlNumber = generatedControlNumber % 11;
            }

            generatedControlNumber = 11 - generatedControlNumber;
            if (generatedControlNumber == 10) generatedControlNumber = 0;

            return generatedControlNumber == oibControlNumber;

        }

    }

    public static boolean isHrIban(@NonNull String iban) {
        boolean isHrIban = false;

        if (iban.length() == 21) {
            isHrIban = true;

            String countryCode = iban.substring(0,2);
            if(countryCode.equals("HR")) {
                String checkIban = iban.substring(4) + "1727" + iban.substring(2,4);
                BigInteger bigInteger = new BigInteger(checkIban);
                int reminder = bigInteger.remainder(BigInteger.valueOf(97)).intValue();

                if(reminder == 1){
                    isHrIban = true;
                } else {
                    isHrIban = false;
                }

            } else isHrIban = false;
        }
        return isHrIban;
    }

    public static boolean isAnyStringEmpty(@NonNull String... strings) {
        for (String string : strings) {
            if (string.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPriceValid(int quantity, double unitPrice, double totalPrice){
        return quantity * unitPrice >= totalPrice;
    }
}
