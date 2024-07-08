package com.example.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class UC1CheckLoginTest {

    @Test
    void testCheckLogin() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver.exe");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String testDate = formatter.format(date);
        String testerName = "Worapakorn Jarusiriphot";

        String path = "C:/Users/Administrator/Desktop/testdata.xlsx";
        FileInputStream fs = new FileInputStream(path);

        // Creating a workbook
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int row = sheet.getLastRowNum() + 1;

        for (int i = 1; i < row; i++) {
            WebDriver driver = new ChromeDriver();
            driver.get("https://katalon-demo-cura.herokuapp.com/");
            driver.findElement(By.id("btn-make-appointment")).click();
            String testcaseid = sheet.getRow(i).getCell(0).toString();
            String username;
            String password;
            if (testcaseid.equals("tc104")) {
                username = "";
                password = "";
            } else {
                username = sheet.getRow(i).getCell(1).toString();
                password = sheet.getRow(i).getCell(2).toString();
            }
            driver.findElement(By.id("txt-username")).sendKeys(username);
            driver.findElement(By.id("txt-password")).sendKeys(password);
            driver.findElement(By.id("btn-login")).click();

            String actual;
            String expected = sheet.getRow(i).getCell(3).toString();
            Row rows = sheet.getRow(i);
            Cell cell = rows.createCell(4);
            if (testcaseid.equals("tc101")) {
                actual = driver.findElement(By.xpath("/html/body/section/div/div/div/h2")).getText();
            } else {
                actual = driver.findElement(By.xpath("/html/body/section/div/div/div[1]/p[2]")).getText();
            }
            cell.setCellValue(actual);
            assertEquals(expected, actual);
            Cell cell2 = rows.createCell(5);
            cell2.setCellValue(actual.equals(expected) ? "Pass" : "Fail");
            Cell cell3 = rows.createCell(6);
            cell3.setCellValue(testDate);
            Cell cell4 = rows.createCell(7);
            cell4.setCellValue(testerName);
            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
            driver.close();
        }
        workbook.close();
        fs.close();
    }
}
