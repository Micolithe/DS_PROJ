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
public class dlinkqueueds { //fields
    int pop = 0;
    int tail = -1;
    int head = -1;
    linkstackds sds;
    
    
    dlinkqueueds(int n){ //constructor creates a double link list of size n
        //constructs a link stack to pull nodes from 
        //& init environment vars
        pop = 0;
        tail = -1;
        head = -1;
        sds = new linkstackds(n);
    }
    dlinkqueueds(linkstackds s){
        sds = s;
    }
    
    int getInfo(int loc){ //returns data in the info field (calls to getInfo in stackds class)
        return sds.element[loc].getInfo();
    }
    
    boolean insert(int x, int riders){ // gets a node and inserts value X at the tail
        int n = sds.getNode();
        if (n == -1){
            
            //System.out.println("Could not get node. Source stack empty? D. Link List full?");
            return false;
        }
        else{
            //System.out.println("Got Node no: "+n+" || Inserting value:"+x);
            sds.element[n].setInfo(x);
            sds.element[n].setRiders(riders);
            sds.element[n].setFlink(tail);
            sds.element[n].setBlink(-1);

            if(tail !=-1){ //is tail ground? if not, we can set the Back Link
                sds.element[tail].setBlink(n);       
            }     
            tail = n; 
            
            if (pop == 0){ //if pop = 0 the structure is empty, and head & tail will be the same node
                head = tail;
            }
            pop ++; //increment population
            return true;
        }
    }
     void addRiders(int n, int x){
        sds.element[n].setRiders(x + sds.element[n].getRiders());
        
    }
    boolean remove(){ //removes an element from the head, if there is one.
        if(isEmpty()){
            return false; //structure is empty? cant remove.
        } 
        int p = head; //the node number of the item to remove
        if(p == -1){ //If the head is ground, we should use tail. Ideally this case will never happen, as we check isEmpty() first.
            p = tail;
            sds.putNode(p);
            tail = -1;
            pop--;
            return true;
        }
        head = sds.element[head].getBlink(); //set new head by getting the previous heads back link

        if(head != -1){
            sds.element[head].setFlink(-1); //set the new heads forward link to ground
        }
        else{
            tail = -1;  //if the new head is -1, that means we've removed the
        }               //last element and as such tail should also be ground
        sds.putNode(p); //return node to the stack
        pop --; //deincrement population
        return true;
    }
    
    boolean isEmpty(){ //Structure is empty, return true. Otherwise return false.
        if (pop == 0){
            return true;
        }
        else{
            return false;
        }
    }
    boolean isFull(){ //Structure is full return true, otherwise return false.
        if (pop == sds.maxsize){
            return true;
        }
        return false;
    }
    
    boolean insertAt(int val, int riders, int loc){
        //priority insert, val = data to store, loc = node to insert in front of
        if (loc == 0 || pop ==0){ //no items in list or trying to insert at tail, call regular insert
            boolean rc = insert(val, riders);
            return rc;
        }
        if (isFull()){ //no room, return false
            return false;
        }
        int n = sds.getNode(); //get a node
        if (n < 0){ //if the returned node is out of bounds (error getting node) return false
            return false;
        }
        sds.element[n].setInfo(val); //store data in node
        if (loc == (pop)){
            //replace at head
            sds.element[n].setFlink(-1);
            sds.element[n].setBlink(head);
            sds.element[head].setFlink(n);
            head = n;
        }
        else{
            //insert in middle at index [loc]
            int q = tail;
            for(int i = 0; i < loc; i++){
               q = sds.element[q].getFlink();
            }
            sds.element[n].setFlink(q);
            int qb = sds.element[q].getBlink();
            sds.element[n].setBlink(qb);
            sds.element[qb].setFlink(n);
            sds.element[q].setBlink(n);
        }
        pop++; //increment pop
        return true;
    }
    boolean removeAt(int loc){ // removes a value a number of hops away from the tail [loc]
        if ( pop == 0 || loc == (pop -1)){
            boolean rc = remove();
            return rc;
        }
        if (loc == 0){//remove from tail
            int q = sds.element[tail].getFlink(); //store the forward link this will be our new tail
            sds.element[q].setBlink(-1); //new tails back link is now ground
            sds.putNode(tail); //return node to stack
            tail =q; //set new tail
        }
        if (loc > 0){
            int t = tail;
            for(int i = 0; i<loc;i++){ //traverse to get to where we need to remove, store the forward link for later
                t = sds.element[t].getFlink(); //when this loop ends, t will be the forward link of the node to be deleted
            }
            int q = sds.element[t].getBlink(); //removed nodes back link
            int r = sds.element[t].getFlink(); //removed nodes forward link
            sds.element[r].setBlink(q); //reattach the links on either side of the node to be removed
            sds.element[q].setFlink(r);
            sds.putNode(t); // all links reattached, return node to stack
        }
        
        pop--; //deincrement population
        return true;
       
    }
    int queryPop(){
        return pop;
    }
    int queryHead(){
        if(isEmpty()){
            return -1;
        }
        return sds.element[head].getInfo();
    }
    int queryHeadNode(){
        return head;
    }
    int queryTail(){
        if(isEmpty()){
            return -1;
        }
        return sds.element[tail].getInfo();
    }
    int queryTailNode(){
        return tail;
    }
    int getFlink(int loc){
        return sds.element[loc].getFlink();
    }
    int getBlink(int loc){
        return sds.element[loc].getBlink();
    }
    void dumpDS(){   //displays all data in structure in a list. the dlinkqueuetest.java 
                     //class calls this every time an insertion or deletion occurs
        System.out.println("Double Link Queue Contents");
        System.out.println(" ");
        System.out.println("Node Number: -- Value: -- F.Link: -- B.Link: ");
        boolean go = true;
        int loc = tail;
        System.out.println("--TAIL-- [Node " + queryTailNode()+"]");
        while(go == true){
            if(loc == -1){ //if tail is -1 the structure is empty.
                System.out.println("Structure is empty!");
                break;
            }
            if (sds.element[loc].getFlink() == -1){ //if the forward link of a node is -1, theres 
                                                    // nowhere left to go and we have an exit case for our while loop.
               go= false;                          
            }
            System.out.printf("%12d %9d %10d %10d %n", loc, getInfo(loc),sds.element[loc].getFlink(),sds.element[loc].getBlink());
            //System.out.println("");
            loc = sds.element[loc].getFlink();
        }
        System.out.println("--HEAD-- [Node "+ queryHeadNode()+"]");
    }
    int getRiders(int loc){
        return sds.element[loc].riders;
    }
    boolean sort(){
        //bubble sort
        boolean doanotherpass = false;
        int loc = tail;
        if(loc == -1){
            return false; //struct empty, return a failure state as there is nothing to sort
        }
        for(int i = 0; i<queryPop();i++){ //traversal. keep track of current node + next node in list
            int nodeAvalue = sds.element[loc].getInfo();
            int next = sds.element[loc].getFlink();
            if(next ==-1){ //no second node, we have traversed to the end
                break;
            }
            int nodeBvalue = sds.element[next].getInfo();
            if(nodeAvalue>nodeBvalue){ //take the values compared and swap the data
                int nodeAriders = sds.element[loc].getRiders();
                int nodeBriders = sds.element[next].getRiders();
                sds.element[loc].setInfo(nodeBvalue);
                sds.element[next].setInfo(nodeAvalue);
                sds.element[loc].setRiders(nodeBriders);
                sds.element[next].setRiders(nodeAriders);
                doanotherpass = true;
            }
            loc = next;
        }
        if (doanotherpass){
            sort();
        }
            return true;
    }
    
}

