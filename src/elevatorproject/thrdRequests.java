/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elevatorproject;
import static elevatorproject.ElevatorProject.list;
import static elevatorproject.ElevatorProject.resourcelock;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Alexander Kronish
 */
public class thrdRequests implements Runnable {
//fields
    boolean posted = false;
    boolean finished = false;
    int floors = 0;
    int loops = 0;
    int counter = 0;
    Thread thisThread = new Thread();
    
    
//constructor accepts arguments: no of requests before ending, no of floors.
public thrdRequests(int reqs, int fl){
    floors = fl;
    loops = reqs;
    posted = true;
    
}


//methods

    
    @Override
public synchronized void run(){ //thread main method
    //reqThread = Thread.currentThread();
    int floornumber;
    int riders;
    int lockresults;
    System.out.println("Starting requests thread...");
    while(loops > counter){
        if (requestGamble()){
            //go inside this if statement once a request occurs
                    lockresults = resourcelock.reserve();
                    floornumber = getRequestedFloor();
                    riders = getNumRiders();
                    if (lockresults < 0)   //Interrupted ?
                    {
                        //System.out.println("Broken Reserve");
                        thisThread.interrupt();  //redrive interruption
                        continue;
                    }
                    if (lockresults == 0){
                        //System.out.println("Got Lock");
                    }
                    
                    //if there is already a request on this floor, all we have to do is add the riders to it. else, we create a new entry in our list.
                    boolean alreadyrequested=false;
                    int loc, next;
                    loc = list.queryTailNode();
                    for(int i = 0;i<list.queryPop();i++){
                        if(loc == -1){
                            break;
                        }
                        
                        if (list.getInfo(loc)==floornumber){
                            //already a request on this floor, just add the riders.
                            list.addRiders(loc, riders);
                            System.out.println("Additional riders ("+riders+") added on floor " + floornumber + " for a total of "+list.getRiders(loc));
                            alreadyrequested = true;
                            break;
                        }
                        else{
                            next = list.getFlink(loc);
                            loc = next;
                        }
                        alreadyrequested = false;
                    }
                    if(alreadyrequested == false){
                        list.insert(floornumber, riders);
                        System.out.println("Button pressed on Floor "+floornumber+" ("+riders+" people) - Current Queue Size: "+list.queryPop());
                    }
                    counter++;
                resourcelock.release();
                //System.out.println("Releasing Lock");
        }
            try {
                wait(100); //one attempt per second, wait and go back to the while loop
            } catch (InterruptedException ex) {
                Logger.getLogger(thrdRequests.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    Terminate();
    return;
    
}
    
boolean requestGamble(){
    //1 in 20 chance of a request
    int value = (int) (Math.random() * 20);
    
    if(value == 1){
        return true;
    }
    else{
        return false;
    }
}
boolean checkFinished(){
    return finished;
}

boolean checkPosted(){
    return posted;
}
int getRequestedFloor(){
    int floor = (int) (Math.ceil(Math.random() * (floors - 1))) + 1;
    return floor;
}

int getNumRiders(){
    int riders = (int) Math.ceil(Math.random()*2);
    return riders;
}
boolean Terminate(){
    finished = true;
    ElevatorProject.RunningA=false;
    ElevatorProject.totalRequests = counter;
    System.out.println("TERMINATING REQUESTS THREAD");
    finished = true;
    return true;
}
}
