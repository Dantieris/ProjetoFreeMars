package org.freemars.earth.support;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.FreeMarsController;

/**
 *
 * @author Deniz ARIKAN
 */
public class DisplayFreeTransporterMessageAction extends AbstractAction {

    private FreeMarsController controller;

    public DisplayFreeTransporterMessageAction(FreeMarsController controller) {
        super("Free transporter");
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        FreeTransporterDialog dialog = new FreeTransporterDialog(controller.getCurrentFrame(), controller);
        dialog.display();
    }
}
