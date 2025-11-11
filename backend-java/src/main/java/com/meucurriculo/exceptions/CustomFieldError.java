package com.meucurriculo.exceptions;

public class CustomFieldError {
    private String name;
    private String message;

    public CustomFieldError(String field, String defaultMessage) {
        this.name = field;
        this.message = defaultMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}