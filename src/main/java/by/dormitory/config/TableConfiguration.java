package by.dormitory.config;

import by.dormitory.entity.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class TableConfiguration {
    private static final String title = "График дежурств по 6 этажу за %s %s";
    private Sheet sheet;
    private Workbook workbook;
    private final String[] monthOfYear;
    private final String[] dayOfWeekString;
    private final byte startColumn = 5;
    private File file;

    {
        dayOfWeekString = new String[]{"Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"};
        monthOfYear = new String[]{
                "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
                "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    }

    public void insertToFile(User user, int day, int half) {
        String filename = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 2);
        this.file = new File("files" + File.separator + filename + ".xls");
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            workbook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            System.out.println("Ошибка создания книги");
        }
        sheet = workbook.getSheet(filename);

        int startCell = 2;
        if (half == 2) {
            startCell = 5;
        }

        Row row = sheet.getRow(day + this.startColumn - 1);

        insertToCell(row, startCell, user);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertToCell(Row row, int startCell, User user){
        Cell cell = row.getCell(startCell);
        if(user != null){
            cell.setCellValue(user.getLastName() + " " + user.getFirstName().toUpperCase().charAt(0) + ".");
            startCell++;
            cell = row.getCell(startCell);
            if(user.getGrp() != null){
                cell.setCellValue(user.getGrp());
            }
            startCell++;
            cell = row.getCell(startCell);
            if(user.getRoom() != null){
                cell.setCellValue(user.getRoom());
            }
        } else {
            cell.setCellValue("");
            startCell++;
            cell = row.getCell(startCell);
            cell.setCellValue("");
            startCell++;
            cell = row.getCell(startCell);
            cell.setCellValue("");
        }
    }

    private void getCustomStyle(Workbook workbook, Cell cell) {
        short borderWidth = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeight((short) 240);
        titleFont.setFontName("Times New Roman");
        cellStyle.setBorderRight(borderWidth);
        cellStyle.setBorderLeft(borderWidth);
        cellStyle.setBorderTop(borderWidth);
        cellStyle.setBorderBottom(borderWidth);
        cell.setCellStyle(cellStyle);
    }

    public void createFile() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        String filename = String.valueOf(cal.get(Calendar.MONTH) + 1);
        sheet = workbook.getSheet("10");
        getTitleCell(cal);
        byte days = (byte) cal.getActualMaximum(cal.DAY_OF_MONTH);
        byte startColumnLocal = startColumn;
        Cell cellOfStyle = sheet.getRow(4).getCell(2);
        for (int i = 1; i < days + 1; i++) {
            Row rowOfPage = sheet.createRow(startColumnLocal);
            for (int j = 0; j < 8; j++) {
                Cell cell = rowOfPage.createCell(j);
                cell.setCellStyle(cellOfStyle.getCellStyle());
                if (j == 1) {
                    cell.setCellValue(i);
                } else if (j == 0) {
                    int day = changeCalendar(i, cal) - 1;
                    cell.setCellValue(this.dayOfWeekString[day]);
                } else {
                    cell.setCellValue("");
                }
            }
            startColumnLocal++;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("files" + File.separator + filename + ".xls"));
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getTitleCell(Calendar calendar) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        String titlePrint = String.format(
                title,
                monthOfYear[calendar.get(Calendar.MONTH)],
                calendar.get(Calendar.YEAR) + " г.");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Times New Roman");
        titleFont.setFontHeight((short) 280);
        cellStyle.setFont(titleFont);
        cellStyle.setBorderRight((short) 0);
        titleCell.setCellStyle(cellStyle);
        titleCell.setCellValue(titlePrint);
    }

    private int changeCalendar(int i, Calendar calendar) {
        byte currentMonth = (byte) calendar.get(Calendar.MONTH);
        short currentYear = (short) calendar.get(Calendar.YEAR);
        Calendar cal = new GregorianCalendar(currentYear, currentMonth, i);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
