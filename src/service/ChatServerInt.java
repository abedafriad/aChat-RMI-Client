/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 *
 * @author Abed
 */
public interface ChatServerInt extends Remote {

    public boolean login(ChatClientInt a) throws RemoteException;

    public void publish(String s,String name) throws RemoteException;

    public Vector getConnected() throws RemoteException;
    
}
