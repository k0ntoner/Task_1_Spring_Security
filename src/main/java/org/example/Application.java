package org.example;

import org.example.configs.Config;
import org.example.utils.ConsoleUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI(new AnnotationConfigApplicationContext(Config.class));
        consoleUI.start();
    }
}
