package view;


import controller.ViewController;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

/**
 * Created by Jannik Geyer on 25.10.2017.
 */
public class AOWLNProtegeView extends AbstractOWLViewComponent {

   // private static final Logger log = LoggerFactory.getLogger(AOWLNProtegeView.class);

    protected void initialiseOWLView() throws Exception {
        ViewController.getInstance().start(this);
    }

    protected void disposeOWLView() {
    }

}
