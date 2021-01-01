package com.currencyexchangeratetracker.filter;
public class ErrorResponse {
    private String status;
    private String title;
    private String description;

    public ErrorResponse() {
    }

    public ErrorResponse(String status, String title, String description) {
        this.status = status;
        this.title = title;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
