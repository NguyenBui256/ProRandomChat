package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerManager extends Thread {
    protected Socket socket;
    protected ObjectInputStream reader;
    protected ObjectOutputStream oos;
    protected boolean userIsConnected;
    public ServerManager(Socket socket) {
        userIsConnected = true;
        this.socket = socket;
        try {
            reader = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
    while (userIsConnected) {
        try {
            Object object = reader.readObject();
            if(object instanceof RequestFromUser)
            {
                String requestType = ((RequestFromUser) object).getType();
                String requestContent = ((RequestFromUser) object).getContent();
                if(requestType.equals("#GET"))
                {
                    if(requestContent.equals("#Profile")) {ServerSendProfile((RequestFromUser) object);}
                    else if(requestContent.equals("#NewPartner")) {ServerSendPartner((RequestFromUser) object);}
                }
                else if(requestType.equals("#POST"))
                {
//                    if(requestContent.equals("#NewUser")) {ServerAddNewUser((RequestFromUser) object);}
                    if(requestContent.equals("#Login")) {ServerAddLogin((RequestFromUser) object);}
                    else if(requestContent.equals("#Logout")) {ServerLogout((RequestFromUser) object);}
                    else if(requestContent.equals("#KillApp")) {ServerKill((RequestFromUser) object);}
                    else if(requestContent.equals("#Signup")) {ServerAddSignup((RequestFromUser) object);}
                    else if(requestContent.equals("#SendMessage")){ServerSendMessage((RequestFromUser) object);}
                    else if(requestContent.equals("#Disconnect")){ServerRemove((RequestFromUser) object);}

                }
                else if(requestType.equals("#PUT"))
                {   if(requestContent.equals("#ChangeUserFirst")){ServerChangeUserFirst((RequestFromUser) object);}
                    else if(requestContent.equals("#ChangeUserLater")){ServerChangeUserLater((RequestFromUser) object);}
                    else if(requestContent.equals("#UserOnline")){ServerChangeUserOnline((RequestFromUser) object);}
                    else if(requestContent.equals("#SaveProfile")) {ServerUpdateUserInfo((RequestFromUser) object);}
                    else if(requestContent.equals("#ChangeGenderCriteria")) {ServerUpdateGenderCriteria((RequestFromUser) object);}
                }

            }
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
            break;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

    private void ServerChangeUserOnline(RequestFromUser object) {
        ChatServer.userMap.get(object.user.getUserName()).status = false;
    }

    private void ServerKill(RequestFromUser object) {
        if(object.user != null)
        {
            userIsConnected = false;
            ChatServer.users.remove(object.user.getUserName());
            ChatServer.userOutStreamMap.remove(object.user.getUserName());
            ChatServer.userMap.remove(object.user.getUserName());
        }
        this.interrupt();
    }

    private void ServerLogout(RequestFromUser object) {
        ChatServer.users.remove(object.user.getUserName());
        ChatServer.userMap.remove(object.user.getUserName());
        System.out.println(object.user.getUserName() + " logout!");
    }

    private void ServerAddSignup(RequestFromUser object) throws IOException {
        boolean userIsAlreadyUsed = false;
        User newUser = object.user;
        for (User user : ChatServer.usersRegisted) {
            if (user.getUserName().equals(newUser.getUserName())) {
                userIsAlreadyUsed = true;
                break;
            }
        }
        if (!userIsAlreadyUsed) {
            ResponseFromServer response = new ResponseFromServer("#UPDATE", "#SignupSuccess");
            oos.writeObject(response);
            IOSystem ios = new IOSystem();
            ChatServer.usersRegisted.add(newUser);
            ios.write(ChatServer.usersRegisted, "userlist.txt");
        } else {
            ResponseFromServer response = new ResponseFromServer("#UPDATE", "#SignupFail");
            oos.writeObject(response);
        }
    }

    private void ServerAddLogin(RequestFromUser object) throws IOException {
        boolean userIsFound = false;
        User verifyingUser = object.user;
        User userClient = null;
        for(User user : ChatServer.usersRegisted)
        {
            if(user.getUserName().equals(verifyingUser.getUserName()) && user.getUserPassword().equals(verifyingUser.getUserPassword()))
            {
                userIsFound = true;
                userClient = user;
                break;
            }
        }
        if(userIsFound) {
            ResponseFromServer response = new ResponseFromServer("#UPDATE", "#LoginSucess");
            userClient.status = true;
            ChatServer.users.add(userClient.getUserName());
            ChatServer.userMap.put(userClient.getUserName(), userClient);
            ChatServer.userOutStreamMap.put(userClient.getUserName(), oos);
            response.setObject(userClient);
            oos.writeObject(response);
            oos.flush();
        } else {
            ResponseFromServer response = new ResponseFromServer("#UPDATE", "#LoginFail");
            oos.writeObject(response);
            oos.flush();
        }
    }

    private void ServerUpdateGenderCriteria(RequestFromUser object) {
        ChatServer.userMap.get(object.user.getUserName()).setGenderCriteria(object.getMessage());
        System.out.println(object.user.getUserName() + " GDC Updated to " + ChatServer.userMap.get(object.user.getUserName()).genderCriteria);
    }

    private void ServerUpdateUserInfo(RequestFromUser object) throws IOException, ClassNotFoundException {
        boolean nameInUsed = false;
        if(!object.user.getUserName().equals(object.getMessage()))
        {
            for(User userI : ChatServer.usersRegisted)
            {
                if(userI.getUserName().equals(object.user.getUserName()))
                {
                    nameInUsed = true;
                    break;
                }
            }
        }
        if(nameInUsed)
        {
            ResponseFromServer responseFromServer = new ResponseFromServer("#UPDATE", "#SaveFail");
            oos.writeObject(responseFromServer);
        } else {
            User update = object.user;
            for(User userI : ChatServer.usersRegisted)
            {
                if(userI.getUserName().equals(object.getMessage()))
                {
                    userI.setGenderCriteria(update.getGenderCriteria());
                    userI.setUserName(update.getUserName());
                    userI.setUserFullname(update.getUserFullname());
                    userI.setUserDescription(update.getUserDescription());
                    userI.setUserAvatarPath(update.getUserAvatarPath());
                    userI.setUserLocation(update.getUserLocation());
                    break;
                }
            }
            ChatServer.users.remove(object.getMessage());
            ChatServer.users.add(update.getUserName());
            ChatServer.userMap.remove(object.getMessage());
            ChatServer.userMap.put(update.getUserName(), update);
            ChatServer.userOutStreamMap.remove(object.getMessage());
            ChatServer.userOutStreamMap.put(update.getUserName(), oos);

            ResponseFromServer responseFromServer = new ResponseFromServer("#UPDATE", "#SaveSuccess");
            responseFromServer.setObject(update);
            System.out.println(((User) responseFromServer.getObject()).getUserName());
            oos.writeObject(responseFromServer);
        }
        IOSystem ios = new IOSystem();
        ios.write(ChatServer.usersRegisted, "userlist.txt");
        ChatServer.usersRegisted = ios.read("userlist.txt");
    }


    protected void ServerRemove(RequestFromUser object) throws IOException {
        ChatServer.userOutStreamMap.get(object.partner.getUserName()).writeObject(
                new ResponseFromServer("#UPDATE", "#PartnerQuit"));
        ChatServer.userMap.get(object.user.getUserName()).status = true; //busy - not available
        System.out.println("Removed " + object.user.getUserName());
    }

    protected void ServerSendMessage(RequestFromUser object) throws IOException {
        boolean authenticated = false;
        if(object.user.getUserName().equals(ChatServer.userMap.get(object.user.getUserName()).getUserName())
        && object.user.getUserPassword().equals(ChatServer.userMap.get(object.user.getUserName()).getUserPassword()))
        {authenticated = true;}
        if(authenticated) {
//          System.out.println("User passed");
            ResponseFromServer response = new ResponseFromServer("#UPDATE", "#Message");
            response.setObject(object.getMessage());
            ChatServer.userOutStreamMap.get(object.partner.getUserName()).writeObject(response);
        }
    }

    protected void ServerChangeUserFirst(RequestFromUser object) throws IOException {
        ChatServer.userMap.get(object.user.getUserName()).status = false;
        ChatServer.userMap.get(object.user.getUserName()).blockUsers.add(object.partner.getUserName());
        ChatServer.userMap.get(object.user.getUserName()).userPartnerName = "";

        ChatServer.userOutStreamMap.get(object.partner.getUserName()).writeObject(
                new ResponseFromServer("#UPDATE", "#PartnerQuit"));
        ServerSendPartner(object);
    }

    private void ServerChangeUserLater(RequestFromUser object) throws IOException {
        ChatServer.userMap.get(object.user.getUserName()).status = false;
        ChatServer.userMap.get(object.user.getUserName()).blockUsers.add(object.user.userPartnerName);
        ChatServer.userMap.get(object.user.getUserName()).userPartnerName = "";
        ServerSendPartner(object);
    }

    protected void ServerSendPartner(RequestFromUser object) throws IOException {
        boolean partnerIsFound = false;
        User partner = null;
        User requestedUser = ChatServer.userMap.get(object.user.getUserName());
        for (String userName : ChatServer.users) {
            User user = ChatServer.userMap.get(userName);
//            System.out.println(user.getUserName() + " " + user.status + " " + user.userPartnerName);
//            System.out.println(user.getUserName() + " " + user.status + " " + user.userPartnerName);
            if (!user.status
                && !userName.equals(requestedUser.getUserName())
                && isSuitable(requestedUser, user)
                && !requestedUser.blockUsers.contains(userName)
                && !user.blockUsers.contains(requestedUser.getUserName()))
            {
                partnerIsFound = true;
                partner = user;
                user.status = true;
                user.userPartnerName = requestedUser.getUserName();
                requestedUser.status = true;
                requestedUser.userPartnerName = partner.getUserName();
                break;
            }
        }
        if(partnerIsFound)
        {
            ResponseFromServer responseToRequestedUser = new ResponseFromServer("#UPDATE", "#Partner");
            responseToRequestedUser.setObject(partner);
            oos.writeObject(responseToRequestedUser);

            ResponseFromServer responseToPartner = new ResponseFromServer("#UPDATE", "#Partner");
            responseToPartner.setObject(requestedUser);
            ChatServer.userOutStreamMap.get(partner.getUserName()).writeObject(responseToPartner);
            System.out.println("Connect sent between: " + requestedUser.getUserName() + " and " + partner.getUserName());
        }
        else
        {
            ResponseFromServer responseToRequestedUser = new ResponseFromServer("#UPDATE", "#Matching");
            oos.writeObject(responseToRequestedUser);
        }
    }

    protected boolean isSuitable(User requestedUser, User user) {
        if(requestedUser.genderCriteria.equals("All") && user.genderCriteria.equals("All")) return true;
        else {
            if((user.getUserGender().equals(requestedUser.genderCriteria) && user.genderCriteria.equals("All"))
            || (requestedUser.getUserGender().equals(user.genderCriteria) || requestedUser.genderCriteria.equals("All")))
            {return true;}
        }
        return false;
    }

    protected void ServerSendProfile(RequestFromUser object) throws IOException {
        ResponseFromServer responseToRequestedUser = new ResponseFromServer("#UPDATE", "#UserInfo");
        responseToRequestedUser.setObject(ChatServer.userMap.get(object.getUser().getUserName()));
        oos.writeObject(responseToRequestedUser);
    }

    protected void ServerAddNewUser(RequestFromUser object) throws IOException {
        ChatServer.users.add(object.user.getUserName());
        System.out.println(ChatServer.users.size());
        ChatServer.userMap.put(object.getUser().getUserName(), object.getUser());
        ChatServer.userOutStreamMap.put(object.getUser().getUserName(), oos);
        System.out.println("Da them user");
    }
}