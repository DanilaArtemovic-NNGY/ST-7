package com.mycompany.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

final class AppDebugSupport {

    private AppDebugSupport() {
    }

    static void configureChromeDriver() {
        if (System.getProperty("webdriver.chrome.driver") != null) {
            return;
        }

        String envPath = System.getenv("CHROMEDRIVER");
        if (envPath != null && !envPath.isBlank()) {
            System.setProperty("webdriver.chrome.driver", envPath);
            return;
        }

        List<Path> candidates = List.of(
                Paths.get("chromedriver.exe"),
                Paths.get("..", "chromedriver.exe"),
                Paths.get(System.getProperty("user.home"), "Downloads", "chromedriver-win64", "chromedriver.exe"),
                Paths.get(System.getProperty("user.home"), "Downloads", "chromedriver.exe"));

        for (Path candidate : candidates) {
            Path absolute = candidate.toAbsolutePath().normalize();
            if (Files.exists(absolute)) {
                System.setProperty("webdriver.chrome.driver", absolute.toString());
                return;
            }
        }
    }
}
