package ru.isshepelev.auto.infrastructure.service;

import java.io.IOException;

public interface ReportService {

    byte[] generateOrderReport(String startDate, String endDate) throws IOException;
}
