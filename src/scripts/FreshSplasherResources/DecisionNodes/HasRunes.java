package scripts.FreshSplasherResources.DecisionNodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.FreshSplasherResources.Constants;
import scripts.Tree_Framework.DecisionNode;

/**
 * @author Wastedbro
 */
public class HasRunes extends DecisionNode
{
    private static final RSTile shopTile = new RSTile(3014,3258,0);
    private static final RSArea shopArea = new RSArea(shopTile, 6);

    @Override
    public boolean isValid()
    {
        boolean isHighMage = Skills.getActualLevel(Skills.SKILLS.MAGIC) >= 13;

        if(shopArea.contains(Player.getRSPlayer()))
        {
            if(isHighMage)
            {
                return Inventory.getCount(Constants.ITEMS.AIR_RUNE.getName()) >= 1000 &&
                        Inventory.getCount(Constants.ITEMS.MIND_RUNE.getName()) >= 500 &&
                        Inventory.getCount(Constants.ITEMS.FIRE_RUNE.getName()) >= 1500;
            }
            else
            {
                return Inventory.getCount(Constants.ITEMS.AIR_RUNE.getName()) >= 500 && Inventory.getCount(Constants.ITEMS.MIND_RUNE.getName()) >= 500;
            }
        }
        else
        {
            if(isHighMage)
            {
                return Inventory.getCount(Constants.ITEMS.AIR_RUNE.getName()) >= 2 &&
                        Inventory.getCount(Constants.ITEMS.MIND_RUNE.getName()) >= 1 &&
                        Inventory.getCount(Constants.ITEMS.FIRE_RUNE.getName()) >= 3;
            }
            else
            {
                return Inventory.getCount(Constants.ITEMS.AIR_RUNE.getName()) >= 1 && Inventory.getCount(Constants.ITEMS.MIND_RUNE.getName()) >= 1;
            }
        }
    }


}
