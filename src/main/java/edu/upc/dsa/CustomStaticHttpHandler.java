package edu.upc.dsa;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import java.io.*;

public class CustomStaticHttpHandler extends HttpHandler {
    private final String basePath;

    public CustomStaticHttpHandler(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        String uri = request.getRequestURI();
        String path = uri.replaceFirst("^/TocaBolas/?", ""); // elimina el prefijo

        File file = new File(basePath, path);

        // 👉 Si no tiene extensión, intenta con .html
        if (!file.exists() && !path.contains(".") && !path.endsWith("/")) {
            file = new File(basePath, path + ".html");
        }

        // Si es un directorio, busca index.html
        if (file.isDirectory()) {
            file = new File(file, "index.html");
        }

        // Archivo encontrado
        if (file.exists() && file.isFile()) {
            sendFile(response, file, 200);
        }
        // 404
        else {
            File notFoundFile = new File(basePath, "404.html");
            if (notFoundFile.exists()) {
                sendFile(response, notFoundFile, 404);
            } else {
                response.setStatus(404);
                response.getWriter().write("404 - Página no encontrada");
            }
        }
    }


    private void sendFile(Response response, File file, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(guessContentType(file));
        response.setContentLengthLong(file.length());

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private String guessContentType(File file) {
        try {
            return java.nio.file.Files.probeContentType(file.toPath());
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
}

