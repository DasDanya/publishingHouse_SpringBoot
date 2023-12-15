package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.dtos.BookingSimpleSendDTO;
import kafpinpin120.publishingHouse.models.Booking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {


    private List<BookingSimpleSendDTO> getBookingSendDTOS(List<Booking> bookings){

        List<BookingSimpleSendDTO> bookingSimpleSendDTOS = new ArrayList<>();
        for(Booking booking: bookings){
            bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(booking));
        }

        return bookingSimpleSendDTOS;
    }

    public BookingSimpleSendDTO getBookingSimpleSendDTO(Booking booking){
        return new BookingSimpleSendDTO(booking.getId(),booking.getCost());
    }
}
