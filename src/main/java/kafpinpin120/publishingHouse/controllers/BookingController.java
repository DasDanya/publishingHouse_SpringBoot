package kafpinpin120.publishingHouse.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.dtos.BookingAcceptDTO;
import kafpinpin120.publishingHouse.dtos.BookingSendDTO;
import kafpinpin120.publishingHouse.dtos.BookingSimpleSendDTO;
import kafpinpin120.publishingHouse.models.Booking;
import kafpinpin120.publishingHouse.services.BookingService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") long id){
        BookingSendDTO bookingSendDTO;
        try{
            bookingSendDTO = bookingService.getOne(id);
        }catch (Exception e){
            if(e instanceof EntityNotFoundException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ошибка получения заказа с id = '"+id+"'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(bookingSendDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id){
        try{
            Optional<Booking> bookingInDb = bookingService.findById(id);
            if(bookingInDb.isEmpty()){
                return new ResponseEntity<>("Заказ не найден", HttpStatus.BAD_REQUEST);
            }else{
                Booking booking = bookingInDb.get();
                if(!booking.getStatus().equals("ожидание")){
                    return new ResponseEntity<>("Удалять можно заказ со статусом \"ожидание\"",HttpStatus.CONFLICT);
                }

                bookingService.delete(booking);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка удаления заказа", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Заказ успешно удалён!", HttpStatus.NO_CONTENT);
    }


    @PutMapping("update/{id}")
    public ResponseEntity<String> update(@RequestBody @Valid BookingAcceptDTO booking, BindingResult bindingResult, @PathVariable("id") long id, @RequestParam(name="admin") boolean admin){
        try{
            Optional<Booking> bookingInDb = bookingService.findById(id);
            if(bookingInDb.isEmpty()){
                return new ResponseEntity<>("Заказ не найден", HttpStatus.BAD_REQUEST);
            }else{
                Booking existBooking = bookingInDb.get();
                if(existBooking.getId() != booking.getId()){
                    return new ResponseEntity<>("Параметр id не совпадает с id заказа", HttpStatus.BAD_REQUEST);
                }

                if(existBooking.getStatus().equals("выполнен")){
                    return new ResponseEntity<>("Нельзя изменить данные у выполненного заказа", HttpStatus.CONFLICT);
                }

                if(bindingResult.hasErrors()){
                    return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
                }

                if(!admin) {
                    bookingService.update(existBooking, booking);
                }else{
                    bookingService.updateAdmin(existBooking, booking);
                }
            }
        } catch (Exception e){
            return new ResponseEntity<>("Ошибка изменения данных о заказе", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Данные о заказе успешно изменены!", HttpStatus.OK);
    }
}
