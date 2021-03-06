package crud.employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import crud.employees.entities.Employee;
import crud.employees.spring.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    @Test
    @Transactional
    public void testGetEmployees() throws Exception {
        long employeeCount = employeeRepository.count();
        Employee employee = employeeRepository.findById(1).orElse(null);
        assertNotNull(employee);

        String result = mockMvc.perform(get("/getEmployeeById")
                .contentType("application/json")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Employee anotherEmployee = mapper.readValue(result, Employee.class);
        assertEquals(employee, anotherEmployee);

        mockMvc.perform(get("/getEmployeeById")
                .contentType("application/json")
                .param("id", "777"))
                .andExpect(status().isNotFound());

        String resultArray = mockMvc.perform(get("/getNotDeletedEmployees")
                .contentType("application/json")
                .param("page", "1")
                .param("limit", "20"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Employee[] employees = mapper.readValue(resultArray, Employee[].class);
        assertEquals(employeeCount, employees.length);
        assertEquals(employees[0], employee);
    }

    @Test
    @Transactional
    public void testCreateEmployee() throws Exception {
        long employeeCount = employeeRepository.count();
        mockMvc.perform(post("/createEmployee")
                .contentType("application/json")
                .param("firstName", "firstName")
                .param("lastName", "lastName")
                .param("middleName", "middleName")
                .param("dateOfBirth", "1990-09-09T00:00:00.000Z")
                .param("accountNumber", "123"))
                .andExpect(status().isCreated());
        long employeeCountAfterCreate = employeeRepository.count();
        assertNotEquals(employeeCount, employeeCountAfterCreate);
        assertEquals(employeeCount + 1, employeeCountAfterCreate);
    }

    @Test
    @Transactional
    public void testUpdateEmployee() throws Exception {
        String newName = "newFirstName";
        Employee employee = employeeRepository.findById(1).orElse(null);
        assertNotNull(employee);
        assertNotEquals(employee.getFirstName(), newName);

        mockMvc.perform(post("/updateEmployeeById")
                .contentType("application/json")
                .param("id", "1")
                .param("firstName", newName))
                .andExpect(status().isOk());

        assertEquals(employee.getFirstName(), newName);
    }

    @Test
    @Transactional
    public void testDeleteEmployee() throws Exception {
        Employee employee = employeeRepository.findById(1).orElse(null);
        assertNotNull(employee);
        assertFalse(employee.isDeleted());
        mockMvc.perform(post("/deleteEmployeeById")
                .contentType("application/json")
                .param("id", "1"))
                .andExpect(status().isOk());

        assertTrue(employee.isDeleted());
    }

}
