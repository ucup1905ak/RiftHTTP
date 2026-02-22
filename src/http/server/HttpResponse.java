/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author farel
 */
public class HttpResponse {
    private int statusCode = 200;
    private String statusText = "OK";
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;
    private String version = "HTTP/1.1";
    private OutputStream out;
    public HttpResponse(OutputStream out) {
        this.out = out;
    }
    
    public void setVersion(String version){this.version = version;}
    public void setStatus(int code, String text){this.statusCode = code; this.statusText = text;}
    public void setHeaders(String key, String value) {headers.put(key, value);}
    public void setBody(byte[] body) {this.body = body;}
    
    public void send() throws IOException {
        StringBuilder response = new StringBuilder();
        
        if (body == null) {
            body = new byte[0];
        }
        
        
        
        //BUILD RESPONSE
        headers.put("Content-Length", String.valueOf(body.length));
        response.append(version)
                .append(" ")
                .append(statusCode)
                .append(" ")
                .append(statusText)
                .append("\r\n");


        for(Map.Entry<String, String> entry : headers.entrySet()){
            response.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\r\n");
        }
        response.append("\r\n");
        byte[] responseBytes = response.toString().getBytes();
        out.write(responseBytes);
        out.write(body);
        out.flush();
    }
}
