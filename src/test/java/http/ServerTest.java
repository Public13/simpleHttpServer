package http;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;

public class ServerTest
{
    private static Logger log = Logger.getLogger(ServerTest.class);

    @Test
    public void runTest() throws Throwable
    {
        log.info("Tests start");

        Main.initStorageDirectory();

        Thread serverThread = new Thread(() -> {
            SimpleServer server = new SimpleServer();
            server.start();
        });

        serverThread.start();
        Thread.sleep(1000);
        Clients.main(new String[0]);

        serverThread.interrupt();
    }
}
