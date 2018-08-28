using Assets.Entity;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class MainMenuScript : MonoBehaviour {
    
    public Text txtErrorMessage;
	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void QRReaderButtonOnClick()
    {
        SceneManager.LoadScene("QRReader");
    }

    public void ListButtonOnClick()
    {
        if(SceneManager.GetSceneByName("OptimisedRoot").isLoaded.Equals(true))
        {
            SceneManager.LoadScene("OptimisedRoot");
        }
        else
        {
            txtErrorMessage.text = "There is No Root to Display";
        }
    }
}
