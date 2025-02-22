package ru.isshepelev.auto.ui.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.isshepelev.auto.infrastructure.service.EmployeeService;
import ru.isshepelev.auto.infrastructure.service.JobTitleService;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeCreateDto;
import ru.isshepelev.auto.infrastructure.service.dto.EmployeeEditDto;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JobTitleService jobTitleService;


    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployee());
        model.addAttribute("jobTitles", jobTitleService.getJobTitle());
        return "employees";
    }


    @PostMapping("/{id}")
    public String deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/employees";
    }


    @PostMapping("/create")
    public String createEmployee(@ModelAttribute EmployeeCreateDto employeeDto) {
        employeeService.createEmployee(employeeDto);
        return "redirect:/employees";
    }


    @PostMapping("/roles/create")
    public String createRole(@RequestParam String role, RedirectAttributes redirectAttributes) {
        jobTitleService.addJobTitle(role);
        redirectAttributes.addFlashAttribute("jobTitles", jobTitleService.getJobTitle());
        return "redirect:/employees";
    }


    @PostMapping("/roles/delete/{id}")
    public String deleteRole(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            jobTitleService.deleteJobTitleById(id);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employees";
    }


    @GetMapping("/edit/{id}")
    public String editEmployeeForm(@PathVariable UUID id, Model model) {
        model.addAttribute("employees", employeeService.getAllEmployee());
        model.addAttribute("employee", employeeService.getEmployeeById(id).get());
        model.addAttribute("jobTitles", jobTitleService.getJobTitle());
        return "edit-employee";
    }


    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable UUID id, @ModelAttribute EmployeeEditDto employeeEditDto) {
        employeeService.update(id, employeeEditDto);
        return "redirect:/employees";
    }


    @PostMapping("/check-access/{employeeCode}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAccess(@PathVariable int employeeCode, HttpSession session) {
        return ResponseEntity.ok(employeeService.checkAccess(employeeCode, session));
    }
}
