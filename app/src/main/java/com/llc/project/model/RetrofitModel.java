package com.llc.project.model;

/**
 *
 */
public class RetrofitModel {
    String response;

    public RetrofitModel(){
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    Boolean Status;
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
}
