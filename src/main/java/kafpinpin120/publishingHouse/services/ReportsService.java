package kafpinpin120.publishingHouse.services;


import kafpinpin120.publishingHouse.models.Booking;
import kafpinpin120.publishingHouse.models.BookingProduct;
import kafpinpin120.publishingHouse.models.Employee;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;


@Service
public class ReportsService {

    public File getExcelWithBookings(List<Booking> bookings) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Заказы");
        Row row = sheet.createRow(0);

        row.createCell(0).setCellValue("Номер");
        row.createCell(1).setCellValue("Статус");
        row.createCell(2).setCellValue("Стоимость");
        row.createCell(3).setCellValue("Дата приёма");
        row.createCell(4).setCellValue("Дата выполнения");
        row.createCell(5).setCellValue("Типография");
        row.createCell(6).setCellValue("Продукции");
        row.createCell(7).setCellValue("Сотрудники");

        int rowIndex = 1;

        for(Booking booking: bookings){
            Row dataRow = sheet.createRow(rowIndex);

            dataRow.createCell(0).setCellValue(booking.getId());
            dataRow.createCell(1).setCellValue(booking.getStatus());
            dataRow.createCell(2).setCellValue(booking.getCost().toString() + "₽");
            dataRow.createCell(3).setCellValue(booking.getStartExecution().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            dataRow.createCell(4).setCellValue(booking.getEndExecution().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            dataRow.createCell(5).setCellValue(booking.getPrintingHouse().getName());

            String products = "";
            for(BookingProduct bookingProduct: booking.getProducts().stream().sorted(Comparator.comparing((BookingProduct bookingProduct) -> bookingProduct.getProduct().getName())).toList()){
                products += bookingProduct.getProduct().getName() + "; ";
            }
            dataRow.createCell(6).setCellValue(products);

            String employees = "";
            for(Employee employee: booking.getEmployees().stream().sorted(Comparator.comparing(Employee::getSurname)).toList()){
                if(employee.getPatronymic().isEmpty()){
                    employees += String.format("%s %s; ", employee.getSurname(), employee.getName());
                }else{
                    employees += String.format("%s %s %s; ", employee.getSurname(), employee.getName(), employee.getPatronymic());
                }
            }

            dataRow.createCell(7).setCellValue(employees);
            rowIndex++;
        }

        ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] byteArray = outputStream.toByteArray();
        File outputFile = new File("Заказы" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx");

        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();


        return outputFile;

    }
}
