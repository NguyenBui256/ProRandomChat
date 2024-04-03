package com.example.prorandomchata;

import com.example.prorandomchata.Controller.IOSystem;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerManager extends Thread {
    protected Socket socket;
    protected ObjectInputStream reader;
    protected ObjectOutputStream oos;
    public ServerManager(Socket socket) {
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
    while (true) {
        try {
            Object object = reader.readObject();
            if(object instanceof RequestFromUser)
            {
                String requestType = ((RequestFromUser) object).getType();
                String requestContent = ((RequestFromUser) object).getContent();
                if(requestType.equals("#GET"))
                {
                    if(requestContent.equals("#Profile")) {ServerSendProfile((RequestFromUser) object);}
                    else if(requestContent.equals("#Partner")) {ServerSendPartner((RequestFromUser) object);}
                }
                else if(requestType.equals("#POST"))
                {
                    if(requestContent.equals("#NewUser")) {ServerAddNewUser((RequestFromUser) object);}
                    else if(requestContent.equals("#ChangeUserFirst")){ServerChangeUserFirst((RequestFromUser) object);}
                    else if(requestContent.equals("#ChangeUserLater")){ServerChangeUserLater((RequestFromUser) object);}
                    else if(requestContent.equals("#SendMessage")){ServerSendMessage((RequestFromUser) object);}
                    else if(requestContent.equals("#Disconnect")){ServerRemove((RequestFromUser) object);}
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


    protected void ServerRemove(RequestFromUser object) throws IOException {
        ChatServer.users.remove(object.user);
        ChatServer.userSocketMap.get(object.user.getUserName()).close();
        ChatServer.userSocketMap.put(object.user.getUserName(), null);
        System.out.println("Removed " + object.user.getUserName());
    }

    protected void ServerSendMessage(RequestFromUser object) throws IOException {
        boolean authenticated = false;
        User requestedUser = object.getUser();
        for(User user : ChatServer.usersRegisted)
        {
            if(requestedUser.getUserName().equals(user.getUserName())
            && requestedUser.getUserPassword().equals(user.getUserPassword()))
            {
                authenticated = true;
                break;
            }
        }
        if(authenticated) {
//            System.out.println("User passed");
            ResponseFromServer response = new ResponseFromServer("#UPDATE", "#Message");
            response.setObject(object.getMessage());
            ChatServer.userSocketMap.get(object.partner.getUserName()).writeObject(response);
        }
    }

    protected void ServerChangeUserFirst(RequestFromUser object) throws IOException {
        User requestedUser = object.getUser();
        User requestUserPartner = object.getPartner();
        for(User user : ChatServer.users)
        {
            if(user.getUserName().equals(requestedUser.getUserName())
            || user.getUserName().equals(requestUserPartner.getUserName()))
            {
                user.status = false;
                user.blockUsers.add(user.userPartnerName);
                user.userPartnerName = "";
            }
        }
        ChatServer.userSocketMap.get(object.partner.getUserName()).writeObject(
                new ResponseFromServer("#UPDATE", "#PartnerQuit"));
        ServerSendPartner(object);
    }

    private void ServerChangeUserLater(RequestFromUser object) throws IOException {
        User requestedUser = object.getUser();
        for(User user : ChatServer.users)
        {
            if(user.getUserName().equals(requestedUser.getUserName()))
            {
                user.status = false;
                user.blockUsers.add(user.userPartnerName);
                user.userPartnerName = "";
                break;
            }
        }
        ServerSendPartner(object);
    }

    protected void ServerSendPartner(RequestFromUser object) throws IOException {
        boolean partnerIsFound = false;
        User partner = null;
        User requestedUser = object.getUser();
        for (User user : ChatServer.users) {
//            System.out.println(user.getUserName() + " " + user.status + " " + user.userPartnerName);
            if (!user.getUserName().equals(requestedUser.getUserName())
                && !requestedUser.blockUsers.contains(user.getUserName())
                && !user.blockUsers.contains(requestedUser.getUserName())
                && !user.status)
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
            ChatServer.userSocketMap.get(partner.getUserName()).writeObject(responseToPartner);
            System.out.println("Connect sent between: " + requestedUser.getUserName() + " and " + partner.getUserName());
        }
        else
        {
            ResponseFromServer responseToRequestedUser = new ResponseFromServer("#UPDATE", "#Matching");
            oos.writeObject(responseToRequestedUser);
        }
    }

    protected void ServerSendProfile(RequestFromUser object) throws IOException {
        for(User user : ChatServer.users)
        {
            if(user.getUserName().equals(object.getUser()))
            {
                ResponseFromServer responseToRequestedUser = new ResponseFromServer("#UPDATE", "#UserInfo");
                responseToRequestedUser.setObject(user);
                oos.writeObject(responseToRequestedUser);
                return;
            }
        }
    }

    protected void ServerAddNewUser(RequestFromUser object) throws IOException {
        ChatServer.users.add(object.getUser());
        System.out.println(ChatServer.users.size());
        ChatServer.userSocketMap.put(object.getUser().getUserName(), oos);
//        System.out.println("List oos:");
//        for(User user : ChatServer.users){
//            if(ChatServer.userSocketMap.get(user.getUserName()) != null)
//            {
//                System.out.println(user.getUserName() + " " + user.status);
//            }
//        }
//        System.out.println("End List oos!");
        System.out.println("Da them user");
    }
}