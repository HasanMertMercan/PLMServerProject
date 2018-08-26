using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using UnityEngine;

public class MachineQrCommunication{

    private string stringData;
    private IPEndPoint ip = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 35010);

    public void SendDataToServer(string MachineQrCodeString)
    {
        Socket server = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        try
        {
            server.Connect(ip);
        }
        catch (SocketException e)
        {
            string error = "Unable to connect to server.";
            Console.WriteLine(error + e);
            //return error;
        }

        string input = MachineQrCodeString;
        server.Send(Encoding.ASCII.GetBytes(input));
        server.Close();

        //return input;
    }

    //Call this method on OperationScene
    public string ReceiveDataFromServer()
    {
        //IPEndPoint ip = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 35010);

        Socket server = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

        try
        {
            server.Connect(ip);
        }
        catch (SocketException e)
        {
            Debug.Log("Unable to connect to server.");
        }

        byte[] data = new byte[1024];
        int receivedDataLength = server.Receive(data);
        stringData = Encoding.ASCII.GetString(data, 0, receivedDataLength);
        Debug.Log(stringData);

        server.Shutdown(SocketShutdown.Both);
        server.Close();

        return stringData; //return the connection status
    }
    
    //Create a nethod which takes receives the filestream from the server

    public byte[] ReceiveCADFileFromServer()
    {
        TcpClient tcpClient = new TcpClient();
        tcpClient.Connect(ip);
        NetworkStream networkStream = tcpClient.GetStream();

        StreamReader sr = new StreamReader(networkStream);
        
        //read file length
        int length = int.Parse(sr.ReadLine());
        Console.WriteLine("File size: {0} bytes", length);

        byte[] buffer = new byte[length];
        int toRead = (int)length;
        int read = 0;
        //read bytes to buffer
        while (toRead > 0)
        {
            int noChars = networkStream.Read(buffer, read, toRead);
            read += noChars;
            length -= noChars;
        }

        return buffer;
    }
}
