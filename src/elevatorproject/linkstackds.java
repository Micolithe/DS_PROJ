/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elevatorproject;

/**
 *
 * @author Alexander Kronish
 */
public class linkstackds {
    int maxsize;
    int avail;
    int top;
    NodeElement[] element;
    
    public linkstackds(int n){
        maxsize = n;
        avail = maxsize;
        element = new NodeElement[n];
        top = maxsize - 1;
        for(int i = 0; i<n; i++){
            element[i] = new NodeElement();
            element[i].setInfo(-1);
            element[i].setNodeNumber(i);
            element[i].setFlink(i-1);
            element[i].setBlink(-1);
            element[i].setLChild(-1);
            element[i].setRChild(-1);
            element[i].setParent(-1);
        }
    }
    
    public int getNode(){
        if(isEmpty()){
            return -1;
        }
        avail --;
        element[top].setInfo(-1);
        element[top].setRiders(0);
        int rc = top;
        top = element[top].getFlink();
        return rc;
    }
    

    
    public boolean putNode(int x){
        if(isFull()){
            return false;
        }
        element[x].setFlink(top);
        element[x].setInfo(-1);
        element[x].setRiders(0);
        element[x].setBlink(-1);
        element[x].setLChild(-1);
        element[x].setRChild(-1);
        element[x].setParent(-1);
        avail++;
        top = x;
        return true;
    }
    
        boolean isEmpty(){
        if (avail == 0){
            return true;
        }
        else{
            return false;
        }
    }
    boolean isFull(){
        if (avail == maxsize){
            return true;
        }
        return false;
    }
    
    boolean setInfo(int loc, int val){
        element[loc].setInfo(val);
        return true;
    }
    
    int getInfo(int loc){
        return element[loc].getInfo();
    }
    
    boolean setParent(int loc, int p){
        element[loc].setParent(p);
        return true;
    }
    
    boolean setLChild(int loc, int lch){
        element[loc].setLChild(lch);
        return true;
    }
    
    boolean setRChild(int loc, int rch){
        element[loc].setRChild(rch);
        return true;
    }
    
    int getRChild(int loc){
        return element[loc].getRChild();
    }
    int getLChild(int loc){
        return element[loc].getLChild();
    }
    int getParent(int loc){
        return element[loc].getParent();
    }
}
