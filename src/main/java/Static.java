import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

public class Static implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        System.out.println("looking for: " + uri.getPath());
        String path = "." + uri.getPath();

        ClassLoader classLoader = getClass().getClassLoader();
        URL fileURL = classLoader.getResource(path);

        OutputStream os = httpExchange.getResponseBody();

        if (fileURL == null) {
            System.out.println("File doesnt exist: " + uri.getPath());
        } else {
            sendFile(httpExchange, fileURL);
        }

    }

    private void sendFile(HttpExchange httpExchange, URL fileURL) throws IOException {

        File file = new File(fileURL.getFile());
        String mimeType = Files.probeContentType(file.toPath());

        httpExchange.getResponseHeaders().set("Content-Type", mimeType);
        httpExchange.sendResponseHeaders(200, 0);

        OutputStream os = httpExchange.getResponseBody();

        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0){
            os.write(buffer,0,count);
        }
        os.close();

    }

}
