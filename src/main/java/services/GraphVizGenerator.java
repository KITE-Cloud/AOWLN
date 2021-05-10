package services;

import com.google.gson.Gson;
import model.observer.DataModel;
import model.GraphListsForViz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;


public class GraphVizGenerator {



    public void generateGraphImage(GraphListsForViz graphList, String rulePart) {

        Gson gson = new Gson();
        String graphListAsJson = gson.toJson(graphList);
        String encodedString = Base64.getEncoder().encodeToString(graphListAsJson.getBytes());

        File aowln_image_engine = DataModel.getInstance().getGraphVizEngine(); // forward slashes with java.io.File works
        String absolutePath = aowln_image_engine.getAbsolutePath();
        FileUtil.runJarUsingCMD(absolutePath, encodedString, rulePart.toUpperCase());
        File file = new File(System.getProperty("java.io.tmpdir") + "AOWLN-" + rulePart.toUpperCase() + ".png");
        BufferedImage image = null;

        for(int i = 0; i< 20; i++){
            try {
                if(file.exists()){
                    image = ImageIO.read(file);
                    file.delete();
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (rulePart.contains("head")) {
            DataModel.getInstance().setCurrentHead(image);
        } else if (rulePart.contains("body")) {
            DataModel.getInstance().setCurrentBody(image);
        }

    }


}
