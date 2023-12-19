package kafpinpin120.publishingHouse.controllers;

import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.dtos.BookingAcceptDTO;
import kafpinpin120.publishingHouse.dtos.BookingSimpleSendDTO;
import kafpinpin120.publishingHouse.services.BookingService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ValidateInputService validateInputService;

    public BookingController(BookingService bookingService, ValidateInputService validateInputService) {
        this.bookingService = bookingService;
        this.validateInputService = validateInputService;
    }


    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid BookingAcceptDTO bookingAcceptDTO, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            bookingService.add(bookingAcceptDTO);
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка добавления заказа", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Заказ успешно добавлен!", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "page",required = false) Integer page, @RequestParam(name = "userId", required = false) Long userId, @RequestParam(name = "status", required = false) String status, @RequestParam(name = "bookingId",required = false) Long bookingId){
        List<BookingSimpleSendDTO> bookings;
        try{
            bookings = bookingService.get(page,bookingId,userId, status);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
