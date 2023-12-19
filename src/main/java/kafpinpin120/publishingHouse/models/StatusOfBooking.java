package kafpinpin120.publishingHouse.models;

public enum StatusOfBooking {
    WAITING("ожидание"),
    EXECUTING("выполняется"),
    DONE("выполнен");

    StatusOfBooking(String s){
    }
}
