/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elevatorproject;

import static elevatorproject.ElevatorProject.list;
import static elevatorproject.ElevatorProject.resourcelock;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander Kronish
 */
public class thrdElevator implements Runnable {
    objElevator elevator;
    int count = 0;
    int evacuated = 0;
    boolean finished = false;
    boolean posted = false;
    Thread thisThread = new Thread();
    
    thrdElevator(){ //constructor
        elevator = new objElevator();
        posted = true;
    }

    @Override
    public synchronized void run(){
        int lockresults;
        int destination=1;
        
        System.out.println("Starting Elevator Thread...");
        
        
            for(;;){
                

                
                lockresults = resourcelock.reserve();
                if(lockresults < 0){ //did not get lock
                     System.out.println("Broken Reserve");
                     thisThread.interrupt();  //redrive interruption
                     continue;
                }
                else{
                    //System.out.println("Got lock (elev)");
                if(ElevatorProject.RunningA==false && list.isEmpty()){
                    Terminate(); //if the queue is empty & the other thread has signified it's done, this thread is also done.
                    resourcelock.release(); //Final Resource Unlock
                    break;
                }
                    if(list.isEmpty()&& elevator.getCurrentFloor()==1){ //no requests but already on floor 1
                        System.out.println("No requests. Waiting.");
                        resourcelock.release();
                        //System.out.println("Releasing Elev & Waiting"); 
                        try {
                            wait(500); //wait 5 seconds for queue to populate before checking again
                            elevator.addTime(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        continue;
                    }
                    
                    if(list.isEmpty() && elevator.getCurrentFloor()>1){ //queue was full but now depled & we are not on floor 1
                        System.out.println("No requests. Moving to floor 1 and waiting."); //NOTE: This should NEVER occur. Riders only exit from Floor 1
                        resourcelock.release();
                        while(elevator.getCurrentFloor()>1){
                            elevator.moveElevatorDown();
                            System.out.println("Moving Elevator Down, Currently Arriving At Floor: "+elevator.getCurrentFloor());
                            try {
                                wait(500); //wait 5 seconds for elevator movement
                            } catch (InterruptedException ex) {
                                Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }
                        System.out.println("Arrived at Floor 1. Waiting for request queue.");  
                        continue;
                    }
                    
                    if(!list.isEmpty()){//if there are requests
                        //this if statement should ALWAYS be entered while the elevator is at the ground floor & all previous riders have exited.
                        
                        //System.out.println("Sorting list.");
                        list.sort(); //highest request will be at the head of the list
                        System.out.println("Direction Change: UP");
                        destination = list.queryHead();
                        int destnode = list.queryHeadNode();
                        //travel to the floor listed in the head node.
                        for(int i = 1; i<destination; i++){
                            elevator.moveElevatorUp();
                            System.out.println("Moving Elevator Up. Currently arriving at floor: "+ elevator.getCurrentFloor());
                            try {
                                wait(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        int nowBoarding = list.getRiders(destnode); //how many riders to move from waiting to the elevator?
                        elevator.addRiders(nowBoarding);
                        System.out.println("Doors opening at floor: "+destination+" for "+ nowBoarding + " people.");
                        elevator.openDoors();
                        int nowRiding = elevator.getRiders(); //how many on the elevator total
                        System.out.println("Elevator now contains "+ nowRiding + " people.");
                        list.remove(); //remove the request & release the resource
                        resourcelock.release();
                        try {
                            wait(3000); //wait 30 seconds for people to get on the elevator
                        } catch (InterruptedException ex) {
                            Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("Change of Direction: DOWN");
                        
                        //return to ground floor and pick up any requests in between.
                        //but first, we must re-sort, there may be new requests, some higher than where we are.
                        resourcelock.reserve();
                        list.sort(); //get lock, sort
                        //System.out.println("Sorting.");
                        
                        
                        while(destination>1){
                            
                            int hops = 0; //how many nodes will we hop over to do a RemoveAt() if there is a request?
                            int loc = list.queryTailNode();
                            boolean found=false;
                            int next;
                            
                            destination--;
                            elevator.moveElevatorDown();
                            System.out.println("Moving Elevator Down. Currently Arriving at Floor: "+elevator.getCurrentFloor());
                            try {
                                wait(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            if(loc == -1){
                                //System.out.println("No Additional Requests.");
                                found = false; //no requests for this floor.
                                continue;
                            }

                            while(list.getInfo(loc)!=destination && loc != -1){
                                //loop to traverse and look for a certain value.
                                hops++;
                                next = list.getFlink(loc);
                                loc = next;
                                if(loc == -1){
                                    found = false;
                                    break;//did not find value. go down another floor.
                                }
                                if(list.getInfo(loc)==destination){
                                    found = true; //additional riders found on this floor.
                                    break;
                                }
                                
                            }
                            if(loc == -1){
                                found= false;
                            }
                            if(found = true && loc != -1){ //found request on the way down
                                System.out.println("Doors opening at floor "+list.getInfo(loc)+" for "+list.getRiders(loc)+" people.");
                                elevator.addRiders(list.getRiders(loc));
                                elevator.openDoors();
                                System.out.println("Elevator now contains "+ elevator.getRiders() + " people");
                                list.removeAt(hops);
                                resourcelock.release();
                                try {
                                    wait(3000); //wait 30 sec for doors
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //resourcelock.reserve();
                            }
                        }
                            if(elevator.getCurrentFloor()==1){ //reached ground floor, everybody out
                                int exiting = elevator.getRiders();
                                elevator.clearRiders();
                                elevator.openDoors();
                                try {
                                    wait(3000); //riders exiting elevator, wait 30 sec
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                System.out.println("-Arrived at ground floor.");
                                System.out.println("--Opening Doors, passengers exiting car: "+exiting);
                                evacuated = evacuated + exiting;
                                
                                System.out.println("---Passengers who have exited the building so far: "+evacuated);
                                resourcelock.release();
                                //Every time we come back to ground, display stats.
                                continue;
                            } 
                        }
                    
                    

                 }
                //System.out.println("Waiting 2 seconds for requests.");
                try {
                    wait(2000);
                    elevator.addTime(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(thrdElevator.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
        }

    boolean checkPosted(){
        return posted;
    }
    
    boolean checkFinished(){
        return finished;
    }
    
    boolean Terminate(){ //final stats
        ElevatorProject.RunningB = false;
        
        String time = String.format("%d min, %d sec", 
            TimeUnit.MILLISECONDS.toMinutes(elevator.getTime()),
            TimeUnit.MILLISECONDS.toSeconds(elevator.getTime()) - 
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elevator.getTime()))
        );
        System.out.println("TERMINATING ELEVATOR THREAD");
        System.out.println("----------");
        System.out.println("Final Results:");
        System.out.println("----------");
        System.out.println("Total number of Button Presses: " + ElevatorProject.totalRequests);
        System.out.println("People serviced: " + evacuated);
        System.out.println("Total Running Time: "+time+" // Simulation at 10x speed");
        finished = true;
        return true;
    }
}
