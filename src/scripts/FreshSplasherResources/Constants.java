package scripts.FreshSplasherResources;

import org.tribot.api2007.types.RSItemDefinition;

import java.util.HashMap;

/**
 * @author Wastedbro
 */
public class Constants
{
    public static final int numberOfCasts = 500;


    public enum ITEMS
    {
        GOBLIN_STAFF("Cursed goblin staff"),
        MIND_RUNE("Mind rune"),
        AIR_RUNE("Air rune"),
        FIRE_RUNE("Fire rune"),
        IRON_SET("Iron set (lg)"),
        IRON_FULL_HELM("Iron full helm"),
        IRON_PLATEBODY("Iron platebody"),
        IRON_PLATELEGS("Iron platelegs"),
        IRON_KITE_SHIELD("Iron kiteshield");


        private String itemName;

        ITEMS(String itemName)
        {
            this.itemName = itemName;
        }

        public String getName()
        {
            return this.itemName;
        }
    }
}
