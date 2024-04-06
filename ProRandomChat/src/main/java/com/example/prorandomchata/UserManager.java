package com.example.prorandomchata;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Optional;

public class UserManager extends Thread {

    protected Socket socket;
    protected ObjectInputStream reader;
    protected ObjectOutputStream oos;
    protected ChatTabController chatTabController;
    protected MainController mainController;
    protected LoginController loginController;
    protected boolean userIsConnected;

    public UserManager(Socket socket, ObjectOutputStream oos) throws IOException {
        this.userIsConnected = true;
        this.socket = socket;
        this.oos = oos;
        InputStream input = socket.getInputStream();
        this.reader = new ObjectInputStream(input);
    }

    public void run() {
    while (userIsConnected) {
        try {
                ResponseFromServer fromServer = (ResponseFromServer) reader.readObject();
                String responseType = fromServer.getType();
                String responseContent = fromServer.getContent();
                if(responseType.equals("#UPDATE"))
                {
                    if(responseContent.equals("#Matching")) {pendingForPartner(fromServer);}
                    else if(responseContent.equals("#LoginSucess")) {userLoginSucess(fromServer);}
                    else if(responseContent.equals("#LoginFail")) {userLoginFail(fromServer);}
                    else if(responseContent.equals("#SaveFail")) {userSaveFail(fromServer);}
                    else if(responseContent.equals("#SaveSuccess")) {userSaveSuccess(fromServer);}
                    else if(responseContent.equals("#SignupSuccess")) {userSignupSuccess(fromServer);}
                    else if(responseContent.equals("#SignupFail")) {userSignupFail(fromServer);}
                    else if(responseContent.equals("#Partner")) {setPartner(fromServer);}
                    else if(responseContent.equals("#Message")) {receiveMessage(fromServer);}
                    else if(responseContent.equals("#PartnerQuit")) {partnerQuit(fromServer);}
                    else if(responseContent.equals("#EndConnection")) {endConnection();}
                }
        }   catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
            break;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

    private void userSaveSuccess(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(((User) fromServer.getObject()).getUserName());
                    mainController.selfProfileController.saveSuccess((User) fromServer.getObject());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void userSaveFail(ResponseFromServer fromServer) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loginController.getMainController().getSelfProfileController().saveFail();
            }
        });
    }

    private void userSignupFail(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {loginController.signUpFail();}
        });
    }

    private void userSignupSuccess(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {loginController.signUpSuccess();}
        });
    }

    private void userLoginFail(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {loginController.loginFail();}
        });
    }

    private void userLoginSucess(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    loginController.loginSucess((User) fromServer.getObject());
                    mainController = loginController.mainController;
                    chatTabController = mainController.chatTabController;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void endConnection() {userIsConnected = false;}

    public void partnerQuit(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    try {
                        chatTabController.partnerHasLeft = true;
                        chatTabController.connectToNewUserLater();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        });
    }

    private void receiveMessage(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTabController.getTextHistory().appendText((String)fromServer.getObject());
            }
        });
    }

    private void pendingForPartner(ResponseFromServer fromServer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTabController.partnerHasLeft = true;
                chatTabController.unsetReceiver();
                chatTabController.setWaitScreen();
            }
        });
    }

    private void setPartner(ResponseFromServer response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTabController.unSetWaitScreen();
                chatTabController.setReceiver((User)response.getObject());
            }
        });
    }

    public void setChatTabController(ChatTabController chatTabController) {
        this.chatTabController = chatTabController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
        this.loginController.setOos(oos);
    }
}