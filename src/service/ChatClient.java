/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import view.ChatUIController;

/**
 * All rights reserved The source code is protected to its owner
 *
 * @author Abed
 */
public class ChatClient extends UnicastRemoteObject implements ChatClientInt {

    private String name;
    private ChatUIController cuic;

    public ChatClient(String n) throws RemoteException {
        name = n;
    }

    @Override
    public void tell(String st) throws RemoteException {
        System.out.println(st);
        cuic.writeMsg(st);
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    public void setCuic(ChatUIController cuic) {
        this.cuic = cuic;
    }
    

}
