package co.edu.uniandes.contaminacionm2m.list;

/**
 * Created by henryfabianvargas on 22/05/17.
 */

public class event
{
    public final static String FLUSH="FLUSH";
    public final static String SMOKE="SMOKE";
    public final static int FLUSH_ALERT=80;
    public final static int SMOKE_ALERT=110;

    private String type;
    private String time;
    private int value;
    private boolean alert;

    public event(String type, String time,int value)
    {
        this.time=time;
        this.type=type;
        this.value=value;
        if(type.contains(FLUSH) && value>=FLUSH_ALERT)
        {
            alert=true;
        }
        else if(type.contains(SMOKE) && value>=SMOKE_ALERT)
        {
            alert=true;
        }
        else
        {
            alert=false;
        }
    }

    public String getType()
    {
        return type;
    }

    public boolean isAlert()
    {
        return alert;
    }

    public String getTime()
    {
        return time;
    }

    public String toString()
    {
        return value+"";
    }
}
