package com.project.sublime.eazynames.model;

public class Events {

    public static final int GET_USER_EVENT = 1; //event callback for response from server.


    private  String STRING;
    private boolean STATUS;
    private int TYPE;
    private Object VALUE;

    public Events(int type, boolean status, Object value) {
        this(type,status,value,null);
    }


    public Events(int type, boolean status, Object value, String string) {
        TYPE = type;
        STATUS = status;
        VALUE = value;
        STRING = string;
    }

    public int getType() {
        return TYPE;
    }

    public Object getValue() {
        return VALUE;
    }

    public boolean getStatus() {
        return STATUS;
    }

    public String getString() {
        return STRING;
    }
}