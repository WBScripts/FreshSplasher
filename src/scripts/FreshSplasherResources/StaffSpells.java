package scripts.FreshSplasherResources;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;

//TODO: Make a proper Autocast API because this is ass
/**
 * @author Wastedbro
 */
public class StaffSpells
{
    public enum SPELLS
    {
        AIR_STRIKE(15),
        FIRE_STRIKE(21);

        private final int textureId;

        SPELLS(int textureId)
        {
            this.textureId = textureId;
        }
    }

    public static boolean isSpellSelected(SPELLS spell)
    {
        RSInterface spellIcon = Interfaces.get(593,25);

        if(spellIcon != null)
        {
            return spellIcon.getTextureID() == spell.textureId;
        }
        return false;
    }

    /**
     * This method is ugly as shit (will make a proper autocast API if I need to use it again)
     * @return
     */
    public static boolean equipSpell(SPELLS spell)
    {
        if(!GameTab.open(GameTab.TABS.COMBAT))
        {
            if(GameTab.open(GameTab.TABS.COMBAT))
            {
                Timing.waitCondition(new Condition()
                {
                    @Override
                    public boolean active()
                    {
                        return GameTab.open(GameTab.TABS.COMBAT);
                    }
                }, General.random(900,1900));
            }
        }

        if(GameTab.open(GameTab.TABS.COMBAT))
        {
            RSInterface spellButton = Interfaces.get(593, 25);

            if (spellButton != null)
            {
                if (spellButton.click())
                {
                    if(Timing.waitCondition(new Condition()
                    {
                        @Override
                        public boolean active()
                        {
                            return Interfaces.get(201) != null;
                        }
                    }, General.random(1000,3000)))
                    {
                        RSInterfaceMaster spells = Interfaces.get(201);

                        for(RSInterface s : spells.getComponents())
                        {
                            if(s.getTextureID() == spell.textureId)
                            {
                                return s.click();
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

}
