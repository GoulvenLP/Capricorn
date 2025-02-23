package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.Location;

import java.util.LinkedList;
import java.util.Queue;

public class RegisteredMobiles {

    private String mobileName;
    private Queue<Location> queue;
    public RegisteredMobiles(String mobileName){
        this.mobileName = mobileName;
        this.queue = new LinkedList<>();
    }

    /**
     * Adds a new Location to the queue. The queue is of size 10.
     * If an item is added to the queue making the queue to be bigger than 10,
     * pops off the oldest location (i.e the first of the queue).
     * @param o: the Location to add to the queue
     * @return true if the move succeeds, else returns false
     */
    public boolean add(Object o) {
        if (o == null) return false;
        if (!(o instanceof Location)) return false;
        if (this.queue.size() > 10) {
            this.queue.poll();
        }
        this.queue.add((Location)o);
        return true;
    }


    /**
     * Sets the equality of objects based on their respective names
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (!(o instanceof RegisteredMobiles)) return false;
        RegisteredMobiles r = (RegisteredMobiles)o;
        if (this.mobileName.equals(r.getName())){
            return true;
        }
        return false;
    }

    public Queue<Location> getQueue(){
        return this.queue;
    }

    protected String getName(){
        return this.mobileName;
    }

}
