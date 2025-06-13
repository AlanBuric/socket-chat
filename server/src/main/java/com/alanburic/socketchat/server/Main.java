package com.alanburic.socketchat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            logger.error("Missing port number.");
            System.exit(1);
            return;
        }

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
            logger.error("Invalid port number: {}", args[0]);
            System.exit(1);
            return;
        }

        new BroadcastServer().run(port);

        logger.info("Broadcast server has shut down.");
    }
}
