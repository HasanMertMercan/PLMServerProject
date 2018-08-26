using Assets;
using Assets.Entity;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class ListController : MonoBehaviour {
    
    public GameObject ContentPanel;
    public GameObject ListItemPrefab;
    public Text InformationText;

    public string ServerResponse;
    public int size;
    public int counter = 0;

    private List<MachineEntity> YellowStateMachinesList = new List<MachineEntity>();

    // Use this for initialization
    void Start()
    {
        InformationText.text = "These are the yellow stated machines. " +
            "If you think they have emergency situation " +
            "please select the machines from the list by clicking to checkbox " +
            "Checked means Urgent, unchecked means Not Urgent!";

        //The data comes from here is a list of Machines.
        PlantQrCommunication qrCommunication = new PlantQrCommunication();
        ServerResponse = qrCommunication.ReceiveDataFromServer();

        //Deserilize the received data to MachineEntityList
        YellowStateMachinesList = JsonUtility.FromJson<List<MachineEntity>>(ServerResponse);
        Debug.Log(YellowStateMachinesList);

        // 2. Iterate through the data, 
        //	  instantiate prefab, 
        //	  set the data, 
        //	  add it to panel

        size = YellowStateMachinesList.Count;

        if(size == 0)
        {
            Debug.Log("This list is empty");
            //Send empty list to the server
            //Call Optimised List in the begining of the next scene
            SceneManager.LoadScene("OptimmisedRoot");
        }
        else
        {
            GameObject newMachine = Instantiate(ListItemPrefab) as GameObject;
            ListItemController controller = newMachine.GetComponent<ListItemController>();
            controller.MachineId.text = YellowStateMachinesList[0].id;
            controller.OperationToDo.text = YellowStateMachinesList[0].operationToDo;
            newMachine.transform.parent = ContentPanel.transform;
            newMachine.transform.localScale = Vector3.one;
        }
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void ApplyButtonOnClick(Toggle toggle)
    {
        string finilisedList;

        if (toggle.isOn.Equals(true))
        {
            YellowStateMachinesList[counter].errorState = "2";
            if (counter == size)
            {
                //Send Updated List to the server
                //Load Optimised List Scene
                // finilisedList = JsonUtility.ToJson(YellowStateMachinesList);
                finilisedList = JsonUtility.ToJson(YellowStateMachinesList);
                JsonEntity jsonEntity = new JsonEntity();
                jsonEntity.JsonFlag = "OptimisationRequest";
                jsonEntity.JsonObject = finilisedList;
                string optimisationMessage = JsonUtility.ToJson(jsonEntity);
                OptimisedListCommunication optimisedListCommunication = new OptimisedListCommunication();
                optimisedListCommunication.SendDataToServer(optimisationMessage);
                SceneManager.LoadScene("OptimisedRoot");
            }
            else
            {
                GameObject newMachine = Instantiate(ListItemPrefab) as GameObject;
                ListItemController controller = newMachine.GetComponent<ListItemController>();
                controller.MachineId.text = YellowStateMachinesList[counter + 1].id;
                controller.OperationToDo.text = YellowStateMachinesList[counter + 1].operationToDo;
                newMachine.transform.parent = ContentPanel.transform;
                newMachine.transform.localScale = Vector3.one;
                counter++;
            }
            

        }
        else
        {
            YellowStateMachinesList[counter].errorState = "0";
            if (counter == size)
            {
                //Send Updated List to the server
                //Load Optimised List Scene
                finilisedList = JsonUtility.ToJson(YellowStateMachinesList);
                JsonEntity jsonEntity = new JsonEntity();
                jsonEntity.JsonFlag = "OptimisationRequest";
                jsonEntity.JsonObject = finilisedList;
                string optimisationMessage = JsonUtility.ToJson(jsonEntity);
                OptimisedListCommunication optimisedListCommunication = new OptimisedListCommunication();
                optimisedListCommunication.SendDataToServer(optimisationMessage);
                SceneManager.LoadScene("OptimisedRoot");
            }
            else
            {
                GameObject newMachine = Instantiate(ListItemPrefab) as GameObject;
                ListItemController controller = newMachine.GetComponent<ListItemController>();
                controller.MachineId.text = YellowStateMachinesList[counter + 1].id;
                controller.OperationToDo.text = YellowStateMachinesList[counter + 1].operationToDo;
                newMachine.transform.parent = ContentPanel.transform;
                newMachine.transform.localScale = Vector3.one;
                counter++;

            }
        }

        

    }

}

    

