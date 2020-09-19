package crud.employees.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import crud.employees.entities.Employee;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(value = "/getNotDeletedEmployees")
    public ResponseEntity<String> getNotDeletedEmployees(@RequestParam Integer page, @RequestParam Integer limit) {
        try {
            // We need page - 1 because in front we usually starts from first page, but repository starts from zero page
            List<Employee> result = employeeRepository.
                    findByDeletedFalse(PageRequest.of(page - 1, limit, Sort.by("id")));
            return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getEmployeeById")
    public ResponseEntity<String> getEmployeeById(@RequestParam Integer id) {
        return requestWithIdParameter(id, employee -> {
            try {
                return new ResponseEntity<>(mapper.writeValueAsString(employee), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        });
    }

    @PostMapping(value = "/updateEmployeeById")
    public ResponseEntity<String> updateEmployeeById(@RequestParam Integer id, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
                                             @RequestParam(required = false) String middleName, @RequestParam(required = false) Instant dateOfBirth,
                                             @RequestParam(required = false) Instant dateAdded, @RequestParam(required = false) Integer accountNumber,
                                             @RequestParam(required = false) Boolean deleted) {
        return requestWithIdParameter(id, employee -> {
            boolean somethingChanged = employee.update(firstName, lastName, middleName, dateOfBirth, dateAdded, accountNumber, deleted);
            if (somethingChanged)
                employeeRepository.save(employee);

            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

    @PostMapping(value = "/deleteEmployeeById")
    public ResponseEntity<String> deleteEmployeeById(@RequestParam Integer id) {
        return requestWithIdParameter(id, employee -> {
            if (!employee.isDeleted()) {
                employee.update(null, null, null, null, null, null, true);
                employeeRepository.save(employee);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

    @PostMapping(value = "/createEmployee")
    public ResponseEntity<String> createEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String middleName,
                                         @RequestParam String dateOfBirth, @RequestParam Integer accountNumber, @RequestParam(required = false) Boolean deleted) {
        try {
            Instant now = Instant.now();
            Instant instantDateOfBirth = new Instant(dateOfBirth);
            Employee employee = employeeRepository.save(
                    new Employee(firstName, lastName, middleName, instantDateOfBirth, now, now, accountNumber, deleted != null ? deleted : false)
            );
            return new ResponseEntity<>(mapper.writeValueAsString(employee), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> requestWithIdParameter(Integer id, Function<Employee, ResponseEntity<String>> function) {
        try {
            Employee employee = employeeRepository.findById(id).orElse(null);
            if (employee == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return function.apply(employee);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
