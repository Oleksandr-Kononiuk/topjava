package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String typeMessage;
    private final String[] details;

    public ErrorInfo(CharSequence typeMessage, String ... details) {
        this.typeMessage = typeMessage.toString();
        this.details = details;
    }

}