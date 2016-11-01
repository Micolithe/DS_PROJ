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
public class objElevator {;
    int currFloor=1;
    int timeElapsed=0; 
    int totalRiders = 0;
    final int totalFloors = 10;
    
    objElevator(){
        currFloor = 1;
        totalRiders = 0;
        timeElapsed=0;
    }
    int getCurrentFloor(){
        return currFloor;
    }
    void clearRiders(){
        totalRiders=0;
    }
    void openDoors(){
        addTime(30000);
    }
    int getRiders(){
        return totalRiders;
    }
    
    void addRiders(int x){
        totalRiders = totalRiders + x;
    }
    
    int getTime(){
        return timeElapsed;
    }
    void addTime(int x){ //in ms
        timeElapsed = timeElapsed + x;
    }
    
    void moveElevatorUp(){
        currFloor++;
        addTime(5000);
        
    }
    void moveElevatorDown(){
        currFloor--;
        addTime(5000);
    }
            
}
