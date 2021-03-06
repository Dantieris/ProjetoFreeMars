package org.freemars.colonydialog.controller;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.freemars.colonydialog.ColonyDialogModel;
import org.freemars.controller.FreeMarsController;

/**
 *
 * @author Deniz ARIKAN
 */
public class ColonyResourceTransferHandler extends ResourceTransferHandler {

    public ColonyResourceTransferHandler(FreeMarsController freeMarsController, ColonyDialogModel model) {
        super(freeMarsController, model);
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        getModel().setResourceTransferSource(getModel().getColony());
        JTable jTable = (JTable) c;
        int row = jTable.getSelectedRow();
        //
        Object value = ((ArrayList)jTable.getValueAt(row, 1)).get(1);
        Logger.getLogger(this.getClass()).debug(value.getClass());
        //
        String resource = value.toString();
        int requestedTransferAmount = getModel().getResourceTransferAmount();
        int colonyResourceQuantity = getModel().getColony().getResourceQuantity(getModel().getRealm().getResourceManager().getResource(resource));
        int quantity = requestedTransferAmount > colonyResourceQuantity ? colonyResourceQuantity : requestedTransferAmount;
        return new ResourceTransferable(resource, quantity);
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        setSource(getModel().getSelectedUnit());
        setDestination(getModel().getColony());
        boolean result = super.importData(comp, t);
        getModel().refresh(ColonyDialogModel.UNIT_CARGO_CHANGE_UPDATE);
        return result;
    }
}
