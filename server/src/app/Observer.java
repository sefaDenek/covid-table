package app;

import java.io.ObjectOutputStream;

public class Observer
{
    private ObjectOutputStream objectOutputStream;
    public Observer(ObjectOutputStream objectOutputStream)
    {
        this.objectOutputStream=objectOutputStream;
    }
    public void update(Object data)
    {
        try
        {
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}