package com.mycompany.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class Task3 {

    private static final String FORECAST_URL =
            "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44"
                    + "&hourly=temperature_2m,rain"
                    + "&current=cloud_cover"
                    + "&timezone=Europe%2FMoscow"
                    + "&forecast_days=1"
                    + "&wind_speed_unit=ms";

    private Task3() {
    }

    public static void printAndSaveForecast(WebDriver webDriver, Path outputPath) throws Exception {
        webDriver.get(FORECAST_URL);

        WebElement element = webDriver.findElement(By.tagName("pre"));
        String json = element.getText();

        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(json);
        JSONObject hourly = (JSONObject) root.get("hourly");

        JSONArray times = (JSONArray) hourly.get("time");
        JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
        JSONArray rain = (JSONArray) hourly.get("rain");

        String table = buildTable(times, temperatures, rain);

        System.out.println("Task 3. Forecast for Nizhny Novgorod:");
        System.out.println(table);

        writeForecast(outputPath, table);
        System.out.println("Forecast saved to: " + outputPath);
    }

    private static String buildTable(JSONArray times, JSONArray temperatures, JSONArray rain) {
        StringBuilder builder = new StringBuilder();
        String header = String.format("%-4s| %-16s| %-13s| %-12s%n",
                "No", "Date/Time", "Temperature", "Rain (mm)");
        builder.append(header);
        builder.append("----+-----------------+--------------+------------").append(System.lineSeparator());

        for (int i = 0; i < times.size(); i++) {
            String line = String.format(Locale.US, "%-4d| %-16s| %-13s| %-12s%n",
                    i + 1,
                    times.get(i).toString(),
                    temperatures.get(i).toString(),
                    rain.get(i).toString());
            builder.append(line);
        }

        return builder.toString();
    }

    private static void writeForecast(Path outputPath, String table) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, table, StandardCharsets.UTF_8);
    }
}
