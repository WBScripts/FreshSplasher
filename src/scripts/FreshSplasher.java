package scripts;

import org.tribot.api.General;
import org.tribot.api2007.Equipment;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import scripts.FreshSplasherResources.Constants;
import scripts.FreshSplasherResources.DecisionNodes.HasRunes;
import scripts.FreshSplasherResources.ProcessNodes.BuyArmor;
import scripts.FreshSplasherResources.ProcessNodes.BuyRunes;
import scripts.FreshSplasherResources.ProcessNodes.BuyStaff;
import scripts.FreshSplasherResources.ProcessNodes.SplashGulls;
import scripts.FreshSplasherResources.StaffSpells;
import scripts.Tree_Framework.*;

import java.awt.*;

/**
 * @author Wastedbro
 */
@ScriptManifest(name = "FreshSplasher", authors = "Wastedbro", category = "Combat", version = 1.0, description = "Buys gear and splashes on low level creatures. Only requirement is GP")
public class FreshSplasher extends Script implements Painting
{
    @Override
    public void run()
    {
        DecisionNode hasRunesNode = new HasRunes();

        ProcessNode buyStaffNode = new BuyStaff(),
                    buyArmorNode = new BuyArmor(),
                    buyRunesNode = new BuyRunes(),
                    splashNode = new SplashGulls();


        hasRunesNode.addOnFalseNode(buyRunesNode);
        hasRunesNode.addOnTrueNode(splashNode);

        hasIronSetEquipped.addOnFalseNode(buyArmorNode);
        hasIronSetEquipped.addOnTrueNode(hasRunesNode);

        hasGoblinStaffEquipped.addOnFalseNode(buyStaffNode);
        hasGoblinStaffEquipped.addOnTrueNode(hasIronSetEquipped);

        DecisionTree tree = new DecisionTree(hasGoblinStaffEquipped);


        String lastStatus = "";
        while(true)
        {
            INode node = tree.getValidNode();

            if(node != null)
            {
                String status = node.getStatus();
                if(!status.equals(lastStatus))
                    General.println(status);
                lastStatus = status;

                node.execute();
            }

            General.sleep(1,10);
        }
    }

    @Override
    public void onPaint(Graphics graphics)
    {

    }

    private GenericDecisionNode hasGoblinStaffEquipped = new GenericDecisionNode(() -> Equipment.isEquipped(Constants.ITEMS.GOBLIN_STAFF.getName()));

    private GenericDecisionNode hasIronSetEquipped = new GenericDecisionNode(() -> Equipment.isEquipped(Constants.ITEMS.IRON_FULL_HELM.getName(),
            Constants.ITEMS.IRON_PLATEBODY.getName(),
            Constants.ITEMS.IRON_PLATELEGS.getName(),
            Constants.ITEMS.IRON_KITE_SHIELD.getName()));

}
