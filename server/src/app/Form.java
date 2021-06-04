package app;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Form
{
    private static Form instance;
    public static Form getInstance()
    {
        if(instance==null)
            instance=new Form();
        return instance;
    }
    private List<Observer> observers;
    private JTextField dateTextBox,testTextBox,intensiveCareTextBox,deathTextBox,casesTextBox,recoveredTextBox;
    public static ServerSocket serverSocket;
    private SimpleDateFormat dateFormat;
    private Repository repository;
    private Form()
    {
        try
        {
            serverSocket=new ServerSocket(9090);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        repository=Repository.getInstance();
        dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        observers=new ArrayList<>();
        JFrame frame=new JFrame("Server");
        frame.setBounds(100,100,600,400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dateTextBox=new JTextField();
        dateTextBox.setBounds(10,10,200,30);
        frame.add(dateTextBox);
        testTextBox=new JTextField();
        testTextBox.setBounds(10,50,200,30);
        frame.add(testTextBox);
        intensiveCareTextBox=new JTextField();
        intensiveCareTextBox.setBounds(10,90,200,30);
        frame.add(intensiveCareTextBox);
        deathTextBox=new JTextField();
        deathTextBox.setBounds(10,130,200,30);
        frame.add(deathTextBox);
        casesTextBox=new JTextField();
        casesTextBox.setBounds(10,170,200,30);
        frame.add(casesTextBox);
        recoveredTextBox=new JTextField();
        recoveredTextBox.setBounds(10,210,200,30);
        frame.add(recoveredTextBox);
        JButton button=new JButton("Send");
        button.setBounds(300,10,100,30);
        button.addActionListener(this::onClick);
        frame.add(button);
        frame.setVisible(true);
        listenNewConnections();
    }
    private void listenNewConnections()
    {
        while(true)
        {
            try
            {
                Socket socket=serverSocket.accept();
                System.out.println("New Connection");
                Observer observer=new Observer(new ObjectOutputStream(socket.getOutputStream()));
                observer.update(repository.all());
                observers.add(observer);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void onClick(ActionEvent actionEvent)
    {
        Date date=null;
        try
        {
            date=dateFormat.parse(dateTextBox.getText());
        }
		catch(ParseException e)
        {
            e.printStackTrace();
        }
        Data data=new Data(date,testTextBox.getText(),intensiveCareTextBox.getText(),deathTextBox.getText(),casesTextBox.getText(),recoveredTextBox.getText());
        repository.add(data);
        update(data);
    }
    private void update(Object data)
    {
        for(Observer observer:observers)
            observer.update(data);
    }
}