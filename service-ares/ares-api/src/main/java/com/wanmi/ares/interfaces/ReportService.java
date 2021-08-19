package com.wanmi.ares.interfaces;

public class ReportService {

  public interface Iface {

    public void generateReport(String date);

    public void tradeGenerateReport(String date);

    public void generateTodayReport();

  }
}