package org.artem.projects.proteincrudexample.exceptions;

public class ProteinUpdatingException extends RuntimeException {
    public ProteinUpdatingException(String proteinNotUpdated) {
        super(proteinNotUpdated);
    }
}
