package org.artem.projects.proteincrudexample.exceptions;

public class ProteinNotFoundException extends RuntimeException {
    public ProteinNotFoundException(String message) {
        super(message);
    }
}
