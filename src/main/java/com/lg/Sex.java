package com.lg;

public enum Sex {
    MALE, FEMALE;

    public String getEnumString(){
        if (this == MALE) {
            return "Mężczyzna";
        } else {
            return "Kobieta";
        }
    }
}
