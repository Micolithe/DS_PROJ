/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elevatorproject;

import static java.lang.Thread.sleep;

/**
 *
 * @author Alexander Kronish
 */
public class ElevatorProject {
    int total = 0;
    int time = 0;
    public static dlinkqueueds list;
    public static cpEnq resourcelock;
    public static boolean RunningA = false;
    public static boolean RunningB = false;
    public static int totalRequests;
    //shared resources
    static{
        list = new dlinkqueueds(1000);
        resourcelock = new cpEnq();
    }
    
    thrdRequests a = new thrdRequests(1000,10);
    Thread requestThread = new Thread(a, "requestThread");
    thrdElevator b = new thrdElevator();
    Thread elevatorThread = new Thread(b, "elevatorThread");
    
    
    ElevatorProject() throws InterruptedException{
        
        total = 0;

        boolean finish = false;
        boolean start = false;
        while(start = false){
            if(a.checkPosted() && b.checkPosted()){
                
                start = true; 
            }
            else{
                System.out.println("Both threads not posted yet");
                wait(100);
            }
        }
        System.out.println("-- Starting Elevator Simulation --");
        System.out.println("Data Structures Project by");
        System.out.println("Alex Kronish and Lindsay Dale");
        System.out.println("Spring 2016");
        System.out.println("-----------------------------------");
        System.out.println(" ");
        requestThread.start();
        RunningA=true;
        elevatorThread.start();
        RunningB=true;
            
        while(!b.checkFinished()){
            sleep(10000); //if the elevator thread returns that it is still running, we cannot continue.
        }
        System.out.println("Both threads terminated. Program exiting!");
    }
    public static void main(String[] args) throws InterruptedException {
        new ElevatorProject();
    }
}

