package com.qsi.simulator;

import java.util.Scanner;

public class SpikeController {

    private final LogGenerator generator;

    public SpikeController(LogGenerator generator) {
        this.generator = generator;
    }

    public void start() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Type 'spike' or 'normal': ");
                String cmd = scanner.nextLine();

                if ("spike".equalsIgnoreCase(cmd)) {
                    generator.enableSpike();
                } else if ("normal".equalsIgnoreCase(cmd)) {
                    generator.disableSpike();
                }
            }
        }).start();
    }
}