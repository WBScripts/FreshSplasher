package scripts.FreshSplasherResources.ProcessNodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.FreshSplasherResources.Constants;
import scripts.OsUtils.CameraUtil;
import scripts.OsUtils.InventoryUtil;
import scripts.Shopping_API.Shopper;
import scripts.Tree_Framework.ProcessNode;
import scripts.webwalker_logic.WebWalker;

/**
 * @author Wastedbro
 */
public class BuyStaff extends ProcessNode
{
    private static final RSTile diangoTile = new RSTile(3082,3247,0);
    private static final RSArea diangoArea = new RSArea(diangoTile, 9);
    private static final String diangoName = "Diango";

    private static final String shopName = "Diango's Toy Store.";

    @Override
    public String getStatus()
    {
        return "Acquiring Goblin Staff";
    }

    @Override
    public void execute()
    {
        String goblinStaffName = Constants.ITEMS.GOBLIN_STAFF.getName();

        if(Inventory.find(goblinStaffName).length > 0)
        {
            if(Shopper.isShopOpen())
                Shopper.close();
            InventoryUtil.equipItems(goblinStaffName);
        }
        else
        {
            if(diangoArea.contains(Player.getRSPlayer()))
            {
                if(Shopper.isShopOpen())
                {
                    if(Shopper.getShopName().equals(shopName))
                    {
                        RSItem[] items = Shopper.get(goblinStaffName);

                        if(items.length > 0)
                        {
                            if(items[0].click("Buy 1"))
                            {
                                if(Timing.waitCondition(new Condition()
                                {
                                    @Override
                                    public boolean active()
                                    {
                                        return Inventory.find(goblinStaffName).length > 0;
                                    }
                                }, General.randomSD(1800,4000,2500,400)))
                                {
                                    General.sleep(General.randomSD(15,2000,444,130));
                                    if(Shopper.close())
                                        General.sleep(General.randomSD(15,2000,344,90));
                                }
                            }
                        }
                    }
                    else
                        Shopper.close();
                }
                else
                {
                    RSNPC[] npcs = NPCs.find(diangoName);

                    if(npcs.length > 0)
                    {
                        if(CameraUtil.attemptAimCamera(npcs[0],12)) {
                            if (npcs[0].click("Trade " + diangoName)) {
                                if (Timing.waitCondition(new Condition()
                                {
                                    @Override
                                    public boolean active()
                                    {
                                        return Shopper.isShopOpen();
                                    }
                                }, General.randomSD(1800, 5000, 2400, 400)))
                                {
                                    General.sleep(General.randomSD(15, 2000, 444, 130));
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                WebWalker.walkTo(diangoTile);
            }
        }
    }
}
