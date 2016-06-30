import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import sim.util.Bag;
import java.util.ArrayList;

public class Ant implements Steppable
{
    public Ant()
    {
    }

    public void step(SimState state)
    {
        AntSim sim = (AntSim)state;

        // get the location
        Int2D location = sim.world.getObjectLocation(this);

        Int2D newLoc = calcLocation(location, location.x + 1, location.y + 1, sim);
        sim.world.setObjectLocation(this, newLoc);
    } //step

    private Int2D calcLocation(Int2D location, int xdir, int ydir,
                               AntSim sim)
    {
        int newx = sim.world.stx(location.x + xdir);
        int newy = sim.world.sty(location.y + ydir);
        return new Int2D(newx,newy);
    }
}

