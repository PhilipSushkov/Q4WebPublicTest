package q4WebTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
 
/**
 * A utility that downloads a file from a URL.
 * @author www.codejava.net
 *
 */
public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;
    private static final String sImage = "image/(png|gif|img)";
 
    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public static String downloadFile(String fileURL, String saveDir, String fileName)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        //System.out.println("Content-Type = " + httpConn.getContentType());
 
        // always check HTTP response code first
        if ( (responseCode == HttpURLConnection.HTTP_OK) && (httpConn.getContentType().matches(sImage)) ) {
            //String fileName = "";
            //String disposition = httpConn.getHeaderField("Content-Disposition");
            //String contentType = httpConn.getContentType();
            //int contentLength = httpConn.getContentLength();

            //fileName = "Chart.png";
 
            //System.out.println("Content-Type = " + contentType);
            /* System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName); */
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            fileName = fileName + httpConn.getContentType().split("/")[1];
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            //System.out.println("File downloaded");
            httpConn.disconnect();
            return fileName;
        } else {
            //System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            httpConn.disconnect();
        	return "";
        }

    }
}
