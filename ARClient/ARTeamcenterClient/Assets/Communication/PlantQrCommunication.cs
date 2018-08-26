using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using UnityEngine;

public class PlantQrCommunication
{
    private string stringData;
    private IPEndPoint ip = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 35010);

    public void SendDataToServer(string PlantQrCodeString)
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

        string input = PlantQrCodeString;
        server.Send(Encoding.ASCII.GetBytes(input));
        server.Close();

        //return input;
    }

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

}

