package org.freemars.controller.action;

import org.freemars.controller.FreeMarsController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.freemars.controller.viewcommand.PlaySoundCommand;
import org.freemars.earth.EarthFlightProperty;
import org.freerealm.executor.command.SetActiveUnitCommand;
import org.freerealm.executor.command.UnitOrderAddCommand;
import org.freerealm.unit.Unit;
import org.freemars.earth.Location;
import org.freemars.earth.ModifyHydrogenConsumption;
import org.freemars.ui.util.FreeMarsOptionPane;
import org.freemars.earth.order.RelocateUnitOrder;
import org.freemars.player.FreeMarsPlayer;
import org.freerealm.Utility;
import org.freerealm.player.Player;
import org.freerealm.settlement.Settlement;
import org.freerealm.resource.Resource;
import org.freerealm.tile.Tile;

/**
 * @author Deniz ARIKAN
 */
public class SendUnitToEarthAction extends AbstractAction {

    private final FreeMarsController freeMarsController;
    private final Unit unit;

    public SendUnitToEarthAction(FreeMarsController freeMarsController, Unit unit) {
        super("Go to Earth");
        this.freeMarsController = freeMarsController;
        this.unit = unit;
    }

    public void actionPerformed(ActionEvent e) {
        Unit unitToOrder = unit != null ? unit : freeMarsController.getFreeMarsModel().getActivePlayer().getActiveUnit();
        if (unitToOrder.getCurrentOrder() == null) {
            FreeMarsPlayer player = (FreeMarsPlayer) unitToOrder.getPlayer();
            if (player.hasDeclaredIndependence()) {
                FreeMarsOptionPane.showMessageDialog(freeMarsController.getCurrentFrame(), "We can not send spaceships to Earth after declaration of independence", "Rejected");
            } else {
                EarthFlightProperty earthFlight = (EarthFlightProperty) unitToOrder.getType().getProperty("EarthFlight");
                if (earthFlight != null) {
                    Tile tile = freeMarsController.getFreeMarsModel().getTile(unitToOrder.getCoordinate());
                    Settlement colony = tile.getSettlement();
                    Resource hydrogenResource = freeMarsController.getFreeMarsModel().getRealm().getResourceManager().getResource("Hydrogen");
                    int hydrogenQuantity = colony.getResourceQuantity(hydrogenResource);
                    int requiredHydrogenQuantity = earthFlight.getHydrogenConsumption();
                    ModifyHydrogenConsumption modifyHydrogenConsumption = (ModifyHydrogenConsumption) player.getProperty("ModifyHydrogenConsumption");
                    if (modifyHydrogenConsumption != null) {
                        requiredHydrogenQuantity = (int) Utility.modifyByPercent(requiredHydrogenQuantity, modifyHydrogenConsumption.getModifier());
                    }
                    if (hydrogenQuantity >= requiredHydrogenQuantity) {
                        RelocateUnitOrder relocateUnitOrder = new RelocateUnitOrder(freeMarsController.getFreeMarsModel().getRealm());
                        relocateUnitOrder.setFreeMarsController(freeMarsController);
                        relocateUnitOrder.setUnit(unitToOrder);
                        relocateUnitOrder.setSource(Location.MARS);
                        relocateUnitOrder.setDestination(Location.EARTH);
                        relocateUnitOrder.setLaunchFromColony(colony);
                        freeMarsController.execute(new UnitOrderAddCommand(freeMarsController.getFreeMarsModel().getRealm(), unitToOrder, relocateUnitOrder));
                        Unit nextUnit = org.freemars.util.Utility.getNextPlayableUnit(player, unitToOrder);
                        freeMarsController.execute(new SetActiveUnitCommand(player, nextUnit));
                        boolean spaceshipLaunchSound = Boolean.valueOf(freeMarsController.getFreeMarsModel().getFreeMarsPreferences().getProperty("spaceship_launch_sound"));
                        if (spaceshipLaunchSound) {
                            freeMarsController.executeViewCommandImmediately(new PlaySoundCommand("sound/spaceship_launch.wav"));
                        }
                        String message = unitToOrder.getType().getName() + " is sent to Earth";
                        message = message + "\n" + requiredHydrogenQuantity + " hydrogen used";
                        FreeMarsOptionPane.showMessageDialog(freeMarsController.getCurrentFrame(), message);
                    } else {
                        FreeMarsOptionPane.showMessageDialog(freeMarsController.getCurrentFrame(), "Not enough Hydrogen. Need " + requiredHydrogenQuantity + " hydrogen to launch.");
                    }
                }
            }
        }
    }

    public boolean checkEnabled() {
        Player activePlayer = freeMarsController.getFreeMarsModel().getActivePlayer();
        if (activePlayer == null) {
            return false;
        }
        Unit unitToOrder = unit != null ? unit : freeMarsController.getFreeMarsModel().getActivePlayer().getActiveUnit();
        if (unitToOrder == null) {
            return false;
        }
        EarthFlightProperty earthFlight = (EarthFlightProperty) unitToOrder.getType().getProperty("EarthFlight");
        return earthFlight != null;
    }

}
