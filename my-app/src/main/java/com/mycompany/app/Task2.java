package com.mycompany.app;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class Task2 {

    private Task2() {
    }

    public static void printClientIp(WebDriver webDriver) throws Exception {
        webDriver.get("https://api.ipify.org/?format=json");

        WebElement element = webDriver.findElement(By.tagName("pre"));
        String json = element.getText();

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(json);
        String ip = object.get("ip").toString();

        System.out.println("Task 2. Client IPv4:");
        System.out.println(ip);
    }
}
