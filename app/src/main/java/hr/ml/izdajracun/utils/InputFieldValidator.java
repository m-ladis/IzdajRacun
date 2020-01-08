package hr.ml.izdajracun.utils;

import androidx.annotation.NonNull;

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
        }
        return isHrIban;
    }

    public static boolean isAnyStringEmpty(@NonNull String... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
