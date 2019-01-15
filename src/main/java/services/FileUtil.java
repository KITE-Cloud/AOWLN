package services;

import java.io.*;

/**
 * Created by Thomas Farrenkopf on 22.01.17.
 */
public class FileUtil {

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
