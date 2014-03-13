package org.javasimon.callback.lastsplits;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Ring/circular buffer, fixed size FIFO list. Stores values as a fixed size
 * array looping insertion point when array is full. Warnings: <ul> <li>Only
 * needed methods of the List interface have been implemented.</li> <li>This
 * class is not thread safe</li> </ul>
 *
 * @author gquintana
 * @since 3.2
 */
public class CircularList<T> extends AbstractList<T> {

	/** Elements. */
	private final T[] elements;
	/**
	 * Index + 1 of the last element.
	 * Or said differently index where will be added the next element.
	 */
	private int lastIndex;
	/**
	 * Index of the the first element.
	 * Or said differently index which will be removed when list capacity is achieved.
	 */
	private int firstIndex = -1;
	/** Number of element stored in this list. */
	private int size;

	/**
	 * Constructor.
	 *
	 * @param capacity Size of the ring buffer
	 */
	public CircularList(int capacity) {
		elements = (T[]) new Object[capacity];
	}

	/**
	 * Get capacity (buffer size).
	 *
	 * @return capacity
	 */
	public int getCapacity() {
		return elements.length;
	}

	/**
	 * Converts an index starting from 0 into an index starting from first
	 *
	 * @param index Index
	 * @return Index converted
	 */
	private int convertIndex(int index) {
		return (lastIndex + index) % elements.length;
	}

	/**
	 * Get element by index
	 *
	 * @param index Index
	 * @return Element
	 */
	public T get(int index) {
		return elements[convertIndex(index)];
	}

	/**
	 * Return the number of elements in the list
	 *
	 * @return Size
	 */
	public int size() {
		return size;
	}

	/**
	 * Tells whether the list is empty
	 *
	 * @return true if empty
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Increment an index
	 *
	 * @param index Input index
	 * @param maxIndex Assigned value when capacity is reached
	 * @return Output index
	 */
	private int incrementIndex(int index, int maxIndex) {
		int newIndex = index + 1;
		if (newIndex >= elements.length) {
			newIndex = maxIndex;
		}
		return newIndex;
	}

	/**
	 * Add an element to the list, overwriting last added element when
	 * list's capacity is reached
	 *
	 * @param newElement Element to add
	 * @return Always true
	 */
	@Override
	public boolean add(T newElement) {
		elements[lastIndex] = newElement;
		lastIndex = incrementIndex(lastIndex, 0);
		if (isEmpty()) {
			// First element
			firstIndex = 0;
			size = 1;
		} else if (isFull()) {
			// Reuse space
			firstIndex = lastIndex;
		} else {
			size = incrementIndex(size, elements.length);
		}
		return true;
	}

	private boolean isFull() {
		return size == elements.length;
	}

	/**
	 * Inserts a collection of elements, looping on them.
	 *
	 * @param newElements Collection of elements to add
	 * @return Always true
	 */
	@Override
	public boolean addAll(Collection<? extends T> newElements) {
		for (T newElement : newElements) {
			add(newElement);
		}
		return true;
	}

	/**
	 * Removes the first (inserted) element of the collection.
	 *
	 * @return Removed element or null if any
	 */
	public T removeFirst() {
		if (isEmpty()) {
			return null;
		}
		T firstElement = elements[firstIndex];
		elements[firstIndex] = null;
		firstIndex = incrementIndex(firstIndex, 0);
		return firstElement;
	}

	/**
	 * Returns the first (inserted) element.
	 *
	 * @return First element or null if any
	 */
	public T first() {
		return isEmpty() ? null : elements[lastIndex];
	}

	/**
	 * Returns the last (inserted) element.
	 *
	 * @return Last element or null if any
	 */
	public T last() {
		return isEmpty() ? null : elements[lastIndex];
	}

	/** Removes all elements from the list. */
	@Override
	public void clear() {
		Arrays.fill(elements, null);
		lastIndex = 0;
		firstIndex = -1;
		size = 0;
	}

	/**
	 * Creates a new iterator to browse elements.
	 *
	 * @return Iterator
	 */
	@Override
	public java.util.Iterator<T> iterator() {
		if (isEmpty()) {
			return new EmptyIterator();
		} else {
			return new MainIterator();
		}
	}

	/** Empty iterator used when the list is empty. */
	private class EmptyIterator implements Iterator<T> {

		public boolean hasNext() {
			return false;
		}

		public T next() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void remove() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	/** Main iterator user when the list contains at least one element. */
	private class MainIterator implements Iterator<T> {

		/** Index of the element. */
		private int nextIndex = firstIndex;
		/**
		 * Is it first element. This flag is required because when
		 * firstIndex=lastIndex (ring buffer is completely used) we
		 * don't whether it's the first iteration or the last one.
		 */
		private boolean begin = true;

		/** Is there another element in the list. */
		public boolean hasNext() {
			return begin || nextIndex != lastIndex;
		}

		/** Returns an element and compute the next one. */
		public T next() {
			T nextElement = elements[nextIndex];
			begin = false;
			nextIndex = incrementIndex(nextIndex, 0);
			return nextElement;
		}

		public void remove() {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	/**
	 * Copy elements in a target array.
	 *
	 * @param elementsCopy Target array
	 */
	private <X> void copy(X[] elementsCopy) {
		if (!isEmpty()) {
			if (firstIndex < lastIndex) {
				System.arraycopy(elements, firstIndex, elementsCopy, 0, lastIndex - firstIndex);
			} else {
				int firstSize = elements.length - firstIndex;
				System.arraycopy(elements, firstIndex, elementsCopy, 0, firstSize);
				System.arraycopy(elements, 0, elementsCopy, firstSize, lastIndex);
			}
		}
	}

	@Override
	public Object[] toArray() {
		Object[] elementsCopy = new Object[size];
		copy(elementsCopy);
		return elementsCopy;
	}

	@Override
	public <T> T[] toArray(T[] elementsCopy) {
		if (elementsCopy.length >= size) {
			copy(elementsCopy);
			return elementsCopy;
		} else {
			return (T[]) toArray();
		}
	}
}
