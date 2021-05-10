package view;

import controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    AOWLNPanel aowlnPanel;

    public MainFrame(ViewController viewController) throws HeadlessException {
        super("AOWLN Plugin");
        aowlnPanel = new AOWLNPanel(viewController);
        this.add(aowlnPanel);
        this.setSize(new Dimension(1000, 800));
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public AOWLNPanel getAOWLNPanel(){
        return aowlnPanel;
    }
}
