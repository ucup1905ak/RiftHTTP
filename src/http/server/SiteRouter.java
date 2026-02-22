package http.server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
/**
 *
 * @author farel
 */
public class SiteRouter {
    private final Path root;
    
    public SiteRouter(Path root) {
        this.root = root;
    }
    

    public boolean handle(HttpRequest request, HttpResponse response) throws IOException {

        String rawPath = request.getPath();

        if (rawPath.equals("/")) {
            rawPath = "/index.html";
        }

        Path filePath = root.resolve(rawPath.substring(1)).normalize();

        // Security check
        if (!filePath.startsWith(root)) {
            return true;
        }

       
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {

            byte[] body = Files.readAllBytes(filePath);

            response.setStatus(200, "OK");
            response.setHeaders("Content-Type", Files.probeContentType(filePath));
            response.setBody(body);
            return true;
        }

        Path indexPath = root.resolve("index.html");

        if (Files.exists(indexPath)) {
            byte[] body = Files.readAllBytes(indexPath);

            response.setStatus(200, "OK");
            response.setHeaders("Content-Type", "text/html; charset=utf-8");
            response.setBody(body);
            return true;
        }

        return false;
    }
}
