package com.example.academic_management_api.dto.auth;

public class SignupRequest {
    private String signupUsername;
    private String signupFullName;
    private String signupEmail;
    private String signupPassword;
    private String signupRole;

    public SignupRequest() {
    }

    public SignupRequest(String signupUsername, String signupFullName, String signupEmail, String signupPassword, String signupRole) {
        this.signupUsername = signupUsername;
        this.signupFullName = signupFullName;
        this.signupEmail = signupEmail;
        this.signupPassword = signupPassword;
        this.signupRole = signupRole;
    }

    public String getSignupUsername() {
        return signupUsername;
    }

    public void setSignupUsername(String signupUsername) {
        this.signupUsername = signupUsername;
    }

    public String getSignupFullName() {
        return signupFullName;
    }

    public void setSignupFullName(String signupFullName) {
        this.signupFullName = signupFullName;
    }

    public String getSignupEmail() {
        return signupEmail;
    }

    public void setSignupEmail(String signupEmail) {
        this.signupEmail = signupEmail;
    }

    public String getSignupPassword() {
        return signupPassword;
    }

    public void setSignupPassword(String signupPassword) {
        this.signupPassword = signupPassword;
    }

    public String getSignupRole() {
        return signupRole;
    }

    public void setSignupRole(String signupRole) {
        this.signupRole = signupRole;
    }
}
