package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

    Optional<Employee> findById(long id);
    Page<Employee> findBySurnameContainsIgnoreCase(Pageable pageable, String surname);
    void save(Employee employee);

    void deleteById(long id);

}
