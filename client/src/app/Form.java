package app;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.List;

public class Form
{
    private static Form instance;
    public static Form getInstance()
    {
        if(instance==null)
            instance=new Form();
        return instance;
    }
    private JLabel dateLabel,testLabel,intensiveCareLabel,deathLabel,casesLabel,recoveredLabel,testTotalLabel,intensiveCareTotalLabel,deathTotalLabel,casesTotalLabel,recoveredTotalLabel;
    private SimpleDateFormat dateFormat;
    private List<Data> dataList;
    private Form()
    {
        dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        JFrame frame=new JFrame("Bildirim");
        frame.setBounds(100,100,600,400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dateLabel=new JLabel("Date:");
        dateLabel.setBounds(10,10,200,30);
        frame.add(dateLabel);
        testLabel=new JLabel("Test:");
        testLabel.setBounds(10,50,200,30);
        frame.add(testLabel);
        intensiveCareLabel=new JLabel("Intensive Care:");
        intensiveCareLabel.setBounds(10,90,200,30);
        frame.add(intensiveCareLabel);
        deathLabel=new JLabel("Death:");
        deathLabel.setBounds(10,130,200,30);
        frame.add(deathLabel);
        casesLabel=new JLabel("Cases:");
        casesLabel.setBounds(10,170,200,30);
        frame.add(casesLabel);
        recoveredLabel=new JLabel("Recovered:");
        recoveredLabel.setBounds(10,210,200,30);
        frame.add(recoveredLabel);
        testTotalLabel=new JLabel("Test Total:");
        testTotalLabel.setBounds(300,50,200,30);
        frame.add(testTotalLabel);
        intensiveCareTotalLabel=new JLabel("Intensive Care Total:");
        intensiveCareTotalLabel.setBounds(300,90,200,30);
        frame.add(intensiveCareTotalLabel);
        deathTotalLabel=new JLabel("Death Total:");
        deathTotalLabel.setBounds(300,130,200,30);
        frame.add(deathTotalLabel);
        casesTotalLabel=new JLabel("Cases Total:");
        casesTotalLabel.setBounds(300,170,200,30);
        frame.add(casesTotalLabel);
        recoveredTotalLabel=new JLabel("Recovered Total:");
        recoveredTotalLabel.setBounds(300,210,200,30);
        frame.add(recoveredTotalLabel);
        frame.setVisible(true);
        listen();
    }
    private void listen()
    {
        try
        {
            Socket socket=new Socket("localhost",9090);
            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            while(true)
            {
                Object o=objectInputStream.readObject();
                if(o instanceof List)
                    dataList=(List<Data>)o;
                else
                    dataList.add((Data)o);
                if(dataList.size()>0)
                {
                    Data data=dataList.get(dataList.size()-1);
                    dateLabel.setText("Date:"+dateFormat.format(data.getDate()));
                    testLabel.setText("Test:"+data.getTest());
                    intensiveCareLabel.setText("Intensive Care:"+data.getIntensiveCare());
                    deathLabel.setText("Death:"+data.getDeath());
                    casesLabel.setText("Cases:"+data.getCases());
                    recoveredLabel.setText("Recovered:"+data.getRecovered());
                    int testTotal=0, intensiveCareTotal=0, deathTotal=0, casesTotal=0, recoveredTotal=0;
                    for(Data data1: dataList)
                    {
                        testTotal+=Integer.parseInt(data1.getTest());
                        intensiveCareTotal+=Integer.parseInt(data1.getIntensiveCare());
                        deathTotal+=Integer.parseInt(data1.getDeath());
                        casesTotal+=Integer.parseInt(data1.getCases());
                        recoveredTotal+=Integer.parseInt(data1.getRecovered());
                    }
                    testTotalLabel.setText("Test Total:"+testTotal);
                    intensiveCareTotalLabel.setText("Intensive Care Total:"+intensiveCareTotal);
                    deathTotalLabel.setText("Death Total:"+deathTotal);
                    casesTotalLabel.setText("Cases Total:"+casesTotal);
                    recoveredTotalLabel.setText("Recovered Total:"+recoveredTotal);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}