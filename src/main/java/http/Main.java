package http;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args)
    {
        initStorageDirectory();

        SimpleServer server = new SimpleServer();
        server.start();
    }

    public static void initStorageDirectory()
    {
        try
        {
            Path dataDir = Paths.get("data");
            if(Files.notExists(dataDir))
                Files.createDirectory(dataDir);

            for(int i = 0; i < 11; i++)
            {
                Path fileToCreatePath = dataDir.resolve("file_"+i+".txt");
                Path filePath = Files.createFile(fileToCreatePath);
                Files.write(filePath, Arrays.asList("Hello","Im", "File "+i));
            }

        }catch (Exception ignore)
        {

        }
    }

}
