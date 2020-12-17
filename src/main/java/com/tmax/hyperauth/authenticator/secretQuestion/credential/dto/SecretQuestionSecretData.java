package com.tmax.hyperauth.authenticator.secretQuestion.credential.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="mailto:alistair.doswald@elca.ch">Alistair Doswald</a>
 * @version $Revision: 1 $
 */
public class SecretQuestionSecretData {

    private final String answer;

    @JsonCreator
    public SecretQuestionSecretData(@JsonProperty("answer") String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}
