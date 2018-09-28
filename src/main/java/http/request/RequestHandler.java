package http.request;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler
{
    // GET /favicon.ico HTTP/1.1
    private static Pattern statusLinePattern = Pattern.compile("(GET|POST|PUT)\\s(.*)\\s(HTTP.*)");
    private static Pattern headerPattern = Pattern.compile("(.*):(.*)");

    public static Request parseRequestString(String strRequest)
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

    public static void main(String[] args)
    {
        String strRequest = "GET /data/file.1 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Cache-Control: max-age=0\n";

        Request request = RequestHandler.parseRequestString(strRequest);
        System.out.println(request.getMethod() + " " + request.getQuery() + " " +request.getProtocol());
        for(Header header : request.getHeaders())
            System.out.println(header.getName() + ":" + header.getValue());
    }
}
