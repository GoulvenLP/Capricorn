/**
* Classe SortedList.java
*@author Olivier VERRON
*@version 1.0.
*/
package enstabretagne.simulation.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SortedList.
 *
 * @param <T> the generic type
 */
public class SortedList<T extends Comparable<T>> implements Iterable<T> {

	/** The l. */
	List<T> l;
	
	/**
	 * Instantiates a new sorted list.
	 */
	public SortedList()
	{
		l = new ArrayList<T>();
	}
	
	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return l.size();
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		l.clear();
	}
	
	/**
	 * Adds the.
	 *
	 * @param element the element
	 */
	public void add(T element) {
		if(l.contains(element))
			l.remove(element);
		
		l.add(element);
		Collections.sort(l);
	}
	
	/**
	 * Removes the.
	 *
	 * @param element the element
	 */
	public void remove(T element)
	{
		l.remove(element);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return l.iterator();
	}
	
	/**
	 * First.
	 *
	 * @return the t
	 */
	public T first(){
		return l.get(0);
	}

	/**
	 * Contains.
	 *
	 * @param ev the ev
	 * @return true, if successful
	 */
	public boolean contains(T ev) {
		return l.contains(ev);
	}
}

