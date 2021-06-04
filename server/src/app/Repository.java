package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository
{
    private static Repository instance;
    public static Repository getInstance()
    {
        if(instance==null)
            instance=new Repository();
        return instance;
    }
    private List<Data> data;
    private File file;
    private Repository()
    {
        try
        {
            file=new File("data.dat");
            if(!file.exists())
            {
                file.createNewFile();
                data=new ArrayList<>();
                writeToFile();
            }
            else
                data=getFromFile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void add(Data data)
    {
        this.data.add(data);
        writeToFile();
    }
    private void writeToFile()
    {
        try
        {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private List<Data> getFromFile()
    {
        try
        {
            FileInputStream fileInputStream=new FileInputStream(file);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            List<Data> data=(List<Data>)objectInputStream.readObject();
            objectInputStream.close();
            return data;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public List<Data> all()
    {
        return data;
    }
}