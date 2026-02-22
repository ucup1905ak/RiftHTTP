# Rift HTTP

**Author:** Farelino Alexander Kim  
**Institution:** UNIVERSITAS ATMA JAYA YOGYAKARTA  

## Overview
A lightweight, multithreaded HTTP/1.1 server written in pure Java. This project demonstrates core networking concepts, raw HTTP protocol parsing, and concurrent connection handling using Java Sockets and the `java.util.concurrent` package. 

The server is configured to serve static files (HTML, CSS, JS, images) from a local `www` directory and handles client requests asynchronously.

## Features
* **Multithreading:** Utilizes a `ThreadPoolExecutor` to handle multiple simultaneous client connections (configured to a max of 3 concurrent clients).
* **Custom Request/Response Parsing:** Parses raw HTTP input streams into structured `HttpRequest` objects and builds raw byte responses via `HttpResponse`.
* **Static File Routing:** Maps URI paths to local file paths using `SiteRouter`.
* **Path Traversal Security:** Prevents malicious users from accessing files outside the designated root directory.
* **MIME Type Detection:** Automatically probes and sets the correct `Content-Type` for served files.
* **SPA Fallback:** Routes unresolved paths back to `index.html`, making it friendly for Single Page Applications (React, Vue, etc.).

## Project Architecture
* `HTTPServer.java`: The main entry point. Binds a `ServerSocket` to port 80 and accepts incoming connections, handing them off to the thread pool.
* `ClientHandler.java`: A `Runnable` task that manages the lifecycle of a single client socket connection.
* `HttpRequest.java`: Reads the socket's `InputStream`, parses the request line (Method, Path, Version), extracts headers, and processes the request body.
* `HttpResponse.java`: Constructs the HTTP response, automatically calculating the `Content-Length` and formatting the status line, headers, and body for the `OutputStream`.
* `SiteRouter.java`: Handles the server's routing logic. Validates the requested path, checks for file existence, and attaches the file bytes to the `HttpResponse`.

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher.

### Setup & Execution
1. **Compile the code:**
   ```bash
   javac http/server/*.java
