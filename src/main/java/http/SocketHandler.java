package http;

import http.request.Request;
import http.response.En_Status;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class SocketHandler implements Runnable
{
    private static Logger log = Logger.getLogger(SocketHandler.class);
    private static HttpHelper httpHelper = new HttpHelper();

    private Socket socket;
    private BufferedReader inReader;
    private OutputStream os;

    public SocketHandler(Socket socket) throws Throwable
    {
        this.socket = socket;
        this.socket.setSoTimeout(10000);
        this.inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.os = socket.getOutputStream();
    }

    public void run()
    {
        log.info("Client accepted");
        Path dataDir = Paths.get("data");
        try
        {
            Request request = readRequest();

            if(request == null)
            {
                httpHelper.writeTextResponse(En_Status.BAD_REQUEST, "", os);
                log.info("<< return BAD_REQUEST");
                return;
            }

            String query = request.getQuery();
            if(query == null || query.isEmpty() || query.equals("/") || query.equalsIgnoreCase("/method=ls"))
            {
                httpHelper.writeTextResponse(En_Status.OK, Files.list(dataDir).map(Path::getFileName).sorted().collect(Collectors.toList()).toString(), os);
                log.info("<< return files list");
                return;
            }

            if(query.endsWith("/"))
            {
                Path insideDir = Paths.get("data"+query);
                httpHelper.writeTextResponse(En_Status.OK, Files.list(insideDir).map(Path::getFileName).sorted().collect(Collectors.toList()).toString(), os);
                log.info("<< return files list");
                return;
            }

            if(query.startsWith("/"))
                query = query.substring(1);

            Path filePath = dataDir.resolve(query);
            if(Files.notExists(filePath))
            {
                httpHelper.writeTextResponse(En_Status.NOT_FOUND, "", os);
                log.info("<< return NOT_FOUND");
                return;
            }

            httpHelper.writeFileResponse(filePath, os);
            log.info("<< return file");

        } catch (Throwable t)
        {
            log.error(t.getMessage(), t);
        } finally
        {
            try
            {
                inReader.close();
                os.close();
                socket.close();
            } catch (Throwable ignore)
            {
            }
        }
        log.info("Client processing finished");
    }

    private Request readRequest() throws Throwable
    {
        StringBuilder buf = new StringBuilder();

        while(true) // or SoTimeout
        {
            String line = inReader.readLine();
            if(line.trim().isEmpty())
                break;
            buf.append(line).append("\n");

        }
        log.info(">> client request \n");
        log.info(buf.toString());
        return httpHelper.parseRequestString(buf.toString());
    }
}
