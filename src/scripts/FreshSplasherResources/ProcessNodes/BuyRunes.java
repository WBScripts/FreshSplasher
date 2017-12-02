package scripts.FreshSplasherResources.ProcessNodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.FreshSplasherResources.Constants;
import scripts.OsUtils.CameraUtil;
import scripts.Shopping_API.Shopper;
import scripts.Tree_Framework.ProcessNode;
import scripts.webwalker_logic.WebWalker;

/**
 * @author Wastedbro
 */
public class BuyRunes extends ProcessNode
{
    private static final RSTile shopTile = new RSTile(3014,3258,0);
    private static final RSArea shopArea = new RSArea(shopTile, 6);
    private static final String npcName = "Betty";

    private static final String shopName = "Betty's Magic Emporium.";

    @Override
    public String getStatus()
    {
        return "Acquiring Runes";
    }

    @Override
    public void execute()
    {
        if(shopArea.contains(Player.getRSPlayer().getPosition()))
        {
            if(Shopper.isShopOpen())
            {
                if(Shopper.getShopName().equals(shopName))
                {
                    if(buyRunes())
                    {
                        if(Shopper.close())
                        {
                            Timing.waitCondition(new Condition()
                            {
                                @Override
                                public boolean active()
                                {
                                    return !Shopper.isShopOpen();
                                }
                            }, General.random(900,2000));
                            General.sleep(General.randomSD(150,2000,640,65));
                        }
                    }
                }
                else
                    Shopper.close();
            }
            else
            {
                RSNPC[] npcs = NPCs.find(npcName);

                if(npcs.length > 0)
                {
                    if(CameraUtil.attemptAimCamera(npcs[0],12))
                    {
                        if (npcs[0].click("Trade " + npcName))
                        {
                            if (Timing.waitCondition(new Condition()
                            {
                                @Override
                                public boolean active()
                                {
                                    return Shopper.isShopOpen();
                                }
                            }, General.randomSD(1800, 5000, 2400, 400)))
                            {
                                General.sleep(General.randomSD(15, 2000, 684, 130));
                            }
                        }
                    }
                }
            }
        }
        else
            WebWalker.walkTo(shopTile);
    }


    private static boolean buyRunes()
    {
        int fireRunesToBuy = 0;
        int airRunesToBuy = 500;
        int mindRunesToBuy = 500;

        if(Skills.getActualLevel(Skills.SKILLS.MAGIC) >= 13)
        {
            fireRunesToBuy = 1500;
            airRunesToBuy = 1000;
        }

        while(Shopper.isShopOpen())
        {
            if(Inventory.getCount(Constants.ITEMS.FIRE_RUNE.getName()) < fireRunesToBuy)
            {
                Shopper.buy(50, Constants.ITEMS.FIRE_RUNE.getName());
            }
            else if(Inventory.getCount(Constants.ITEMS.AIR_RUNE.getName()) < airRunesToBuy)
            {
                Shopper.buy(50, Constants.ITEMS.AIR_RUNE.getName());
            }
            else if(Inventory.getCount(Constants.ITEMS.MIND_RUNE.getName()) < mindRunesToBuy)
            {
                Shopper.buy(50, Constants.ITEMS.MIND_RUNE.getName());
            }
            else
                return true;
        }


        return false;
    }

    private static boolean hasRunes(int fire, int air, int mind)
    {
        return Inventory.getCount(Constants.ITEMS.FIRE_RUNE.getName()) >= fire &&
                Inventory.getCount(Constants.ITEMS.AIR_RUNE.getName()) >= air &&
                Inventory.getCount(Constants.ITEMS.MIND_RUNE.getName()) >= mind;
    }
}
