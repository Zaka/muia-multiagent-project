import sim.engine.*;
import sim.field.grid.*;
import sim.util.*;

public class AntSim extends SimState
{
  public SparseGrid2D world;
  public final static boolean DEBUG = false;

  private int _fGridWith = 200;
  private int _fGridHeight = 200;
  private int _fNumAnts = 100;
  private int _fMemorySize = 50;
  private double _fConstantPickUp = 0.1;
  private double _fConstantPutDown = 0.3;
  private int _fNumThings = 1500;
  private int _fNumThingTypes = 1;
  private int _fAntSteps = 1;

  public int getNumAnts()
  {
    return _fNumAnts;
  }

  public void setNumAnts(int na)
  {
    _fNumAnts = na;
  }

  public int getMemorySize()
  {
    return _fMemorySize;
  }

  public void setMemorySize(int ms)
  {
    _fMemorySize = ms;
  }

  public double getConstantPickUp()
  {
    return _fConstantPickUp;
  }

  public void setConstantPickUp(double cpu)
  {
    _fConstantPickUp = cpu;
  }

  public double getConstantPutDown()
  {
    return _fConstantPutDown;
  }

  public void setConstantPutDown(double cpd)
  {
    _fConstantPutDown = cpd;
  }

  public int getNumThings()
  {
    return _fNumThings;
  }

  public void setNumThings(int nt)
  {
    _fNumThings = nt;
  }

  public void setNumThingTypes(int ntt)
  {
    if (_fNumThingTypes > 0 || _fNumThingTypes < Thing.MAX_TYPES)
      _fNumThingTypes = ntt;
  }

  public int getNumThingTypes()
  {
    return _fNumThingTypes;
  }

  public int getAntSteps()
  {
    return _fAntSteps;
  }

  public void setAntSteps(int as)
  {
    _fAntSteps = as;
  }


  public AntSim(long seed)
  {
    super(seed);
  }

  public void start()
  {
    super.start();
    world = new SparseGrid2D(_fGridWith, _fGridHeight);

    Object object;
    for(int type=0; type<2; type++)
    {
      int count = (type==0?_fNumAnts:_fNumThings);
      for(int i=0;i<count;i++)
      {
	if (type == 0)
	{
	  object = new Ant();
	  schedule.scheduleRepeating((Steppable)object);
	}
	else
	  object = new Thing(random.nextInt(_fNumThingTypes));

	int x = random.nextInt(_fGridWith);
	int y = random.nextInt(_fGridHeight);
	while(world.getObjectsAtLocation(x,y) != null)
	{
	  x = random.nextInt(_fGridWith);
	  y = random.nextInt(_fGridHeight);
	}
	world.setObjectLocation(object,x,y);
      }
    }
  }

  public static void main(String[] args)
  {
    doLoop(AntSim.class, args);
    System.exit(0);
  }    
}
