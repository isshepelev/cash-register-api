package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;
import ru.isshepelev.auto.infrastructure.service.OrderService;
import ru.isshepelev.auto.infrastructure.service.ReportService;
import ru.isshepelev.auto.infrastructure.service.dto.OrderMenuDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {


    private final OrderService orderService;

    @Override
    public byte[] generateOrderReport(String startDate, String endDate) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        List<Order> orders = orderService.getAllOrders().stream()
                .filter(order -> !order.getDate().toLocalDate().isBefore(start) && !order.getDate().toLocalDate().isAfter(end))
                .collect(Collectors.toList());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(';').withHeader("ID заказа", "Дата", "Статус", "Товар", "Описание"))) {

            outputStream.write(0xEF);
            outputStream.write(0xBB);
            outputStream.write(0xBF);

            for (Order order : orders) {
                for (OrderMenuDto menuItem : order.getMenuList()) {
                    csvPrinter.printRecord(order.getId(), order.getDate().toLocalDate().toString(), order.getStatus(), menuItem.getName(), menuItem.getDescription());
                }
            }

            Map<String, Long> productCount = orders.stream()
                    .flatMap(order -> order.getMenuList().stream())
                    .collect(Collectors.groupingBy(OrderMenuDto::getName, Collectors.counting()));

            csvPrinter.println();
            csvPrinter.printRecord("Товар", "Количество заказов");

            for (Map.Entry<String, Long> entry : productCount.entrySet()) {
                csvPrinter.printRecord(entry.getKey(), entry.getValue());
            }

            List<Order> errorOrders = orders.stream()
                    .filter(order -> order.getStatus() == OrderStatus.ERROR)
                    .collect(Collectors.toList());

            csvPrinter.println();
            csvPrinter.printRecord("ID заказа", "Дата", "Ошибка");

            for (Order order : errorOrders) {
                csvPrinter.printRecord(order.getId(), order.getDate().toLocalDate().toString(), order.getStatus());
            }

            Map<OrderStatus, Long> statusCount = orders.stream()
                    .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

            csvPrinter.println();
            csvPrinter.printRecord("Статус заказа", "Количество заказов");

            for (Map.Entry<OrderStatus, Long> entry : statusCount.entrySet()) {
                csvPrinter.printRecord(entry.getKey(), entry.getValue());
            }

            csvPrinter.flush();
            return outputStream.toByteArray();
        }
    }
}