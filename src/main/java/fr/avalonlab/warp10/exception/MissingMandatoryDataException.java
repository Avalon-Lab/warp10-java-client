package fr.avalonlab.warp10.exception;

public class MissingMandatoryDataException extends RuntimeException {

    public MissingMandatoryDataException(String dataName) {
        super(String.format("The data '%s' was not set.", dataName));
    }

}
