import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import sim.util.Bag;
import java.util.ArrayList;

public class Ant implements Steppable
{
  private Thing _fPayload = null;
  private Memory _fMemory = new Memory(); 

  class Memory
  {
    private int _fSize = 20;
    private ArrayList<Thing> _FArray = new ArrayList<Thing>();

    public void resize(int s)
    {
      _fSize = s;
    }

    public int size()
    {
      return _fSize;
    }

    public void remember(Thing t)
    {
      _FArray.add(t);
      while (_FArray.size() > _fSize)
	_FArray.remove(0);
    }

    public double getProportion(int type)
    {
      double count = 0;
      for (Thing t:_FArray)
	if (t != null && t.getType() == type)
	  count++;
      return count/_fSize; 
    }

    public void dump()
    {
      for (Thing t:_FArray)
	if (t != null)
          System.out.print(t.getType());
        else 
	  System.out.print("N");
      System.out.println("");
    }
  }

  public Ant()
  {
  }

  public void step(SimState state)
  {
    AntSim sim = (AntSim)state;

    // in case the memory has changed size
    _fMemory.resize(sim.getMemorySize());

    // get the location
    Int2D location = sim.world.getObjectLocation(this);

    // look to see what is under us
    // (remember we will be returned as well in p!)
    Bag p = sim.world.getObjectsAtLocation(location);

    // first we update the memory
    if (p.numObjs == 1)
      _fMemory.remember(null);
    else
      for(int j=0; j<p.numObjs; j++)
	if (p.objs[j] instanceof Thing)
	{
	  _fMemory.remember((Thing)p.objs[j]);
	  break;
	}

    if (sim.DEBUG)
      _fMemory.dump();

    if (p.numObjs > 1 && _fPayload == null) 
    {
      for(int j=0; j<p.numObjs; j++)
	if (p.objs[j] instanceof Thing)
	{
	  Thing thing = (Thing)(p.objs[j]);
	  // we decide whether to pick up the newly found Thing
	  double chance = sim.random.nextDouble();
	  double prob = Math.pow(sim.getConstantPickUp()/
				 (sim.getConstantPickUp()+
				  _fMemory.getProportion(thing.getType())),2);
	  if (sim.DEBUG)
	  {
	    System.out.println("Pickup?");
	    System.out.println("Mem proportion:"+
			       _fMemory.getProportion(thing.getType()));
	    System.out.println("Calculated Prob:"+prob);
	    System.out.println("Random:"+chance);
	  }
	  if (chance < prob)
	  { 
	    if (sim.DEBUG)
	      System.out.println("PICKUP");
	    _fPayload = thing;
	    sim.world.remove(_fPayload);
	  }
	  break;
	}
    }
    else if (p.numObjs == 1 && _fPayload != null)
    {
      // we decide whether to drop the current payload
      double chance = sim.random.nextDouble();
      double prob =  Math.pow(_fMemory.getProportion(_fPayload.getType())/
			      (sim.getConstantPutDown()+
			       _fMemory.getProportion(_fPayload.getType())),2);
      if (sim.DEBUG)
      {
	System.out.println("Put down?");
	System.out.println("Mem proportion:"+
			   _fMemory.getProportion(_fPayload.getType()));
	System.out.println("Calculated Prob:"+prob);
	System.out.println("Random:"+chance);
      }
      if (chance < prob)
      {
	if (sim.DEBUG)
	  System.out.println("PUTDOWN");
	sim.world.setObjectLocation(_fPayload, location);
	_fPayload = null;
      }
    }

    // randomize my movement
    Int2D newLoc = calcRandomMove(location, sim.getAntSteps(), sim);
    sim.world.setObjectLocation(this, newLoc);
  }

  private Int2D calcRandomMove(Int2D location, int range, AntSim sim)
  {
    return calcRandomMove(location, range, false, sim);
  }

  private Int2D calcRandomMove(Int2D location, int range, 
			       boolean overlapCheck, AntSim sim)
  {
    if (!overlapCheck)
      return calcLocation(location, 
			  sim.random.nextInt((range*2)+1) - range, 
			  sim.random.nextInt((range*2)+1) - range, 
			  sim);
    else 
      for (int attempt=0; attempt < 12; attempt++)
      {
	Int2D newLoc = calcLocation(location, 
				    sim.random.nextInt((range*2)+1) - range, 
				    sim.random.nextInt((range*2)+1) - range, 
				    sim);
	if (sim.world.getObjectsAtLocation(newLoc) == null)
	  return newLoc;
      }
    return location;
  }

  private Int2D calcLocation(Int2D location, int xdir, int ydir,
			     AntSim sim)
  {
    int newx = sim.world.stx(location.x + xdir);
    int newy = sim.world.sty(location.y + ydir);
    return new Int2D(newx,newy);
  }
}

