package elevatorproject;

//
//  Author.  Geoffrey J. Cullen
//  Copyright (c) 1999, 2005  Cullen Programming
//  All Rights Reserved.
//
//  Purpose: Reserve/Release Serialization
//
//
//package CPutils;

// Purpose.....: Constructor
//
public class cpEnq
{
    private boolean locked = false;
    private boolean verbose = false;

// Purpose.....: Reserve/Release locking mechanism.
//
public cpEnq()
{
    locked = false;
}

public cpEnq(boolean v)
{
    locked = false;
    verbose = v;
}

// Purpose.....: Request and reserve a lock on a resource.
// The instance name of this class is the resource name.
//
public synchronized int reserve()
{
    while (locked)
    {
        try
        {
            if (verbose)
            {
                System.out.println("Waiting for LOCK");
            }
            wait();
        }
        catch (InterruptedException e1)
        {
            locked = false;
            if (verbose)
            {
                System.out.println("RESERVE Interrupted");
            }
            return -1;   //Interrupted
        }
    }  // end while loop
    if (verbose)
    {
        System.out.println("Acquired LOCK");
    }
    locked = true;
    return 0;
}


// Purpose.....: Attempt to reserve a lock on a resource.
// Wait until TIMEOUT expires before failing.
// The instance name of this class is the resource name.
//
public synchronized int attemptReserve(long timeout)
{
    long start = System.currentTimeMillis();
    long waitTime = timeout;
    while (locked)
    {
        try
        {
        wait(waitTime);
        if (locked == false)
        {
            break;
        }
        else
        {
            long now = System.currentTimeMillis();
            waitTime = timeout - (now - start);
            if (waitTime <= 0)
            {
                return 1;   //Timeout occurred, still locked
            }
        }
        }
        catch (InterruptedException e1)
        {
            return -1;   //Interrupted
        }
    } // end while loop
    if (verbose)
    {
        System.out.println("Acquired LOCK");
    }
    locked = true;
    return 0;
    }


// Purpose.....: Release the lock on a resource.
//
public synchronized int release()
{
    if (verbose)
    {
        System.out.println("Releasing the LOCK");
    }
    locked = false;
    notifyAll();
    return 0;
  }


// Purpose.....: Query the state of the lock.
//
public synchronized int  testlock()
{
    if (locked)
    {
        return 1;
    }
    return 0;
}


// Purpose.....: Test and Set lock if available.
//
public synchronized int testset()
{
  if (locked)
  {
     return 1;   // locked by some other
  }
  locked = true;
  return 0;
}

}