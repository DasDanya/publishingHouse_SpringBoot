package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.models.PrintingHouse;
import kafpinpin120.publishingHouse.repositories.PrintingHouseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrintingHouseService {

    private final PrintingHouseRepository printingHouseRepository;

    public PrintingHouseService(PrintingHouseRepository printingHouseRepository) {
        this.printingHouseRepository = printingHouseRepository;
    }

    public void save(PrintingHouse printingHouse) {
        printingHouseRepository.save(printingHouse);
    }

    public List<PrintingHouse> findByPage(int page) {
        int size = 7;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

        return printingHouseRepository.findAll(pageable).getContent();
    }

    public List<PrintingHouse> findByPage(int page, String name){
        int size = 7;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

        return printingHouseRepository.findByNameContainsIgnoreCase(pageable,name).getContent();
    }

    public Optional<PrintingHouse> findById(long id){
        return printingHouseRepository.findById(id);
    }

    public void delete(long id){
        printingHouseRepository.deleteById(id);
    }
}
