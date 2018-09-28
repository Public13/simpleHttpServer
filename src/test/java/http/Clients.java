package http;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Clients
{
    private static Logger log = Logger.getLogger(Clients.class);

    private static ExecutorService execPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Throwable
    {
        log.info("Clients start");
        for(int i = 0; i < 12; i++)
            execPool.execute(new Client(i));
        execPool.shutdown();
        execPool.awaitTermination(600, TimeUnit.SECONDS);
    }

    private static class Client implements Runnable, Callable
    {
        private int id;

        public Client(int id)
        {
            this.id = id;
        }

        @Override
        public void run()
        {
            Socket socket = null;
            BufferedReader in = null;
            BufferedWriter out = null;
            try
            {
                socket = new Socket("localhost", 8080);
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader((new InputStreamReader(socket.getInputStream())));

                String req = "GET /file_" + id + ".txt HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:62.0) Gecko/20100101 Firefox/62.0\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                        "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3\n" +
                        "Accept-Encoding: gzip, deflate\n" +
                        "Connection: close\nr" +
                        "Cache-Control: no-cache\n\r\n\r";
                out.write(req);
                out.flush();

                in.lines().forEach(log::info);
                log.info("=============================================== Client " + id + " ends =============================================== ");

            } catch (IOException e)
            {
                log.error(e.getMessage(), e);
            } finally
            {
                try
                {
                    if(out != null)
                        out.close();
                    if(in != null)
                        in.close();
                    if(socket != null)
                        socket.close();
                } catch (Throwable ignore)
                {
                }
            }
        }

        @Override
        public Object call() throws Exception
        {
            return null;
        }
    }
}
