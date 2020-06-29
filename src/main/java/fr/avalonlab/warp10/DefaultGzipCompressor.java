package fr.avalonlab.warp10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class DefaultGzipCompressor {
    private static final Logger logger = Logger.getLogger("DefaultGzipCompressor");

    public static final String CONTENT_TYPE = "application/gzip";

    public static byte[] compress(List<String> data){
        String batchData = String.join(System.getProperty("line.separator"), data);

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(batchData.length());
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(batchData.getBytes());
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();

            return compressed;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while GZipping data", e);
        }

        return new byte[0];
    }
}
