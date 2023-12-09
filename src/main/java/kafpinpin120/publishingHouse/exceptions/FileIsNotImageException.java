package kafpinpin120.publishingHouse.exceptions;

public class FileIsNotImageException extends RuntimeException {

    public FileIsNotImageException(String errorMessage){
        super(errorMessage);
    }
}
