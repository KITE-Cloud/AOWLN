package view;

import model.observer.DataModel;
import org.apache.commons.io.FileUtils;
import utilities.FileUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class StartUpEngine implements ActionListener {

    JButton aContinue;
    File selectedFile;

    public File getSelectedFile() {
        return selectedFile;
    }

    JDialog fileChooserDialog;
    public void displayFileChooserDialog(JFrame owner) {
        fileChooserDialog = new JDialog(owner, "Settings");
        fileChooserDialog.setLayout(new BorderLayout());
        fileChooserDialog.add(getFileChooserPanel(), BorderLayout.CENTER);
        aContinue = new JButton("Continue");
        aContinue.setActionCommand("CONTINUE");
        aContinue.setEnabled(false);
        aContinue.addActionListener(this);
        fileChooserDialog.add(aContinue, BorderLayout.SOUTH);
        fileChooserDialog.setModal(false);
        fileChooserDialog.setUndecorated(false);
        fileChooserDialog.pack();
        fileChooserDialog.setLocationRelativeTo(null);
        fileChooserDialog.setVisible(true);
    }
    JPanel panel;
    JTextField pathField;
    private JPanel getFileChooserPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("<html>Please enter a path to aowln-image-engine.jar.  You can<br> download the file at https://github.com/KITE-Cloud/AOWLN</html>");
        panel.add(label, BorderLayout.NORTH);
        JPanel fileChooserPanel = new JPanel(new FlowLayout());
        pathField = new JTextField();
        pathField.setPreferredSize(new Dimension(200,25));
        pathField.setEnabled(false);
        fileChooserPanel.add(pathField);

        JButton btn_fileChooser = new JButton("Select JAR");
        btn_fileChooser.setActionCommand("OPEN_FILECHOOSER");
        btn_fileChooser.addActionListener(this);

        fileChooserPanel.add(pathField);
        fileChooserPanel.add(btn_fileChooser);
        panel.add(fileChooserPanel, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("OPEN_FILECHOOSER")){

            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Jar Files", "jar");
            jfc.setFileFilter(filter);
            jfc.setMultiSelectionEnabled(false);
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {

                selectedFile = jfc.getSelectedFile();

                if(selectedFile.getName().contains("jar")){
                    if(FileUtil.runJarUsingCMD(selectedFile.getAbsolutePath(), "TEST").contains("SUCCESSFUL")){
                        aContinue.setEnabled(true);
                    }else {
                        JOptionPane.showMessageDialog(panel, "Wrong JAR Selected", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.out.println(selectedFile.getAbsolutePath());
                pathField.setText(selectedFile.getAbsolutePath());
            }
        }

        if (e.getActionCommand().equals("CONTINUE")) {
            File selectedFile = this.getSelectedFile();
            System.out.println("selected file: " + selectedFile.getPath());

            try {
                String path = AOWLNPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath(); //plugin location
                String decodedPath = URLDecoder.decode(path, "UTF-8");
                System.out.println("Decoded Path of AOWLNPanel = " + decodedPath);
                System.out.println("Decoded Path 2 of AOWLNPanel = " + this.getParent(this.getParent(decodedPath)));
                copyFileToProtegeDirectory(selectedFile);
            } catch (UnsupportedEncodingException exception) {
                exception.printStackTrace();
            }

            DataModel.getInstance().setGraphVizEngine(selectedFile);
            fileChooserDialog.setVisible(false);
        }
    }

    private void copyFileToProtegeDirectory(File source) {
        try {
            String path = AOWLNPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            File dest = new File(this.getParent(this.getParent(decodedPath)) + "/aowln-image-engine.jar");

            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getParent(String resourcePath) {
        int index = resourcePath.lastIndexOf('/');
        if (index > 0) {
            return resourcePath.substring(0, index);
        }
        return "/";
    }

      /*  public static void main(String[] args) {

        JFrame frame = new JFrame("JFrame Example");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartUpEngine().displayFileChooserDialog(frame);
            }
        });
    }*/
}