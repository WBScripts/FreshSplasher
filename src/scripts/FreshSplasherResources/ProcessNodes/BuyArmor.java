package scripts.FreshSplasherResources.ProcessNodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.GrandExchange;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.FreshSplasherResources.Constants;
import scripts.GrandExchangeApi.GeAPI;
import scripts.GrandExchangeApi.OFFER_TYPE;
import scripts.OsUtils.CameraUtil;
import scripts.OsUtils.InventoryUtil;
import scripts.Tree_Framework.ProcessNode;
import scripts.webwalker_logic.WebWalker;

/**
 * @author Wastedbro
 */
public class BuyArmor extends ProcessNode
{
    private static final RSTile GeTile = new RSTile(3165,3487,0);
    private static final RSArea GeArea = new RSArea(GeTile, 9);

    private static final String[] ironItems = new String[]{ Constants.ITEMS.IRON_FULL_HELM.getName(),
            Constants.ITEMS.IRON_KITE_SHIELD.getName(),
            Constants.ITEMS.IRON_PLATEBODY.getName(),
            Constants.ITEMS.IRON_PLATELEGS.getName()};

    @Override
    public String getStatus()
    {
        return "Acquiring Armor";
    }

    @Override
    public void execute()
    {
        if(GeArea.contains(Player.getRSPlayer()))
        {
            if(Inventory.find(Constants.ITEMS.IRON_SET.getName()).length > 0)
            {
                if(GrandExchange.getWindowState() != null)
                    GrandExchange.close();

                if(isSetsExchangeOpen())
                {
                    RSItem[] items = Inventory.find(Constants.ITEMS.IRON_SET.getName());

                    if(items.length > 0)
                    {
                        if(items[0].click("Unpack"))
                        {
                            if(Timing.waitCondition(new Condition()
                            {
                                @Override
                                public boolean active()
                                {
                                    return Inventory.find(Constants.ITEMS.IRON_SET.getName()).length == 0;
                                }
                            }, General.randomSD(1800,4000,2500,400)))
                            {
                                General.sleep(General.randomSD(15,2000,444,130));
                                if(closeSetsExchange())
                                    General.sleep(General.randomSD(15,2000,344,90));
                            }
                        }
                    }
                }
                else
                {
                    interactNpc("Grand Exchange Clerk", "Sets", new Condition()
                    {
                        @Override
                        public boolean active()
                        {
                            return isSetsExchangeOpen();
                        }
                    });
                }
            }
            else if(Inventory.find(ironItems).length > 0)
            {
                if(GrandExchange.getWindowState() != null)
                    GrandExchange.close();

                InventoryUtil.equipItems(ironItems);
            }
            else
            {
                GrandExchange.WINDOW_STATE windowState = GrandExchange.getWindowState();

                if(windowState == GrandExchange.WINDOW_STATE.SELECTION_WINDOW)
                {
                    RSGEOffer offer = getIronSetOffer();
                    if(offer != null)
                    {
                        if(offer.getStatus() == RSGEOffer.STATUS.COMPLETED)
                        {
                            GeAPI.collectAllItems();
                            General.sleep(General.randomSD(400,5000,1634,400));
                            GrandExchange.close();
                        }
                        else
                            General.sleep(General.randomSD(20,10000,3453,800));
                    }
                    else
                    {
                        GeAPI.offerItem(Constants.ITEMS.IRON_SET.getName(),5000,1, OFFER_TYPE.BUYING);
                    }
                }
                else if(windowState == GrandExchange.WINDOW_STATE.OFFER_WINDOW || windowState == GrandExchange.WINDOW_STATE.NEW_OFFER_WINDOW)
                {
                    GrandExchange.close();
                }
                else
                {
                    interactNpc("Grand Exchange Clerk", "Exchange", new Condition()
                    {
                        @Override
                        public boolean active()
                        {
                            return GrandExchange.getWindowState() == GrandExchange.WINDOW_STATE.SELECTION_WINDOW;
                        }
                    });
                }
            }
        }
        else
        {
            WebWalker.walkTo(GeTile);
        }
    }


    private static RSGEOffer getIronSetOffer()
    {
        RSGEOffer[] offers = GrandExchange.getOffers();

        for(RSGEOffer offer : offers)
        {
            if(offer.getStatus() != RSGEOffer.STATUS.EMPTY && offer.getItemName().equals(Constants.ITEMS.IRON_SET.getName()))
                return offer;
        }

        return null;
    }

    private static boolean isSetsExchangeOpen()
    {
        RSInterfaceChild frame = Interfaces.get(451, 1);
        if (frame != null) {
            return frame.getChild(1).getText().equals("Item Sets");
        }
        return false;
    }

    private static boolean closeSetsExchange()
    {
        RSInterfaceChild frame = Interfaces.get(451, 1);
        if (frame != null) {
            RSInterfaceComponent closeButton = frame.getChild(11);
            return closeButton != null && closeButton.click("Close") && Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    return !isSetsExchangeOpen();
                }
            }, 2000);
        }
        return false;
    }

    private static void interactNpc(String name, String option, Condition condition)
    {
        RSNPC[] npcs = NPCs.find(name);

        if(npcs.length > 0)
        {
            if(CameraUtil.attemptAimCamera(npcs[0], 12))
            {
                if (npcs[0].click(option + " " + name)) {
                    if (Timing.waitCondition(condition, General.randomSD(1800, 5000, 2400, 400)))
                    {
                        General.sleep(General.randomSD(15, 2000, 444, 130));
                    }
                }
            }
        }
    }
}
