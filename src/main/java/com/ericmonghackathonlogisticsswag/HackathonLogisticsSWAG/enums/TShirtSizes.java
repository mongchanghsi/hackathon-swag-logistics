package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG.enums;

public enum TShirtSizes {
    XS ("XS"),
    S ("S"),
    M ("M"),
    L ("L"),
    XL ("XL");

    private final String size;

    TShirtSizes(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return this.size;
    }
}
