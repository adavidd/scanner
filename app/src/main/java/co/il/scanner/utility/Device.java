package co.il.scanner.utility;

import android.os.Build;
import android.text.TextUtils;

public  class Device {
    public  String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }
    public  String getDevicemManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }
    public  String getDevicemModel() {
        String manufacturer = Build.MODEL;
        return manufacturer;
    }
    private  String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
}
