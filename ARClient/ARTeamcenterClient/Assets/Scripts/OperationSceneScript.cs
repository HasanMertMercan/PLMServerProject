using Assets.Entity;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class OperationSceneScript : MonoBehaviour {

    public GameObject CADImage;
    public Text InformationText;
    public Text UpdateInformationText;
    public Button NextInformationButton;
    public int counter = 0;
    public int size = 0;
    public List<string> InstructionList = new List<string>();
    private UpdateInstructionsEntity instructionsEntity = new UpdateInstructionsEntity();

    internal UpdateInstructionsEntity InstructionsEntity
    {
        get
        {
            return instructionsEntity;
        }

        set
        {
            instructionsEntity = value;
        }
    }


    // Use this for initialization
    void Start () {
        MachineQrCommunication machineQrCommunication = new MachineQrCommunication();
        string ServerResponse = machineQrCommunication.ReceiveDataFromServer();

        //Receive the CAD file and save to Resources or local of the client machine
        MachineEntity machine = JsonUtility.FromJson<MachineEntity>(ServerResponse);
        instructionsEntity.operationId = machine.operationToDo;
        instructionsEntity.operationInstructionListId = machine.operationInstructionListId;
        instructionsEntity.operationInstructionList = machine.operationInstructionList;

        //Iterprete CAD Filename 
        string CadFilenameAbsolute = machine.machineCADFile;
        string[] file = CadFilenameAbsolute.Split('/');
        int counter = file.Length;
        string CadFilename = file[counter - 1]; //Exact Filename => example.obj
        
        //string CadFilename = "gokart_main_assy.obj";
        //This filepath can be changed according to device
        string FilePath = "C:/ARClient/ARTeamcenterClient/Assets/Resources/" + CadFilename;
        byte[] FileBuffer = machineQrCommunication.ReceiveCADFileFromServer();

        // create a file in local and write to file
        BinaryWriter bWrite = new System.IO.BinaryWriter(File.Open(FilePath, FileMode.Create));
        bWrite.Write(FileBuffer);
        bWrite.Flush();
        bWrite.Close();

        //Load CAD model to the scene
        CADImage = OBJLoader.LoadOBJFile(FilePath); //I hope this will work

        
        //Display first step of the operation on the screen
        InstructionList = machine.operationInstructionList;
        size = InstructionList.Count;
        NextInformationButton.GetComponentInChildren<Text>().text = "Start";
        InformationText.text = "You will see the instructions here!" + 
                               "If you feel you need more information, click the button below!";

        //Highlight the part which will be changed
        
    }
	
	// Update is called once per frame
	void Update () {
		
	}

    public void MoreInfoButtonOnClick()
    {
        //Decrease the user ability and display more detailed information
        //if user has the most basic information, display an error text ;
        //"This is the most basic information, please contact with your supervisor for more info!"

        string remainingInstruction = InstructionList[counter];

        UpdateInstructionsEntity updateInstructions = new UpdateInstructionsEntity();
        updateInstructions.operationId = instructionsEntity.operationId;
        updateInstructions.operationInstructionListId = instructionsEntity.operationInstructionListId;
        updateInstructions.operationInstructionList = instructionsEntity.operationInstructionList;
        updateInstructions.changeListType = false;

        string UpdateListCredentials = JsonUtility.ToJson(updateInstructions);
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.JsonFlag = "UpdateInformationRequest";
        jsonEntity.JsonObject = UpdateListCredentials;
        string jsonMessage = JsonUtility.ToJson(jsonEntity);
        InstructionListUpdateCommunication instructionListUpdateCommunication = new InstructionListUpdateCommunication();
        instructionListUpdateCommunication.SendDataToServer(jsonMessage);

        InstructionListUpdateCommunication instructionListUpdateCommunicationResponse = new InstructionListUpdateCommunication();
        string ServerResponse = instructionListUpdateCommunicationResponse.ReceiveServerResponse();
        UpdateInstructionsEntity UpdatedInstructionList = JsonUtility.FromJson<UpdateInstructionsEntity>(ServerResponse);

        if (UpdatedInstructionList.changeListType.Equals(false))
        {
            UpdateInformationText.text = "This is the most basic information." + 
                " If you need more information, please contact with your supervisor!";
        }
        else
        {
            int newsize = UpdatedInstructionList.operationInstructionList.Count;
            for(int i = 0; i < newsize; i++)
            {
                if (remainingInstruction.Equals(UpdatedInstructionList.operationInstructionList[i]))
                {
                    InformationText.text = UpdatedInstructionList.operationInstructionList[i];
                    InstructionList = UpdatedInstructionList.operationInstructionList;
                    size = newsize;
                    counter = i;
                    counter++;
                    instructionsEntity.operationId = UpdatedInstructionList.operationId;
                    instructionsEntity.operationInstructionListId = UpdatedInstructionList.operationInstructionListId;
                    instructionsEntity.operationInstructionList = UpdatedInstructionList.operationInstructionList;
                    break;
                }
            }
        }


    }
    

    public void NextButtonOnClick()
    {
        //Display the next line of the information.
        //if this information is the last line, change buttonText to "Finish"
        if(counter == size)
        {
            SceneManager.LoadScene("MainMenu");
        }

        if(counter == size - 1)
        {
            InformationText.text = "Congrulations! You have completed the operation!";
            NextInformationButton.GetComponentInChildren<Text>().text = "Main Menu";
            counter++;
        }
        else
        {
            if (counter == 0)
            {
                NextInformationButton.GetComponentInChildren<Text>().text = "Next";
            }

            InformationText.text = InstructionList[counter];
            counter++;

            if (counter == size - 1)
            {
                NextInformationButton.GetComponentInChildren<Text>().text = "Finish";
            }
        }
        
    }
}
