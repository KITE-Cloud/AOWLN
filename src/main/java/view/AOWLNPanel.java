package view;

import controller.MainController;
import interfaces.ModelObserver;
import model.DataModel;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import services.StartUpEngine;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AOWLNPanel extends JPanel implements ActionListener, ModelObserver, ChangeListener, ComponentListener, OWLOntologyChangeListener {


    private final MainController mainController;
    private JComboBox<String> ruleBox;
    JPanel rightCanvas;
    JPanel leftCanvas;

    JSlider sliderRightPane;
    JSlider sliderLeftPane;

    Image rootImgBody = null;
    Image rootImgHead = null;
    private int initialImageZoom = 50;
    private JLabel loadingAnimation;


    ExecutorService executor = Executors.newSingleThreadExecutor();


    public AOWLNPanel(MainController mainController) {
        this.setLayout(new BorderLayout());

        this.mainController = mainController;

        initRulesBar();

        initCanvasArea();

        this.addComponentListener(this);
        
        this.loadGraphVizEngine();

    }

    private void loadGraphVizEngine() {

        StartUpEngine startUpEngine = new StartUpEngine();

        JFrame frame = (JFrame)this.getTopLevelAncestor();

        startUpEngine.displayFileChooserDialog(frame);

        File selectedFile = startUpEngine.getSelectedFile();

        /*JFileChooser chooser = new JFileChooser();
        // Dialog zum Oeffnen von Dateien anzeigen
        chooser.showOpenDialog(this);

        chooser.setMultiSelectionEnabled(false);
        File selectedFile = chooser.getSelectedFile();*/

        DataModel.getInstance().setGraphVizEngine(selectedFile);
    }

    private void initCanvasArea() {

        JPanel upperRightPane =  new JPanel(new BorderLayout()),
                lowerRightPane = new JPanel(new FlowLayout()),
                completeRightPane = new JPanel(new BorderLayout()),
                upperLeftPane = new JPanel(new BorderLayout()),
                lowerleftPane = new JPanel(new FlowLayout()),
                completeleftPane = new JPanel(new BorderLayout());
        upperRightPane.setBackground(Color.white);
        upperLeftPane.setBackground(Color.white);

        rightCanvas = new JPanel(new GridBagLayout());
        rightCanvas.setBackground(Color.white);

        leftCanvas = new JPanel(new GridBagLayout());
        leftCanvas.setBackground(Color.white);



        JScrollPane rightScrollPane = new JScrollPane(upperRightPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                leftScrollPane = new JScrollPane(upperLeftPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JButton btn_rightCenter = new JButton("Center");
        JButton btn_leftCenter = new JButton("Center");

        upperRightPane.add(rightCanvas, BorderLayout.CENTER);
        upperLeftPane.add(leftCanvas, BorderLayout.CENTER);

       // lowerRightPane.add(btn_rightCenter);
        sliderRightPane = new JSlider(JSlider.HORIZONTAL,0, 100, initialImageZoom);
        sliderRightPane.setSnapToTicks(false);
        sliderRightPane.addChangeListener(this);

//Turn on labels at major tick marks.
        sliderRightPane.setMajorTickSpacing(20);
        sliderRightPane.setMinorTickSpacing(5);
        sliderRightPane.setPaintTicks(true);
        sliderRightPane.setPaintLabels(true);
        lowerRightPane.add(new JLabel("Zoom in %:"));
        lowerRightPane.add(sliderRightPane);

      //   lowerleftPane.add(btn_leftCenter);
        sliderLeftPane = new JSlider(JSlider.HORIZONTAL,0, 100, initialImageZoom);
        sliderLeftPane.setSnapToTicks(false);

        sliderLeftPane.addChangeListener(this);

//Turn on labels at major tick marks.
        sliderLeftPane.setMajorTickSpacing(20);
        sliderLeftPane.setMinorTickSpacing(5);
        sliderLeftPane.setPaintTicks(true);
        sliderLeftPane.setPaintLabels(true);
        lowerleftPane.add(new JLabel("Zoom in %:"));
        lowerleftPane.add(sliderLeftPane);


        completeRightPane.add(rightScrollPane, BorderLayout.CENTER);
        completeRightPane.add(lowerRightPane, BorderLayout.SOUTH);
        completeRightPane.setBackground(Color.white);

        completeleftPane.add(leftScrollPane, BorderLayout.CENTER);
        completeleftPane.add(lowerleftPane, BorderLayout.SOUTH);
        completeleftPane.setBackground(Color.white);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,completeleftPane, completeRightPane);
        splitPane.setResizeWeight(0.6);

        this.add(splitPane, BorderLayout.CENTER);
    }

    private void initRulesBar() {

        JPanel rulesBar = new JPanel(new FlowLayout());


        ruleBox = new JComboBox<String>();



        ruleBox.setPreferredSize(new Dimension(800, (int) ruleBox.getPreferredSize().getHeight()));
        ruleBox.addActionListener(this);

        JButton btn_select = new JButton("Select");
        btn_select.addActionListener(this);

        JButton btn_edit = new JButton("Edit");
        btn_edit.addActionListener(this);

        MediaTracker mediaTracker = new MediaTracker(this);
        URL resource = this.getClass().getResource("/loadingAnimation.gif");
        Image icon = new ImageIcon(resource).getImage();
        mediaTracker.addImage(icon, 1);

        loadingAnimation = new JLabel(new ImageIcon(icon));
        //loadingAnimation.setVisible(false);
        loadingAnimation.setVisible(false);

        rulesBar.add(ruleBox, 0);
        rulesBar.add(btn_select, 1);
        //rulesBar.add(btn_edit,2);
        rulesBar.add(loadingAnimation, 2);

        this.add(rulesBar, BorderLayout.NORTH);

    }

    public void fillRuleBox(ArrayList<String> rules) {
        java.util.Collections.sort(rules);
        if (ruleBox.getItemCount() > 0) ruleBox.removeAllItems();
        for (String renderedRule : rules) {
            ruleBox.addItem(renderedRule);
        }
        ruleBox.repaint();
    }



    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand() == "Select"){
            if(ruleBox.getItemCount() > 0) {
                String selectedRule = (String) ruleBox.getSelectedItem();
                String ruleName = selectedRule.substring(0, selectedRule.indexOf(":"));

                executor.submit(() -> {
                    try {
                        loadingAnimation.setVisible(true);
                        mainController.produceRuleImages(ruleName);
                        this.sliderRightPane.setValue(initialImageZoom);
                        this.sliderLeftPane.setValue(initialImageZoom);
                        loadingAnimation.setVisible(false);
                    } catch (Exception ex) {
                        loadingAnimation.setVisible(false);
                        JOptionPane.showMessageDialog(null,
                                "Your rule structure does not comply to the required AOWLN convention.\n" +
                                        "See http://bit.ly/AOWLN-Paper for more information.",
                                "Processing Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                });

            }else{
                JOptionPane.showMessageDialog(null,
                        "No rules found.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        }

    }

    @Override
    public void ruleListHasChanged(ArrayList<String> rules) {
        fillRuleBox(rules);
    }

    public void loadImages() {
        rootImgBody = DataModel.getInstance().getCurrentBody();
        rootImgBody.flush();
        rootImgHead = DataModel.getInstance().getCurrentHead();
        rootImgHead.flush();
        updateCanvasArea("Body", initialImageZoom);
        updateCanvasArea("Head", initialImageZoom);

    }

    private void updateCanvasArea(String type, int scalePercentage) {
        if(rootImgBody != null || rootImgHead != null) {

            Image img = null;
            if (type.equals("Body"))
                img = scaleImage(rootImgBody, scalePercentage);
            else if (type.equals("Head"))
                img = scaleImage(rootImgHead, scalePercentage);
            JLabel imgLabel = new JLabel();
            imgLabel.setIcon(new ImageIcon(img));

            if (type.equals("Body")) {
                leftCanvas.removeAll();
                leftCanvas.add(imgLabel);
            } else if (type.equals("Head")) {
                rightCanvas.removeAll();
                rightCanvas.add(imgLabel);
            }
            this.repaint();
            this.revalidate();
        }

    }


    private Image scaleImage(Image img, int percentage){
        double normedPercentage = (double)percentage / 100d;

        Image toolkitImage = img.getScaledInstance((int)(img.getWidth(null)*normedPercentage), (int)(img.getHeight(null)*normedPercentage),
                Image.SCALE_SMOOTH);
        
        return toolkitImage;
    }

    int lastSliderValue = initialImageZoom;
    public void stateChanged(ChangeEvent e) {

        if(e.getSource() instanceof JSlider){
            JSlider source = (JSlider)e.getSource();

            int value = source.getValue();
            int mod = value % 5;

            if(mod > 3 ){
                value = value - mod + 5;
            }else{
                value = value - mod;
            }

            source.setValue(value);

            if(value != lastSliderValue) {
                lastSliderValue = value;
                if (source == sliderLeftPane) {
                    updateCanvasArea("Body", source.getValue());
                }

                if (source == sliderRightPane) {
                    updateCanvasArea("Head", source.getValue());
                }
            }

        }
    }


    @Override
    public void componentResized(ComponentEvent e) {
        ruleBox.setPreferredSize(new Dimension(this.getWidth()-250, (int) ruleBox.getPreferredSize().getHeight()));
        this.repaint();
        this.revalidate();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    ExecutorService executorService = Executors.newFixedThreadPool(1);
    @Override
    public void ontologiesChanged(@Nonnull List<? extends OWLOntologyChange> changes) throws OWLException {

        System.out.println("ONTOLOGY HAS CHANGED");


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainController.getInstance().loadRulesfromOntology();
            }
        });
    }
}
