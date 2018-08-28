using Assets.Entity;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using ZXing;

public class QRCameraScript : MonoBehaviour {
    private bool camAvailable;
    private WebCamTexture backCamera;
    private Texture defaultBackground;
    private string QrResult;

    public RawImage background;
    public AspectRatioFitter fit;

    // Use this for initialization
    void Start () {
        defaultBackground = background.texture;
        WebCamDevice[] devices = WebCamTexture.devices;

        if (devices.Length == 0)
        {
            Debug.Log("No Camera Available");
            camAvailable = false;
            return;
        }

        for (int i = 0; i < devices.Length; i++)
        {
            if (!devices[i].isFrontFacing)
            {
                backCamera = new WebCamTexture(devices[i].name, Screen.width, Screen.height);
            }
        }

        if (backCamera == null)
        {
            Debug.Log("unable to find back camera");
            return;
        }

        backCamera.Play();
        background.texture = backCamera;

        camAvailable = true;

    }

    // Update is called once per frame
    void Update () {
        if (!camAvailable)
            return;

        float ratio = (float)backCamera.width / (float)backCamera.height;
        fit.aspectRatio = ratio;

        float scaleY = backCamera.videoVerticallyMirrored ? -1f : 1f;
        background.rectTransform.localScale = new Vector3(1f, scaleY, 1f);

        int orient = -backCamera.videoRotationAngle;
        background.rectTransform.localEulerAngles = new Vector3(0, 0, orient);
    }

    public void PlantButtonOnClick()
    {
        try
        {
            IBarcodeReader barcodeReader = new BarcodeReader();
            // decode the current frame
            var result = barcodeReader.Decode(backCamera.GetPixels32(), backCamera.width, backCamera.height);

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

    public void MachineButtonOnClick()
    {
        // do the reading — you might want to attempt to read less often than you draw on the screen for performance sake
        try
        {
            IBarcodeReader barcodeReader = new BarcodeReader();
            // decode the current frame
            var result = barcodeReader.Decode(backCamera.GetPixels32(), backCamera.width, backCamera.height);

            if (result != null)
            {
                Debug.Log("DECODED TEXT FROM QR: " + result.Text);
            }

            QrResult = result.Text;
            JsonEntity jsonEntity = new JsonEntity();
            jsonEntity.JsonFlag = "MachineQR";
            jsonEntity.JsonObject = QrResult;
            string jsonString = JsonUtility.ToJson(jsonEntity);
            MachineQrCommunication qrCommunication = new MachineQrCommunication();
            qrCommunication.SendDataToServer(jsonString);

            //LoadScene which displays OptimisedRootList of Machines
            SceneManager.LoadScene("OperationScene");

        }
        catch (Exception ex)
        {
            Debug.LogWarning(ex.Message);
            //QrResult = "";
        }
    }
}
