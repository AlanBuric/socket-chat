# socket-chat

[![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](https://www.java.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

A little exercise socket broadcast chat Swing application. It consists of two separate JARs:

1. A **socket server** that listens for incoming connections and broadcasts messages to all connected **socket clients**.
2. A Java Swing **socket client** that connects to the **socket server** and allows the user to send messages to it.

Note: this project is not my proudest work, because the code feels very messy and ugly.

## Features

- **Socket Server**: Listens for incoming connections and broadcasts messages to all connected clients in real-time.
- **Socket Client**: Connects to the server and allows the user to send messages.
- **Swing GUI**: Provides a user-friendly interface for the client to interact with the server.
- **Multithreading**: For handling multiple clients simultaneously using threads.