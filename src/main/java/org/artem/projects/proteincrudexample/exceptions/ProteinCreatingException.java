package org.artem.projects.proteincrudexample.exceptions;

public class ProteinCreatingException extends RuntimeException {
    public ProteinCreatingException(String proteinNotCreated) {
        super(proteinNotCreated);
    }
}
