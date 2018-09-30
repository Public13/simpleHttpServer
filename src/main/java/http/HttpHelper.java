package http;

import http.request.Header;
import http.request.Request;
import http.response.En_Status;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHelper
{
    private static Pattern statusLinePattern = Pattern.compile("(GET|POST|PUT)\\s(.*)\\s(HTTP.*)");
    private static Pattern headerPattern = Pattern.compile("(.*):(.*)");

    public Request parseRequestString(String strRequest)
    {
        if(strRequest==null || strRequest.isEmpty())
            return null;

        Request request = new Request();
        Scanner scanner = new Scanner(strRequest);
        Matcher matcher = statusLinePattern.matcher(scanner.nextLine());
        if(matcher.matches())
        {
            request.setMethod(matcher.group(1));
            request.setQuery(matcher.group(2));
            request.setProtocol(matcher.group(3));
        }
        else
        {
            return null;
        }

        request.setHeaders(new ArrayList<>());
        while(scanner.hasNextLine())
        {
            matcher = headerPattern.matcher(scanner.nextLine());
            if(matcher.matches())
                request.getHeaders().add(new Header(matcher.group(1).trim(),matcher.group(2).trim()));
        }

        return request;
    }

    public void writeFileResponse(Path filePath, OutputStream os) throws Throwable
    {
        String response = "HTTP/1.1 " + En_Status.OK.getHttpStatus() + "\r\n" +
                "Server: PavelServer/2018-09-28\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Length: " + Files.size(filePath) + "\r\n" +
                "Connection: close\r\n\r\n";
        os.write(response.getBytes());
        os.write(Files.readAllBytes(filePath));
        os.flush();
    }

    public void writeTextResponse(En_Status status, String strResponse, OutputStream os) throws Throwable
    {
        String response = "HTTP/1.1 " + status.getHttpStatus() + "\r\n" +
                "Server: PavelServer/2018-09-28\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + strResponse.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + strResponse;
        os.write(result.getBytes());
        os.flush();
    }
}
