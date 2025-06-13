package com.alanburic.socketchat.server;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

public class BroadcastServer {

    private static final Logger logger = LoggerFactory.getLogger("BroadcastServer");

    private final CopyOnWriteArrayList<PrintWriter> clientWriters = new CopyOnWriteArrayList<>();

    public void run(int port) {
        try (var serverSocket = new ServerSocket(port)) {
            logger.info("Broadcast server is listening on port {}", port);

            while (!serverSocket.isClosed()) {
                new Thread(new ClientHandler(serverSocket.accept())).start();
            }
        } catch (IOException exception) {
            logger.error("Broadcast server caught an exception", exception);
        }
    }

    void broadcastMessage(@NotNull String message) {
        clientWriters.forEach(writer -> writer.println(message));
    }

    private class ClientHandler implements Runnable {

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");

        private final Socket socket;

        private PrintWriter out = null;

        public ClientHandler(@NotNull Socket socket) {
            this.socket = socket;
        }

        public void run() {
            logger.info("Client connected from IP address {}.", socket.getInetAddress().getHostAddress());

            try (var in = new BufferedReader(new InputStreamReader(socket.getInputStream())); var out = this.out =
                    new PrintWriter(socket.getOutputStream(), true)) {
                clientWriters.add(out);

                broadcastMessage("A client has connected.");

                String message;
                while ((message = in.readLine()) != null) {
                    broadcastMessage("[%s] %s".formatted(ZonedDateTime.now(ZoneOffset.UTC).format(DATE_TIME_FORMATTER),
                            message));
                }
            } catch (IOException ignored) {
            } finally {
                logger.info("Client from {} has disconnected.", socket.getInetAddress().getHostAddress());

                try {
                    socket.close();
                } catch (IOException ignored) {
                }

                clientWriters.remove(this.out);
            }
        }
    }
}