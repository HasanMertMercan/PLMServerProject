using Assets.Entity;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using ZXing;

public class PlantQrReader : MonoBehaviour {

    // Use this for initialization
    private WebCamTexture camTexture;
    private Rect screenRect;
    private string QrResult;
    
    void Start()
    {
        screenRect = new Rect(0, 0, Screen.width, Screen.height);
        camTexture = new WebCamTexture();
        camTexture.requestedHeight = Screen.height;
        camTexture.requestedWidth = Screen.width;
        
        if (camTexture != null)
        {
            camTexture.Play();
            //background.texture = camTexture;
        }
    }
    // Update is called once per frame
    void Update()
    {
        
    }

    public void ReaderButtonOnClick()
    {
        //OnGUI();
        // drawing the camera on screen
        
        //GUI.DrawTexture(screenRect, camTexture, ScaleMode.ScaleToFit);

        // do the reading — you might want to attempt to read less often than you draw on the screen for performance sake
        try
        {
            IBarcodeReader barcodeReader = new BarcodeReader();
            // decode the current frame
            var result = barcodeReader.Decode(camTexture.GetPixels32(), camTexture.width, camTexture.height);

            if (result != null)
            {
                Debug.Log("DECODED TEXT FROM QR: " + result.Text);
            }

            QrResult = result.Text;
            JsonEntity jsonEntity = new JsonEntity();
            jsonEntity.JsonFlag = "FactoryQR";
            jsonEntity.JsonObject = QrResult;
            string jsonString = JsonUtility.ToJson(jsonEntity);
            PlantQrCommunication qrCommunication = new PlantQrCommunication();
            qrCommunication.SendDataToServer(jsonString);

            //LoadScene which displays OptimisedRootList of Machines
            SceneManager.LoadScene("YellowStateMachines");

        }
        catch (Exception ex)
        {
            Debug.LogWarning(ex.Message);
            //QrResult = "";
        }

    }
    
    /*
    void OnGUI()
    {
        // drawing the camera on screen
        GUI.DrawTexture(screenRect, camTexture, ScaleMode.ScaleToFit);

        // do the reading — you might want to attempt to read less often than you draw on the screen for performance sake
        try
        {
            IBarcodeReader barcodeReader = new BarcodeReader();
            // decode the current frame
            var result = barcodeReader.Decode(camTexture.GetPixels32(), camTexture.width, camTexture.height);

            if (result != null)
            {
                Debug.Log("DECODED TEXT FROM QR: " + result.Text);
            }

            QrResult = result.Text;
            JsonEntity jsonEntity = new JsonEntity();
            jsonEntity.JsonFlag = "FactoryQR";
            jsonEntity.JsonObject = QrResult;
            string jsonString = JsonUtility.ToJson(jsonEntity);
            PlantQrCommunication qrCommunication = new PlantQrCommunication();
            qrCommunication.SendDataToServer(jsonString);

            //LoadScene which displays OptimisedRootList of Machines
            SceneManager.LoadScene("YellowStateMachines");

        }
        catch (Exception ex)
        {
            Debug.LogWarning(ex.Message);
            //QrResult = "";
        }

    }*/

}
