package kafpinpin120.publishingHouse.exceptions;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
