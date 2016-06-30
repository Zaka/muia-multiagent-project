public class Thing
{
  private int type;
  public final static int MAX_TYPES = 10;

  public Thing(int t)
  {
    type = t;
  }

  public int getType()
  {
    return type;
  }

  /*
  public boolean equals(Object o)
  {
    if (o == null || o.getClass() != getClass())
      return false;
    else
      return type == ((Thing)o).type;
  }
  */
}
