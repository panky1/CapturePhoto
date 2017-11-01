package com.example.ninadgawankar.capturephoto;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Ninad Gawankar on 04/10/2017.
 */

public class FileController {
    public static Boolean sendToRemotePc(String serverIP, String srcPath, String destPath) throws IOException {
        InputStream in = new FileInputStream(new File(srcPath));
        File f = new File("\\\\" + serverIP + "\\" + destPath);
        Boolean isCreated = f.createNewFile();
        OutputStream out = new FileOutputStream(f);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return true;
    }

//    public static Boolean sendToRemotePcAuthorised(String serverIP, String srcPath, String destPath) throws IOException {
//        String hostname = "some.remote.machine"; //Remote FTP server: Change this
//        String username = "user"; //Remote user name: Change this
//        String password = "start123"; //Remote user password: Change this
//        //String upfile = args[0]; //File to upload passed on command line
//        String remdir = "/home/user"; //Remote directory for file upload
//        FtpClient ftp = new FtpClient();
//        try {
//            ftp.openServer(hostname); //Connect to FTP server
//            ftp.login(username, password); //Login
//            ftp.binary(); //Set to binary mode transfer
//            ftp.cd(remdir); //Change to remote directory
//            File file = new File(upfile);
//            OutputStream out = ftp.put(file.getName()); //Start upload
//            InputStream in = new FileInputStream(file);
//            byte c[] = new byte[4096];
//            int read = 0;
//            while ((read = in.read(c)) != -1 ) {
//                out.write(c, 0, read);
//            } //Upload finished
//            in.close();
//            out.close();
//            ftp.closeServer(); //Close connection
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }



    public static String fileToBase64String(String srcFilePath, String srcFileName) throws IOException {
        if (srcFileName.equals("")) {
            return Base64Controller.encodeFromFile(srcFilePath);
        } else {
            return Base64Controller.encodeFromFile(srcFilePath + srcFileName);
        }
    }

    public static byte[] base64StringToFile(String base64String) throws IOException {
        return Base64Controller.decode(base64String);
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }


    public static void copyFile() {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            File afile = new File("C:\\folderA\\Afile.txt");
            File bfile = new File("C:\\folderB\\Afile.txt");
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);
            byte[] buffer = new byte[1024];
            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
            //delete the original file
            afile.delete();
            System.out.println("File is copied successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean generateImageFromBitmap(String destinationFilePath, String destinationFileName, Bitmap sourceBitmap) throws IOException {
        File destination = new File(destinationFilePath + destinationFileName);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fo = null;
        destination.createNewFile();
        fo = new FileOutputStream(destination);
        fo.write(bytes.toByteArray());
        fo.close();
        return true;
    }

    public static String[] readSettingFile(String settingFilePath, String settingFile) throws IOException {
        try {
            String[] credentials = new String[2];
            File fin = new File(settingFilePath + settingFile);
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] tempLine1 = line.split("~");
                credentials[i] = tempLine1[1];
                i++;
            }
            br.close();
            return credentials;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{e.toString()};
        }
//        BufferedReader br = new BufferedReader(new FileReader(settingFilePath + settingFile));
//        try {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//            while (line != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            return sb.toString();
//        } finally {
//            br.close();
//        }
    }
}