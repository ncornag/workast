package es.workast.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Nicolás Cornaglia
 */
public final class FileUtils {

    public static final String UNIXSEPARATOR = "/"; // Unix based
    public static final String FILESEP = System.getProperty("file.separator");

    /**
     * Hidden constructor
     */
    private FileUtils() {
    }

    /**
     * Mueve un fichero de carpeta
     * 
     * @param sourceFile
     * @param destinationPath
     * @return
     */
    public static File moveFile(File sourceFile, String destinationPath, String destinationFileName) {
        File newFile = new File(destinationPath, destinationFileName);
        if (newFile.exists()) {
            newFile.delete();
        }
        if (sourceFile.renameTo(newFile)) {
            return newFile;
        } else {
            return null;
        }
    }

    /**
     * Mueve un fichero de carpeta
     * 
     * @param sourceFile
     * @param destinationPath
     * @return
     */
    public static File moveFile(File sourceFile, String destinationPath) {
        return moveFile(sourceFile, destinationPath, sourceFile.getName());
    }

    /**
     * Mueve un fichero de carpeta
     * 
     * @param sourceFilePath
     * @param destinationPath
     * @return
     */
    public static File moveFile(String sourceFilePath, String destinationPath) {
        File sourceFile = new File(sourceFilePath);
        return moveFile(sourceFile, destinationPath, sourceFile.getName());

    }

    /**
     * Mueve un fichero de carpeta
     * 
     * @param sourcePath
     * @param sourceFileName
     * @param destinationFilePath
     * @return
     */
    public static File moveFile(String sourcePath, String sourceFileName, String destinationFilePath) {
        return moveFile(sourcePath + File.separator + sourceFileName, destinationFilePath);
    }

    /**
     * @param in
     * @param out
     * @return
     * @throws IOException
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        try {
            int byteCount = 0;
            byte[] buffer = new byte[8192];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

}
