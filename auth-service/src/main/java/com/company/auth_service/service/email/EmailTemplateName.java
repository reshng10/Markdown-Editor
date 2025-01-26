package com.company.auth_service.service.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account");

    private String activateAccount;


    EmailTemplateName(String activateAccount) {
        this.activateAccount=activateAccount;
    }
}
