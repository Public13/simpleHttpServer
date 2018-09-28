package http;

import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer
{
    private static Logger log = Logger.getLogger(SimpleServer.class);

    private static ExecutorService execPool = Executors.newFixedThreadPool(3);

    public void start()
    {
        try (ServerSocket serverSocket = new ServerSocket(8080))
        {
            log.info("Server start");
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                execPool.execute(new SocketHandler(socket));
            }
        }catch (Throwable e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            execPool.shutdown();
        }
    }
}