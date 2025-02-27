// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import java.util.Objects;
import java.util.function.Consumer;

/******************************************************************************
 * This class is a homework assignment;
 * A BallSeq is a collection of Balls.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods that are not available in the sequence class 
 * (start, getCurrent, advance and isCurrent).
 *
 * @note
 *   (1) The capacity of a sequence can change after it's created, but
 *   the maximum capacity is limited by the amount of free memory on the 
 *   machine. The constructor, addAfter, 
 *   addBefore, clone, 
 *   and concatenation will result in an
 *   OutOfMemoryError when free memory is exhausted.
 *   <p>
 *   (2) A sequence's capacity cannot exceed the maximum integer 2,147,483,647
 *   (Integer.MAX_VALUE). Any attempt to create a larger capacity
 *   results in a failure due to an arithmetic overflow. 
 *   
 *   NB: Neither of these conditions require any work for the implementors (students).
 ******************************************************************************/
public class BallSeq implements Cloneable
{
	// Implementation of the BallSeq class:
	//   1. The number of elements in the sequences is in the instance variable 
	//      manyItems.  The elements may be Ball objects or nulls.
	//   2. For any sequence, the elements of the
	//      sequence are stored in data[0] through data[manyItems-1], and we
	//      don't care what's in the rest of data.
	//   3. If there is a current element, then it lies in data[currentIndex];
	//      if there is no current element, then currentIndex equals manyItems. 

	private Ball[ ] data;
	private int manyItems;
	private int currentIndex;

	private static int INITIAL_CAPACITY = 1;

	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	private boolean wellFormed() {
		// Check the invariant.
		// 1. data is never null
		if (data == null) return report("data is null"); // test the NEGATION of the condition

		// 2. The data array is at least as long as the number of items
		//    claimed by the sequence.
		// TODO
		if (data.length < manyItems) return report("ManyItems is greater than data.length: ");

		// 3. currentIndex is never negative and never more than the number of
		//    items claimed by the sequence.
		// TODO
		if(currentIndex < 0 || currentIndex > manyItems) return report("Current element is negative or never more than the number of items claimed");

		// If no problems discovered, return true
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private BallSeq(boolean testInvariant) { }

	/**
	 * Initialize an empty sequence with an initial capacity of INITIAL_CAPACITY.
	 * The append method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty and has an initial capacity of INITIAL_CAPACITY
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for initial array.
	 **/   
	public BallSeq( )
	{
		// NB: NEVER assert the invariant at the START of the constructor.
		// (Why not?  Think about it.)
		manyItems = 0;
		data = new Ball[INITIAL_CAPACITY];
		currentIndex = manyItems;
		assert wellFormed() : "Invariant false at end of constructor";
	}


	/**
	 * Initialize an empty sequence with a specified initial capacity. Note that
	 * the append method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param initialCapacity
	 *   the initial capacity of this sequence
	 * @precondition
	 *   initialCapacity is non-negative.
	 * @postcondition
	 *   This sequence is empty and has the given initial capacity.
	 * @exception IllegalArgumentException
	 *   Indicates that initialCapacity is negative.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Ball[initialCapacity].
	 **/   
	public BallSeq(int initialCapacity)
	{
		if (initialCapacity<0)
			throw new IllegalArgumentException
			("initialCapacity is nagative:"+initialCapacity);
		try {
			manyItems = 0;
			data = new Ball[initialCapacity];
			currentIndex = manyItems;
		} catch(OutOfMemoryError e) {
			throw new OutOfMemoryError("Insufficient memory for an array with " +initialCapacity);
		} 

		assert wellFormed() : "Invariant false at end of constructor";
	}

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant failed at start of size";
		
		return manyItems;
		// size() should not modify anything, so we omit testing the invariant here
	}

	/**
	 * The first element (if any) of this sequence is now current.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence (if any) is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant failed at start of start";
		//Comparing manyItems with 0 as if there is no manyItems that means no data stored in array and hence no current element
		if(manyItems > 0)
			currentIndex = 0;
		else 
			currentIndex = manyItems;
		assert wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element (a Ball or null) that can be retrieved with the 
	 * getCurrent method. This depends on the status of the cursor.
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean isCurrent( )
	{
		assert wellFormed() : "invariant failed at start of isCurrent";
		
		if(currentIndex < manyItems)
			return true;
		else
			return false;
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence, possibly null
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Ball getCurrent( )
	{
		assert wellFormed() : "invariant failed at start of getCurrent";
		
		if(isCurrent())
			return data[currentIndex];
		else
			throw new IllegalStateException("There is no current element: "); 
	}

	/**
	 * Move forward, so that the next element is now the current element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new current element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   If there was no current element, so 
	 *   advance may not be called (the precondition was false).
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant failed at start of advance";
		
		if(isCurrent())
			currentIndex++;
		else
			throw new IllegalStateException("There is no current element: ");
		assert wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant failed at start of removeCurrent";
		
		// You will need to shift elements in the array.
		if(!isCurrent())
			throw new IllegalStateException("There is no current element: ");	
		if(isCurrent()) {
			//shifiting the elements of array to the left until the current element is reached as it is to be removed
			for(int i=currentIndex;i<manyItems-1;i++) {
				data[i] = data[i+1];
			}
			//After shifting the array to left removing the last element of array by assigning null to it and decrementing no of used elements
			manyItems--;
		}
		if(currentIndex >= manyItems) {
			currentIndex = manyItems;
		}

		assert wellFormed() : "invariant failed at end of removeCurrent";
	}

	/**
	 * Change the current capacity of this sequence if needed.
	 * @param minimumCapacity
	 *   the new capacity for this sequence
	 * @postcondition
	 *   This sequence's capacity has been changed to at least minimumCapacity.
	 *   If the capacity was already at or greater than minimumCapacity,
	 *   then the capacity is left unchanged.
	 *   If the capacity is changed, it must be at least twice as big as before.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for: new array of minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity)
	{
		
		// This is a private method: don't check invariants
		Ball[] biggerArray;
		int newCapacity;
		
		if(data.length < minimumCapacity) {
			//To find the maximum of minimum capacity and twice the length of data array
			if(minimumCapacity > 2*data.length)
				newCapacity = minimumCapacity;
			else
				newCapacity = 2*data.length;
			biggerArray = new Ball[newCapacity];
			//Copy all the elements in data array to new array
			for(int i=0;i<manyItems;++i) {
				biggerArray[i] = data[i];
			}
			data = biggerArray;
		}
	}


	/**
	 * Add a new element to this sequence, before the current element (if any). 
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 **/
	public void insert(Ball element)
	{
		assert wellFormed() : "invariant failed at start of insert";
		
		if(data.length <= manyItems) {
			ensureCapacity(manyItems+1);
		}
		if(isCurrent()) {
			//Shifiting the elements of array to the right until the current element is reached as it is to be inserted 
			for(int i=manyItems;i>currentIndex;--i) {
				data[i] = data[i-1];
			}
			//Replacing the element with the current element of ongoing sequence
			data[currentIndex] = element;
		}
		else {
			currentIndex = manyItems;
			data[manyItems] = element;
			
		}
		manyItems++; //Incrementing the used array
		assert wellFormed() : "invariant failed at end of insert";
	}


	/**
	 * Place the contents of another sequence (which may be the
	 * same one as this!) into this sequence before the current element (if any).
	 * @param addend
	 *   a sequence whose contents will be placed into this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed into
	 *   this sequence. The current element of this sequence (if any)
	 *   is unchanged.  The addend is unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this sequence.
	 **/
	public void insertAll(BallSeq addend) {
		assert wellFormed() : "invariant failed at start of addAll";
		// TODO: Implement this code.
		//int insert_newpos;
		if(addend==null) {
			throw new NullPointerException("Addend is null");
		}
		if(addend.manyItems == 0) {
			return;
		}
		
		
		BallSeq addend_temp; //Temporary assigning variable to hold the sequence that is to be inserted before the current sequence
		if(Objects.equals(this, addend)) // Comparing if the current instance is same as that of the addend
			addend_temp = addend.clone(); //if comparison is true create a clone of addend to avoid overwritting 
		else
			addend_temp = addend; // else assign addend directlt to temporary variable 
				
		ensureCapacity(manyItems+addend.manyItems);
		if(isCurrent()) {
			this.currentIndex = currentIndex;
			//Shifiting of elements of current sequence to right to make space for the sequence that is to be added before current element
			for(int i=manyItems-1;i>=this.currentIndex;--i) {
				data[addend_temp.manyItems + i] = data[i];
			}
			//Updating the current sequence with the new sequence before the current element
			for(int i=0;i<addend_temp.manyItems;++i) 
				data[this.currentIndex+i] = addend_temp.data[i];
		}
		else {
			this.currentIndex = manyItems;

			for (int i =0;i<addend_temp.manyItems;++i) 
				data[this.currentIndex + i] = addend_temp.data[i];
			}
		if (this.currentIndex <= currentIndex) {
			currentIndex = currentIndex + addend_temp.manyItems;
		}
		
		manyItems = manyItems + addend_temp.manyItems;
		assert wellFormed() : "invariant failed at end of insertAll";	
	}
		
	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public BallSeq clone( )
	{  // Clone a BallSeq object.
		assert wellFormed() : "invariant failed at start of clone";
		BallSeq answer;

		try
		{
			answer = (BallSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		
		answer.data = data.clone();

		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant failed for clone";

		return answer;
	}


	/**
	 * Used for testing the invariant.  Do not change this code.
	 */
	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a debugging instance of a BallSeq
		 * with a particular data structure.
		 * @param a static array to use
		 * @param m size to use
		 * @param c current index for cursor
		 * @return a new instance of a BallSeq with the given data structure
		 */
		public BallSeq newInstance(Ball[] a, int m, int c) {
			BallSeq result = new BallSeq(false);
			result.data = a;
			result.manyItems = m;
			result.currentIndex = c;
			return result;
		}

		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param bs instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(BallSeq bs) {
			return bs.wellFormed();
		}
	}
}
