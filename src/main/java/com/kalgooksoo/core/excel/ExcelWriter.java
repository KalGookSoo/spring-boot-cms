package com.kalgooksoo.core.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ExcelWriter {

    private final Workbook workbook;

    private final Map<String, Object> data;

    private final HttpServletResponse response;

    public ExcelWriter(Workbook workbook, Map<String, Object> data, HttpServletResponse response) {
        this.workbook = workbook;
        this.data = data;
        this.response = response;
    }

    public void create() {
        setFileName(response, getFilename());
        Sheet sheet = workbook.createSheet();
        createHeads(sheet, getHeads());
        createBodies(sheet, getBodies());
    }

    private String getFilename() {
        return (String) data.get("filename");
    }

    @SuppressWarnings("unchecked")
    private List<String> getHeads() {
        return (List<String>) data.get("head");
    }

    @SuppressWarnings("unchecked")
    private List<List<String>> getBodies() {
        return (List<List<String>>) data.get("body");
    }

    private void setFileName(HttpServletResponse response, String fileName) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + getFileExtension(fileName) + "\"");
    }

    private String getFileExtension(String fileName) {
        if (workbook instanceof XSSFWorkbook) {
            fileName += ".xlsx";
        } else if (workbook instanceof SXSSFWorkbook) {
            fileName += ".xlsx";
        } else if (workbook instanceof HSSFWorkbook) {
            fileName += ".xls";
        }
        return fileName;
    }

    private void createHeads(Sheet sheet, List<String> headList) {
        createRows(sheet, headList, 0);
    }

    private void createBodies(Sheet sheet, List<List<String>> bodies) {
        int rowSize = bodies.size();
        for (int i = 0; i < rowSize; i++) {
            createRows(sheet, bodies.get(i), i + 1);
        }
    }

    private void createRows(Sheet sheet, List<String> cells, int rowNum) {
        int size = cells.size();
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < size; i++) {
            row.createCell(i).setCellValue(cells.get(i));
        }
    }

    public static Map<String, Object> createExcelData(List<? extends Exportable> data, Class<?> target) {
        Map<String, Object> excelData = new HashMap<>();
        excelData.put("filename", createFileName(target));
        excelData.put("head", createHeaderName(target));
        excelData.put("body", createBodyData(data));
        return excelData;
    }

    private static List<String> createHeaderName(Class<?> header) {
        List<String> headData = new ArrayList<>();
        for (Field field : header.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ExcelColumnName.class)) {
                String headerName = field.getAnnotation(ExcelColumnName.class).headerName();
                headData.add("".equals(headerName) ? field.getName() : headerName);
            }
        }
        return headData;
    }

    private static String createFileName(Class<?> file) {
        if (file.isAnnotationPresent(ExcelFileName.class)) {
            String fileName = file.getAnnotation(ExcelFileName.class).fileName();
            return StringUtils.isBlank(fileName) ? file.getSimpleName() : URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        }
        throw new RuntimeException("excel filename not exist");
    }

    private static List<List<String>> createBodyData(List<? extends Exportable> dataList) {
        return dataList.stream().map(Exportable::mapToList).collect(Collectors.toList());
    }

}
