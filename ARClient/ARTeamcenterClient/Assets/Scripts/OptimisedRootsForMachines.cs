using Assets.Entity;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class OptimisedRootsForMachines : MonoBehaviour {
    public GameObject RedContentPanel;
    public GameObject GreenContentPanel;
    public GameObject RedListItemPrefab;
    public GameObject GreenListItemPrefab;

    public string ServerResponseGreenRoot, ServerResponseRedRoot;
	// Use this for initialization
	void Start () {
        OptimisedListCommunication redRootCommunication = new OptimisedListCommunication();
        ServerResponseRedRoot = redRootCommunication.ReceiveRedRootDataFromServer();
        OptimisedListCommunication greenRootCommunication = new OptimisedListCommunication();
        ServerResponseGreenRoot = greenRootCommunication.ReceiveGreenRootDataFromServer();

        List<MachineEntity> greenRootMachinesList = new List<MachineEntity>();
        List<MachineEntity> redRootMachinesList = new List<MachineEntity>();

        redRootMachinesList = JsonUtility.FromJson<List<MachineEntity>>(ServerResponseRedRoot);
        greenRootMachinesList = JsonUtility.FromJson<List<MachineEntity>>(ServerResponseGreenRoot);

        foreach(MachineEntity machines in greenRootMachinesList)
        {
            GameObject newMachine = Instantiate(GreenListItemPrefab) as GameObject;
            OptimisedListItemController greenController = newMachine.GetComponent<OptimisedListItemController>();
            greenController.MachineId.text = machines.id;
            greenController.OperationToDo.text = machines.operationToDo;
            greenController.Xaxis.text = machines.xAxis;
            greenController.Yaxis.text = machines.yAxis;
            newMachine.transform.parent = GreenContentPanel.transform;
            newMachine.transform.localScale = Vector3.one;
        }

        foreach(MachineEntity machines in redRootMachinesList)
        {
            GameObject newMachine = Instantiate(RedListItemPrefab) as GameObject;
            OptimisedListItemController redController = newMachine.GetComponent<OptimisedListItemController>();
            redController.MachineId.text = machines.id;
            redController.OperationToDo.text = machines.operationToDo;
            redController.Xaxis.text = machines.xAxis;
            redController.Yaxis.text = machines.yAxis;
            newMachine.transform.parent = RedContentPanel.transform;
            newMachine.transform.localScale = Vector3.one;
        }
        


    }
	
	// Update is called once per frame
	void Update () {
		
	}

    public void BackButtonOnClick()
    {
        SceneManager.LoadScene("MainMenu");
    }
}
