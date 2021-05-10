package utilities;

import java.io.*;

/**
 * Created by Thomas Farrenkopf on 22.01.17.
 */
public class FileUtil {

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

    public static String runJarUsingCMD(String jarPath, String ... argument ){
        try {
            String exec_command = "java -jar \"" + jarPath + "\" ";
            for (String arg : argument) {
                exec_command += arg + " ";
            }

            Process proc = null;

            proc = Runtime.getRuntime().exec(exec_command);
            proc.waitFor();
            // Then retreive the process output
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();

            byte b[]=new byte[in.available()];
            in.read(b,0,b.length);
            String input_message = new String(b);
            System.out.println();

            byte c[]=new byte[err.available()];
            err.read(c,0,c.length);
            String error_message = new String(c);
            System.err.println(error_message);

            return input_message;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static InputStream getInputStream(String filePath){
        InputStream inputStream = null;
        File f = new File(filePath);

        if(f.exists()){
            try {
                inputStream = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else{
            inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
        }

        return inputStream;
    }

    public static OutputStream getOutputStream(String filePath){
        OutputStream outputStream = null;
        File f = new File(filePath);


            try {
                outputStream = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        return outputStream;
    }
}
