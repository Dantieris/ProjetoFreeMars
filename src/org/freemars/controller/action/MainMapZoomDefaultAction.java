package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.viewcommand.SetMainMapZoomLevelCommand;

/**
 *
 * @author Deniz ARIKAN
 */
public class MainMapZoomDefaultAction extends AbstractAction {

    private FreeMarsController freeMarsController;

    public MainMapZoomDefaultAction(FreeMarsController freeMarsController) {
        super("Default zoom");
        this.freeMarsController = freeMarsController;
    }

    public void actionPerformed(ActionEvent e) {
        freeMarsController.executeViewCommand(new SetMainMapZoomLevelCommand(freeMarsController, 3));
    }
}
