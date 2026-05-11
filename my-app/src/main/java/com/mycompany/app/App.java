package com.mycompany.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App {

    public static void main(String[] args) {
        AppDebugSupport.configureChromeDriver();

        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            String password = fetchGeneratedPassword(webDriver);
            System.out.println("Task 1. Generated password:");
            System.out.println(password);
            System.out.println();

            Task2.printClientIp(webDriver);
            System.out.println();

            Task3.printAndSaveForecast(webDriver, resolveForecastPath());
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace(System.out);
        } finally {
            webDriver.quit();
        }
    }

    private static String fetchGeneratedPassword(WebDriver webDriver) {
        webDriver.get("https://www.calculator.net/password-generator.html");

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#resultid .verybigtext b"))).getText();
    }

    private static Path resolveForecastPath() throws Exception {
        List<Path> candidates = List.of(
                Paths.get("..", "result", "forecast.txt"),
                Paths.get("result", "forecast.txt"));

        for (Path candidate : candidates) {
            Path absolute = candidate.toAbsolutePath().normalize();
            Path parent = absolute.getParent();
            if (parent != null && Files.exists(parent)) {
                return absolute;
            }
        }

        throw new IllegalStateException("Directory for forecast.txt was not found");
    }

}
