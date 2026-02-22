
package http.server;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author farel
 */
public class HttpRequest {
    private String method;
    private String path;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;
    

    public HttpRequest(InputStream in) throws IOException{
        parseRequestHeader(in);
        parseBody(in);
    }
    public String getMethod(){return method;}
    public String getPath(){return path;}
    public String getVersion(){return version;}
    public Map<String, String> getHeaders() {return headers;}
    public byte[] getBody() {return body;}
    
    private byte[] readHeaderBytes(InputStream in) throws IOException{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  
        int current;
        int matched = 0;
        byte[] delimiter = {13,10,13,10};
        while((current = in.read()) != -1){
            buffer.write(current);
            if(current == delimiter[matched])
                matched++;
            else 
               matched = (current == delimiter[0])? 1 : 0;
            
            if(matched == delimiter.length)break;
        }
        return buffer.toByteArray();
    }
    private void parseRequestHeader(InputStream in) throws IOException{
        int lineIndex = 0;
        //PARSE REQUEST

        byte[] headerbytes = readHeaderBytes(in);
        String requestHeader = new String(headerbytes, StandardCharsets.UTF_8);
        String[] lines = requestHeader.split("\n");
//        int idx = 0;
//        for(String a : lines){
//            System.out.println(idx++ + " : "+a);
//        }
        String[] parts = lines[lineIndex++].split(" ");
        if(parts.length != 3){
            throw new IOException("Malformed request line");
        }
        
        method = parts[0];
        path = parts[1];
        version = parts[2];
//        System.out.println("METHOD  : " + method);
//        System.out.println("PATH    : " + path);
//        System.out.println("VERSION : " + version);
//        
        // PARSE HEADER KV

        
        while( lineIndex < lines.length && lines[lineIndex] != null && !lines[lineIndex].isEmpty()){
            String[] headerParts = lines[lineIndex].split(":");
            if(headerParts.length == 2){
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
//            System.out.println( headerParts[0]+  " : "+ headers.get(headerParts[0]));
            lineIndex++;
        }
        
    }

    private void parseBody(InputStream in) throws IOException {
        String contextLengthHeader = headers.get("Content-Length");
        if(contextLengthHeader == null){
            return;
        }
        
        int contentLength = Integer.parseInt(contextLengthHeader);
        body = new byte[contentLength];
        int bytesRead = 0;
        while(bytesRead < contentLength){
            int result = in.read(body, bytesRead, contentLength-bytesRead);
            if(result == -1){
                throw new IOException("Unexpected end of stream");
            }
            bytesRead += result;
        }
    }
    
}
