package com.fancytank.gamegen.programming.data;

import java.io.Serializable;

public enum ValueType implements Serializable {
    BOOLEAN(),
    ANY(),
    COLOR(MethodType.COLOR_SETTER),
    NUMBER(),
    INT_NUMBER(),
    CLASS_NAME(MethodType.BLOCK_SETTER),
    METHOD(),
    VARIABLE(),
    SCREEN(MethodType.SCREEN_SWAPPER);;

    private static final long serialVersionUID = 1233613063064496950L;
    MethodType expectedMethod;

    ValueType() {
    }

    ValueType(MethodType expectedMethod) {
        this.expectedMethod = expectedMethod;
    }

    static ValueType[] usedValues;
    static String[] valueNames;

}
