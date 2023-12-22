package kafpinpin120.publishingHouse.services;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import kafpinpin120.publishingHouse.dtos.*;
import kafpinpin120.publishingHouse.exceptions.DataNotFoundException;
import kafpinpin120.publishingHouse.models.*;
import kafpinpin120.publishingHouse.payloads.requests.GenerateReportRequest;
import kafpinpin120.publishingHouse.repositories.BookingRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final ProductService productService;

    private final BookingProductService bookingProductService;

    private final EmployeeService employeeService;

    private final EmailService emailService;

    private final ReportsService reportsService;

    private final UserService userService;

    public BookingService(BookingRepository bookingRepository, @Lazy ProductService productService, BookingProductService bookingProductService, EmployeeService employeeService, EmailService emailService, ReportsService reportsService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.productService = productService;
        this.bookingProductService = bookingProductService;
        this.employeeService = employeeService;
        this.emailService = emailService;
        this.reportsService = reportsService;
        this.userService = userService;
    }

//    private List<BookingSimpleSendDTO> getBookingSendDTOS(List<Booking> bookings){
//
//        List<BookingSimpleSendDTO> bookingSimpleSendDTOS = new ArrayList<>();
//        for(Booking booking: bookings){
//            bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(booking));
//        }
//
//        return bookingSimpleSendDTOS;
//    }


    public Optional<Booking> findById(long id){
        return bookingRepository.findById(id);
    }


    public BookingSimpleSendDTO getBookingSimpleSendDTO(Booking booking){
        return new BookingSimpleSendDTO(booking.getId(),booking.getCost());
    }

    public void add(BookingAcceptDTO bookingAcceptDTO) {

        try {
            Booking booking = new Booking();
            booking.setStatus(bookingAcceptDTO.getStatus());
            booking.setStartExecution(bookingAcceptDTO.getStartExecution());
            booking.setCost(bookingAcceptDTO.getCost());

            booking.setProducts(new ArrayList<>());
            for (ProductWithEditionDTO productWithEditionDTO : bookingAcceptDTO.getProductsWithMargin()) {
                Optional<Product> product = productService.findById(productWithEditionDTO.getId());
                product.ifPresent(value -> booking.getProducts().add(new BookingProduct(productWithEditionDTO.getEdition(), booking, value)));
            }

            bookingRepository.save(booking);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public List<BookingSimpleSendDTO> get(Integer page, Long bookingId, Long userId, String status) {
        List<BookingSimpleSendDTO> bookingSimpleSendDTOS = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();

        if(bookingId != null){
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            booking.ifPresent(value->bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(value)));
        }else {
            if (userId == null) {
                Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Order.desc("id")));
                if(status != null) {
                    bookings = bookingRepository.findByStatusContainsIgnoreCase(pageable, status).getContent();
                } else{
                    try {
                        bookings = bookingRepository.findAll(pageable).getContent();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else {
                List<BookingProduct> bookingProducts;
                if(status != null){
                    bookingProducts = bookingProductService.getUserBookingsProducts(page, userId, status);
                }else{
                    bookingProducts = bookingProductService.getUserBookingsProducts(page,userId);
                }

                for (BookingProduct bookingProduct : bookingProducts) {
                    bookings.add(bookingProduct.getBooking());
                }
            }

            for (Booking booking : bookings) {
                bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(booking));
            }
        }

        return bookingSimpleSendDTOS;
    }

    public BookingSendDTO getOne(long id) throws IOException {
        Optional<Booking> booking = bookingRepository.findById(id);
        if(booking.isEmpty()){
            throw new EntityNotFoundException("Заказ не найден");
        }else{
            Booking existBooking = booking.get();

            BookingSendDTO bookingSendDTO = new BookingSendDTO();
            bookingSendDTO.setId(existBooking.getId());
            bookingSendDTO.setStatus(existBooking.getStatus());
            bookingSendDTO.setStartExecution(existBooking.getStartExecution());
            bookingSendDTO.setEndExecution(existBooking.getEndExecution());
            bookingSendDTO.setCost(existBooking.getCost());
            bookingSendDTO.setPrintingHouse(existBooking.getPrintingHouse());

            //if(existBooking.getPrintingHouse() != null) {
                //bookingSendDTO.setPrintingHouse(existBooking.getPrintingHouse());
            //}
            if(existBooking.getEmployees() != null) {

                bookingSendDTO.setEmployees(employeeService.getListDTOS(existBooking.getEmployees()).stream()
                        .sorted(Comparator.comparing(EmployeeDTO::getSurname))
                        .toList());
            }

            bookingSendDTO.setProducts(productService.getListSimpleSendDTOS(existBooking.getProducts()));


            return bookingSendDTO;

        }
    }

    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    public void update(Booking booking, BookingAcceptDTO bookingAcceptDTO) {
        //booking.setStatus(bookingAcceptDTO.getStatus());
        booking.setCost(bookingAcceptDTO.getCost());
        //booking.setStartExecution(bookingAcceptDTO.getStartExecution());
        //booking.setEndExecution(bookingAcceptDTO.getEndExecution());
        //booking.setPrintingHouse(bookingAcceptDTO.getPrintingHouse());

        try {
            bookingProductService.deleteByBookingId(booking.getId());
            for (ProductWithEditionDTO productWithEditionDTO : bookingAcceptDTO.getProductsWithMargin()) {
                Optional<Product> product = productService.findById(productWithEditionDTO.getId());
                product.ifPresent(value -> booking.getProducts().add(new BookingProduct(productWithEditionDTO.getEdition(), booking, value)));
            }

//        if(bookingAcceptDTO.getIdsOfEmployees() != null){
//            booking.setEmployees(new ArrayList<>());
//            for(long employeeId: bookingAcceptDTO.getIdsOfEmployees()){
//                Optional<Employee> employee = employeeService.findById(employeeId);
//                employee.ifPresent(value-> booking.getEmployees().add(value));
//            }
//        }

            bookingRepository.save(booking);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateAdmin(Booking existBooking, BookingAcceptDTO bookingAcceptDTO, String updateStatus) {

        if(!updateStatus.equals("end")) {
            existBooking.setPrintingHouse(bookingAcceptDTO.getPrintingHouse());

            if (bookingAcceptDTO.getStatus().equals("ожидание")) {
                existBooking.setStatus("выполняется");

                User user = existBooking.getProducts().get(0).getProduct().getUser();
                String message = String.format("Уважаемый(-ая) %s, Ваш заказ №%d успешно зарегистрирован! Ориентировочная дата выполнения: %s. С уважением, \"Издательство\".", user.getName(), existBooking.getId(), bookingAcceptDTO.getEndExecution().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                emailService.sendSimpleEmail(user.getEmail(), "Регистрация заказа №" + existBooking.getId(), message);
            } else if (bookingAcceptDTO.getStatus().equals("выполняется")) {
                if (!bookingAcceptDTO.getEndExecution().equals(existBooking.getEndExecution())) {

                    User user = existBooking.getProducts().get(0).getProduct().getUser();
                    String message = String.format("Уважаемый(-ая) %s, у Вашего заказа №%d сменилась ориентировочная дата выполнения с %s на %s. С уважением, \"Издательство\".", user.getName(), existBooking.getId(), existBooking.getEndExecution().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), bookingAcceptDTO.getEndExecution().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    emailService.sendSimpleEmail(user.getEmail(), "Смена ориентировочной даты выполнения у заказа №" + existBooking.getId(), message);
                }
            }
            existBooking.setEndExecution(bookingAcceptDTO.getEndExecution());
            existBooking.getEmployees().clear();
            for (long employeeId : bookingAcceptDTO.getIdsOfEmployees()) {
                Optional<Employee> employee = employeeService.findById(employeeId);
                employee.ifPresent(value -> existBooking.getEmployees().add(value));
            }
        }else{
            if(!existBooking.getEndExecution().equals(LocalDate.now())){
                existBooking.setEndExecution(LocalDate.now());
            }

            existBooking.setStatus("выполнен");

            User user = existBooking.getProducts().get(0).getProduct().getUser();
            String message = String.format("Уважаемый(-ая) %s, Ваш заказ №%d успешно выполнен! С уважением, \"Издательство\".", user.getName(), existBooking.getId());
            emailService.sendSimpleEmail(user.getEmail(), "Выполнение заказа №" + existBooking.getId(), message);
        }

        bookingRepository.save(existBooking);

    }

    public void generateReport(GenerateReportRequest generateReportRequest) throws IOException, MessagingException {
        String status = "выполнен";
        List<Booking> bookings = new ArrayList<>();

        if(generateReportRequest.getUserRole().equals(UserRole.ADMINISTRATOR.name())){
            bookings = bookingRepository.findByStatusContainsIgnoreCaseAndEndExecutionBetween(status, generateReportRequest.getStartPeriod(), generateReportRequest.getEndPeriod());
        }else{
            List<BookingProduct> bookingProducts = bookingProductService.getUserBookingsProductsBetweenData(generateReportRequest.getUserId(), status, generateReportRequest.getStartPeriod(), generateReportRequest.getEndPeriod());
            for(BookingProduct bookingProduct: bookingProducts){
                bookings.add(bookingProduct.getBooking());
            }
        }

        if(bookings.isEmpty()){
            throw new DataNotFoundException("Не найдено ни одного заказа со статусом: " + status);
        }else{
            File report = reportsService.getExcelWithBookings(bookings.stream().sorted(Comparator.comparing(Booking::getId).reversed()).toList());

            String message = String.format(
                    "Отчёт о выполненных заказах с %s по %s. С уважением, \"Издательство\".",
                    generateReportRequest.getStartPeriod().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    generateReportRequest.getEndPeriod().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            );

            Optional<User> user = userService.findById(generateReportRequest.getUserId());
            if(user.isPresent()){
                emailService.sendEmailWithAttachment(user.get().getEmail(), "Отчёт о выполненных заказах", message, report);
            }

        }
    }
}

