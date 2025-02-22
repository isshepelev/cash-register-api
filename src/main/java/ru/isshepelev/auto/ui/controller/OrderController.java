package ru.isshepelev.auto.ui.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.kafka.Producer;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.OrderService;
import ru.isshepelev.auto.infrastructure.service.ReportService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final Producer producer;
    private final OrderService orderService;
    private final ReportService reportService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestBody List<Menu> orderItems, HttpSession session) {
        String employeeName = (String) session.getAttribute("employeeName");

        if (employeeName == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы не авторизованы.");
        }
        if (orderItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Список заказов пуст.");
        }
        producer.sendOrder(orderService.createNewOrder(orderItems));
        return ResponseEntity.ok("Заказ успешно создан.");
    }

    @GetMapping("")
    public String formOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "order";
    }

    @GetMapping("/generateReport")
    @ResponseBody
    public void generateReportAndDownload(@RequestParam("startDate") String startDate,
                                          @RequestParam("endDate") String endDate,
                                          HttpServletResponse response) throws IOException {
        byte[] report = reportService.generateOrderReport(startDate, endDate);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders_report.csv");

        response.getOutputStream().write(report);
    }
}
