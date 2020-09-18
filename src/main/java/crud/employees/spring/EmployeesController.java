package crud.employees.spring;

import crud.employees.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@RestController
public class EmployeesController {

    private final EmployeesRepository employeesRepository;

    @Autowired
    public EmployeesController(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    @GetMapping("/getNotDeletedEmployees")
    public ResponseEntity getNotDeletedEmployees(@RequestParam Integer page, @RequestParam Integer limit) {
        try {
            List<Employee> result = employeesRepository.
                    findByDeletedFalse(PageRequest.of(page, limit, Sort.by("id")));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getEmployeeById")
    public ResponseEntity getEmployeeById(@RequestParam Integer id) {
        return requestWithIdParameter(id, employee -> new ResponseEntity<>(employee, HttpStatus.OK));
    }

    @PostMapping("/updateEmployeeById")
    public ResponseEntity updateEmployeeById(@RequestParam Integer id, @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
                                             @RequestParam(required = false) String middleName, @RequestParam(required = false) Instant dateOfBirth,
                                             @RequestParam(required = false) Instant dateAdded, @RequestParam(required = false) Integer accountNumber,
                                             @RequestParam(required = false) Boolean deleted) {
        return requestWithIdParameter(id, employee -> {
            try {
                boolean somethingChanged = employee.update(firstName, lastName, middleName, dateOfBirth, dateAdded, accountNumber, deleted);
                if (somethingChanged)
                    employeesRepository.save(employee);

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        });
    }

    @PostMapping("/deleteEmployeeById")
    public ResponseEntity deleteEmployeeById(@RequestParam Integer id) {
        return requestWithIdParameter(id, employee -> {
            try {
                if (!employee.isDeleted()) {
                    employee.update(null, null, null, null, null, null, true);
                    employeesRepository.save(employee);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        });
    }

    @PostMapping()
    public ResponseEntity createEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String middleName,
                                         @RequestParam Instant dateOfBirth, @RequestParam Integer accountNumber, @RequestParam Boolean deleted) {
        try {
            Instant now = Instant.now();
            Employee employee = employeesRepository.save(
                    new Employee(firstName, lastName, middleName, dateOfBirth, now, now, accountNumber, deleted)
            );
            return new ResponseEntity<>(employee.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity requestWithIdParameter(Integer id, Function<Employee, ResponseEntity> function) {
        Employee employee = employeesRepository.findById(id).orElse(null);
        if (employee == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return function.apply(employee);
    }
}
