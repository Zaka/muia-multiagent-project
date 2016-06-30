import sim.engine.SimState;
import sim.display.Controller;
import sim.display.GUIState;
import sim.display.Display2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.util.gui.SimpleColorMap;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;

public class AntSimWithUI extends GUIState
{
  public Display2D display;
  public JFrame displayFrame;

  SparseGridPortrayal2D worldPortrayal = new SparseGridPortrayal2D();

  public AntSimWithUI() 
  { 
    super(new AntSim(System.currentTimeMillis())); 
  }

  public AntSimWithUI(SimState state) 
  { 
    super(state); 
  }

  public Object getSimulationInspectedObject() 
  { 
    return state; 
  }

  public void start()
  {
    super.start();
    setupPortrayals();
  }

  public void load(SimState state)
  {
    super.load(state);
    setupPortrayals();
  }

  public void setupPortrayals()
  {
    final SimpleColorMap map = new SimpleColorMap(new Color[] {
						    Color.cyan,
						    Color.yellow,
						    Color.magenta,
						    Color.pink,
						    Color.orange},
						  5.0,Thing.MAX_TYPES,
						  Color.blue,
						  Color.red);
    // tell the portrayals what to
    // portray and how to portray them
    worldPortrayal.setField(((AntSim)state).world);
    worldPortrayal.setPortrayalForClass(Ant.class, 
					 new sim.portrayal.simple.OvalPortrayal2D(Color.green));

    worldPortrayal.setPortrayalForClass(Thing.class,
	 new RectanglePortrayal2D()
	 {
	   public void draw(Object object, Graphics2D graphics,
			  DrawInfo2D info)
	   {
	     paint = map.getColor(((Thing)object).getType());
	     super.draw(object,graphics,info);
	   }
	 }
      );

    // reschedule the displayer
    display.reset();
    // redraw the display
    display.repaint();
  }

  public void init(Controller c)
  {
    super.init(c);

    display = new Display2D(600,600,this,1);
    displayFrame = display.createFrame();
    c.registerFrame(displayFrame);
    displayFrame.setVisible(true);

    display.setBackdrop(Color.white);

    // attach the portrayals
    display.attach(worldPortrayal,"World");
  }

  public static void main(String[] args)
  {
    new AntSimWithUI().createController();
  }
}
