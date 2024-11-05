package com.formation_full_stack_java_angular.rest_api.exceptions;

public class PasUtilisateurException extends Throwable {
    public PasUtilisateurException(String s) {
        super(s);
    }

    public PasUtilisateurException(String s, Throwable id) {
        super(s,id);
    }
}
