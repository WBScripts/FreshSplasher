package scripts.FreshSplasherResources.ProcessNodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.FreshSplasherResources.StaffSpells;
import scripts.OsUtils.CameraUtil;
import scripts.Shopping_API.Shopper;
import scripts.Tree_Framework.ProcessNode;
import scripts.webwalker_logic.WebWalker;

/**
 * @author Wastedbro
 */
public class SplashGulls extends ProcessNode
{
    private RSTile combatTile = new RSTile(3029,3236,0);
    private RSArea combatArea = new RSArea(combatTile, 10);

    private String npcName = "Seagull";

    @Override
    public String getStatus()
    {
        return "Splashing";
    }

    @Override
    public void execute()
    {
        StaffSpells.SPELLS spell = Skills.getActualLevel(Skills.SKILLS.MAGIC) >= 13 ? StaffSpells.SPELLS.FIRE_STRIKE : StaffSpells.SPELLS.AIR_STRIKE;

        if(combatArea.contains(Player.getRSPlayer()))
        {
            if(StaffSpells.isSpellSelected(spell))
            {
                if(Player.getRSPlayer().isInCombat())
                {
                    int sleepTime = General.randomSD(1000,60000,20000,5000);
                    General.println("Splashing. Sleeping for: " + sleepTime + " milliseconds");
                    General.sleep(sleepTime);
                }
                else
                {
                    RSNPC[] npcs = NPCs.find(new Filter<RSNPC>()
                    {
                        @Override
                        public boolean accept(RSNPC rsnpc)
                        {
                            return rsnpc.getName().equals(npcName) && !rsnpc.isInCombat();
                        }
                    });

                    if(npcs.length > 0)
                    {
                        if(CameraUtil.attemptAimCamera(npcs[0],12)) {
                            if (npcs[0].click("Attack " + npcName)) {
                                if (Timing.waitCondition(new Condition()
                                {
                                    @Override
                                    public boolean active()
                                    {
                                        return Player.getRSPlayer().isInCombat();
                                    }
                                }, General.randomSD(1800, 5000, 2400, 400)))
                                {
                                    General.sleep(General.randomSD(15, 2000, 544, 130));
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                if(StaffSpells.equipSpell(spell))
                {
                    Timing.waitCondition(new Condition()
                    {
                        @Override
                        public boolean active()
                        {
                            return StaffSpells.isSpellSelected(spell);
                        }
                    }, General.random(900,2500));
                    General.sleep(General.randomSD(200,5000,687,99));
                }
            }
        }
        else
            WebWalker.walkTo(combatTile);
    }
}
