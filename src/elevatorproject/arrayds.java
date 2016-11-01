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
public class arrayds {
    public int arraysize;
    public int pop;
    private NodeElement[] element;
    
    public arrayds(int n){ //Constructor. int n is size of array
        element = new NodeElement[n];
        arraysize = n;
        for(int i=0; i<n; i++){
            element[i]= new NodeElement();
            element[i].setInfo(-1);
        }
        pop = 0;
    }
    
    public int querySize(){
        return arraysize;
    }
    
    public boolean isFull() {
        if (pop == arraysize){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean addElement(int x){
        if (isFull() == true){
            return false;
        }
        for(int i=0; i<arraysize; i++){
            if(element[i].getInfo() == -1){
                element[i].setInfo(x);
                pop++;
                break;
            }
        }
        return true;
    }
    
    public boolean isEmpty(){
        if(pop ==0){
            return true;
        }
        else{
            return false;
        }
    }
    
    public int queryPop(){
        return pop;
    }
    
    public int queryElement(int n){
        return element[n].getInfo();
    }
    
    public boolean removeElement(int n){
        if(0>n || n>arraysize){ //out of bounds index passed
            return false;
        }
        if(element[n].getInfo() == -1){ //this index holds no data
            return false;
        }
        else{
            element[n].setInfo(-1);
            pop--;
            return true;
        }
    }
    public int findElementA(int x){
        int loc = -1;
        for(int i = 0; i<arraysize; i++){
            if(element[i].getInfo() == x){
                loc = i;
                break;
            }
        }
        return loc;
    }
    
    public int findElement(int lo, int hi, int val){
        
        if(lo > hi){
            return -1; // not found, termination
        }
        int m = (hi + lo)/2;
        if(val<element[m].getInfo()){
            return findElement(lo, m-1, val); //value less than m
        }
        if(val>element[m].getInfo()){
            return findElement(m+1, hi, val); //value greater than m
        }
            return m; //found, termination
    }
    
    
    public boolean updateElement(int n, int x){
        if(0>n || n>=arraysize){
            return false;
        }
        if (element[n].getInfo() == -1){
            pop++;
            element[n].setInfo(x);
            return true;
        }
        if (element[n].getInfo() != -1){
            element[n].setInfo(x);
            return true;
        }
        else{
            return false;
            
        }    
    }
}
