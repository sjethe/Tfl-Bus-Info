package com.example.tflbusinfo.exception;

public class TflBusInfoException extends RuntimeException{

    private String message;
    private String detailedMessage;

    public TflBusInfoException(String message){
        this.message = message;
    }

    public TflBusInfoException(String message, Throwable t){
        this.message = message;
        this.detailedMessage = t.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }
}