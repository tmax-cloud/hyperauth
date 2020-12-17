package com.tmax.hyperauth.authenticator.secretQuestion.credential.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="mailto:alistair.doswald@elca.ch">Alistair Doswald</a>
 * @version $Revision: 1 $
 */
public class SecretQuestionCredentialData {

    private final String question;

    @JsonCreator
    public SecretQuestionCredentialData(@JsonProperty("question") String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
