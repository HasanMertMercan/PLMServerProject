using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CameraController : MonoBehaviour {

    private bool camAvailable;
    private WebCamTexture backCamera;
    private Texture defaultBackground;
    
    public RawImage background;
    public AspectRatioFitter fit;


    // Use this for initialization
    void Start()
    {
        defaultBackground = background.texture;
        WebCamDevice[] devices = WebCamTexture.devices;

        if(devices.Length == 0)
        {
            Debug.Log("No Camera Available");
            camAvailable = false;
            return;
        }

        for(int i = 0; i < devices.Length; i++)
        {
            if (!devices[i].isFrontFacing)
            {
                backCamera = new WebCamTexture(devices[i].name, Screen.width, Screen.height);
            }
        }

        if(backCamera == null)
        {
            Debug.Log("unable to find back camera");
            return;
        }

        backCamera.Play();
        background.texture = backCamera;

        camAvailable = true;


        /*
        Debug.Log("Script has been started");

        mCamera = new WebCamTexture();
        mCamera.Play();
        RawImage.GetComponent<Renderer>().material.mainTexture = mCamera;
        //RawImage.AddComponent<Renderer>().material.mainTexture = mCamera;
        //panel. = mCamera;*/

    }

    // Update is called once per frame
    void Update()
    {
        if (!camAvailable)
            return;

        float ratio = (float)backCamera.width / (float)backCamera.height;
        fit.aspectRatio = ratio;

        float scaleY = backCamera.videoVerticallyMirrored ? -1f : 1f;
        background.rectTransform.localScale = new Vector3(1f, scaleY, 1f);

        int orient = -backCamera.videoRotationAngle;
        background.rectTransform.localEulerAngles = new Vector3(0, 0, orient);
    }
}
