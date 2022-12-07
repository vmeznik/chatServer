package chatServer.model;

import java.io.Serializable;

public class RequestConfirmation implements Serializable {
    //This class is used to confirm if request was successful or not , if not it provides error message
    private boolean success;
    private String error;

    public RequestConfirmation(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public RequestConfirmation() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String error) {
        this.error = error;
    }
}

