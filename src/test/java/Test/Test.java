package Test;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.model.Label;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class Test extends JFrame{




    public Test() {

        MutableNode
                init = mutNode("init"),
                execute = mutNode("execute"),
                compare = mutNode("compare").add(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
                mkString = mutNode("mkString").add(Label.of("make a\nstring")),
                printf = mutNode("printf"),
                main = mutNode("main").add(Shape.TRAPEZIUM),
                parse = mutNode("parse");



        //Graph g1 = graph("example1").directed().with(init).link(to(execute));
        //@Thomas: Einzelne zuweisungen von Nodes gehen nicht.

        Link linkPrintf = printf.linkTo();
        linkPrintf = linkPrintf.with(Style.BOLD, Label.of("100 times"));
        MutableNode node = compare.addLink(linkPrintf);

        Link link1 = main.linkTo();
        MutableNode add = printf.addLink(link1);

        Link link2 = mkString.linkTo();
        MutableNode main2 = main.addLink(link2);

        //g2 = g2.with(node);
        //  g2 = g2.with(node("1").link(node("2")));

        MutableGraph mutableGraph = new MutableGraph();
        mutableGraph.setDirected(true);

        mutableGraph = mutableGraph.add(node);
        mutableGraph= mutableGraph.add(add);
        mutableGraph= mutableGraph.add(main2);
        Link link = parse.linkTo();
        node.addLink(link);
        mutableGraph = mutableGraph.add(node);
        Collection<MutableNode> nodes = mutableGraph.nodes();

        //  mutableGraph = mutableGraph.add(execute);
     //  mutableGraph = mutableGraph.add(execute).addLink(init);
        //   mutableGraph = mutableGraph.add(init);
       // mutableGraph.with
        //@Thomas: Dieser Lange Teil produziert einen brauchbaren graphen. Der Graph wird als Bild gespeichert und dann in einem Frame dargestellt
 /*    Graph g = graph("example2").directed().with(
                node("main").with(Shape.TRAPEZIUM).link(
                        to(mutNode("parse").addLink(execute)).with("weight", 8),
                        to(init).with(Style.DASHED),
                        node("cleanup"),
                        to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)),
                execute.addLink(
                        graph().with(mkString, printf),
                        to(compare).with(Color.RED)),
                init.addLink(mkString));
*/

 System.out.println();
        String path = "example/ex6.png";
        try {
            Graphviz.fromGraph(mutableGraph).width(300).render(Format.PNG).toFile(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ShowPNG(path);

        this.setVisible(true);
        pack();
    }

    private void ShowPNG(String arg){
        if (arg == null ) {
            arg = "example/ex1.png";
        }
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setSize(500,640);

        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(arg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width          = bimg.getWidth();
        int height         = bimg.getHeight();
        double w2 = width;
        System.out.println(width + " --- "+ height+ " --- " + (w2));

        Image icon = null;
        if(width>600){

            w2 = (600.0/(double)width);


            icon = new ImageIcon(arg).getImage().getScaledInstance(600, (int)(w2 *  height), Image.SCALE_DEFAULT);
        }
        else{
            icon = new ImageIcon(arg).getImage();
        }



        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(icon));

        panel.setPreferredSize(new Dimension(800, 800));
        panel.add(label);
        this.getContentPane().add(panel);
    }
    public static void main(String[] args) {
        Test frame = new Test();
    }

}
