using Assets.Entity;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;
using UnityEngine.SceneManagement;

public class Login : MonoBehaviour {
    
    public InputField txt_password;
    public InputField txt_username;
    public Text txtMessage;
    private string Username;
    private string Password;
    private string ServerResponse;
    

    // Use this for initialization
    void Start()
    {
        txt_password.inputType = InputField.InputType.Password;
    }

    // Update is called once per frame
    void Update()
    {

    }


    public void btnLogin_OnClick()
    {
        //These will be used in order to validate the text.
        Username = txt_username.text;
        Password = txt_password.text;
        txtMessage.text = "";

        if ((Username.Contains("/") || Username.Contains("'") || Username.Contains(" ") || Username.Contains("$"))
            || Password.Contains("/") || Password.Contains("'") || Password.Contains(" ") || Password.Contains("$"))
        {
            txtMessage.text = "Invalid Username or Password";
        }
        else
        {
            
            if (!string.IsNullOrEmpty(txt_password.text) && !string.IsNullOrEmpty(txt_username.text))
            {
                LoginArguments loginArguments = new LoginArguments
                {
                    UserId = Username,
                    Password = Password
                };

                Debug.Log(loginArguments.UserId);
                Debug.Log(loginArguments.Password);

                //translate to Json
                string json = JsonUtility.ToJson(loginArguments);
                Debug.Log(json);

                //Add Json Flag
                JsonEntity jsonEntity = new JsonEntity();
                jsonEntity.JsonFlag = "LoginRequest";
                jsonEntity.JsonObject = json;
                string flaggedJson = JsonUtility.ToJson(jsonEntity);
                Debug.Log(jsonEntity);

                //Establish the connection
                //Send loginArguments to server.
                LoginCommunication serverCommunication = new LoginCommunication();
                string Log = serverCommunication.SendDataToServer(flaggedJson);
                Debug.Log(Log);
                txtMessage.text = Log;

                LoginCommunication serverCommunication2 = new LoginCommunication();
                ServerResponse = serverCommunication2.ReceiveDataFromServer();
                int a = Convert.ToInt32(ServerResponse);

                if (a == 1)
                {
                    txtMessage.text = "Login Succesful";
                    SceneManager.LoadScene("MainMenu");
                    //Load Main Scene
                }
                else if(a == 0)
                {
                    txtMessage.text = "Unable to create the teamcenter session";
                    //Stay in the Login Scene
                }
                else
                {
                    Debug.Log("There is a connection Error");
                }

                

                /*if (txt_username.text.Equals("e1") && txt_password.text.Equals("123hm123"))
                {
                    Debug.Log("Login Successful.");
                    txtMessage.text = "Login" + "Successful";
                    //redirect to the other scene
                }
                else
                {
                    Debug.Log("Error: Username and password do not match.");
                    txtMessage.text = "Error: Username and password do not match.";
                }*/
            }
            else
            {
                Debug.Log("Error: Username and password can not be empty.");
                txtMessage.text = "Error: Username and password can not be empty.";
            }

            /*
            LoginArguments loginArguments = new LoginArguments();
            loginArguments.UserId = Username;
            loginArguments.Password = Password;
            
            //translate to Json
            string json = JsonUtility.ToJson(loginArguments);

            //Establish the connection
            //Send loginArguments to server.
            ServerCommunication serverCommunication = new ServerCommunication();
            ServerResponse = serverCommunication.SendDataToServer(json);

            //If serverResponse == true, show main page

            //TODO


            //If success, display main page
            SceneManager.LoadScene("MainMenu");*/
        }
        
    }
}
