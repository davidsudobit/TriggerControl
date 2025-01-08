package com.TriggerControl.Util;

import lombok.Getter;

@Getter
public enum RequestState {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    RETRIEVE("RETRIEVE");

    private final String state;

    private RequestState(String state){
        this.state = state;
    }
}