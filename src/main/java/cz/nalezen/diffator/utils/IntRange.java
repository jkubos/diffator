/**
 * Copyright 2013 Jaroslav Kubos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.nalezen.diffator.utils;

import java.util.Iterator;

/**
 * Integer range - begin is inclusive, end is exclusive.
 * @author Jaroslav Kubos
 *
 */
public class IntRange implements Iterable<Integer> {
	
	/**
	 * Lightweight iterator of IntRange.
	 * @author Jaroslav Kubos
	 *
	 */
	class MyIterator implements Iterator<Integer> {

		int act = 0;
		
		@Override
		public boolean hasNext() {
			return act<length;
		}

		@Override
		public Integer next() {
			int res = begin+act;
			
			++act;
			
			return res;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private int begin;
	private int end;
	private int length;
	
	/**
	 * Initialize range.
	 * @param begin inclusive
	 * @param end exclusive
	 */
	public IntRange(int begin, int end) {
		initialize(begin, end);
	}
	
	/**
	 * Initialize empty range - invalid.
	 */
	public IntRange() {
		clean();
	}
	
	/**
	 * Copy constructor.
	 * @param b
	 */
	public IntRange(IntRange b) {
		initialize(b.begin, b.end);
	}

	/**
	 * Make range empty - invalid.
	 */
	public void clean() {
		begin = 0;
		end = 0;
		length = 0;
	}
	
	/**
	 * Initialize range. It is public for performance issues with objects allocation - so ranges may be reused.
	 * @param begin inclusive
	 * @param end exclusive
	 */
	public void initialize(int begin, int end) {
		if (begin>end) {
			throw new RuntimeException("End must be equal or greater than begin (begin="+begin+", end="+end+")!");
		}
		
		this.begin = begin;
		this.end = end;
		length = end-begin;
	}
	
	/**
	 * Begin of range - inclusive.
	 * @return
	 */
	public int getBegin() {
		return begin;
	}
	
	/**
	 * End of range - exclusive.
	 * @return
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Length of range, pre-calculated.
	 * @return
	 */
	public int length() {
		return length;
	}
	
	/**
	 * Check range emptiness.
	 * @return
	 */
	public boolean isEmpty() {
		return length<1;
	}
	
	/**
	 * Check whether value is within range.
	 * @param val
	 * @return
	 */
	public boolean contains(int val) {
		return val>=begin && val<end;
	}
	
	/**
	 * Extend range so passed value is inside.
	 * @param val
	 */
	public void extend(int val) {
		if (isEmpty()) {
			begin = val;
			end = val+1;
		}
		else if (val+1>end) {
			end = val+1;
		} else if (val<begin) {
			begin = val;
		} else {
			return;
		}
		
		length = end-begin;
	}

	/**
	 * Check whether 2 ranges intersects.
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean intersects(IntRange a, IntRange b) {
		return a.contains(b.begin) || a.contains(b.end-1) || b.contains(a.begin) || b.contains(a.end-1);
	}

	/**
	 * Iterator for range - lightweight.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new MyIterator();
	}

	public void print() {
		System.out.println("("+begin+", "+end+">");
	}
}