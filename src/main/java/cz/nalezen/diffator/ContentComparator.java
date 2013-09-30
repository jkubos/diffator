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

package cz.nalezen.diffator;

import cz.nalezen.diffator.impl.ContentsComparatorHashImpl;
import cz.nalezen.diffator.impl.ContentsComparatorImpl;

/**
 * Class doing actual comparation/diff. Provides content similarity ratio in range from 0.0 to 1.0.
 *  This number means "how big portion of longer content is covered by shorter one". So 0.0 means no match, 1.0 means complete match.
 *  During comparison callback methods of optionally delivered handler are called in SAX fashion. This may be simply used for diff purposes.
 *  This class instance is threadsafe - each comparison owns object with comparison related data (ContentsComparatorContext subclass).
 * @author Jaroslav Kubos
 *
 */
public class ContentComparator {

	/**
	 * Shorthand for instance method with same signature. Internally using default implementation.
	 */
	public static double compareStatic(Content a, Content b) {
		return new ContentComparator().compare(a, b);
	}
	
	/**
	 * Shorthand for instance method with same signature. Internally using default implementation.
	 */
	public static double compareStatic(Content a, Content b, DiffEventsHandler consumer) {
		return new ContentComparator().compare(a, b, consumer);
	}
	
	/**
	 * Shorthand for instance method with same signature. Internally using default implementation.
	 */
	public static double compareStatic(Content a, Content b, DiffEventsHandler consumer, int minBlockSize) {
		return new ContentComparator().compare(a, b, consumer, minBlockSize);
	}
	
	private ContentsComparatorImpl<?> impl;
	
	/**
	 * Non-parametric constructor using default implementation ContentsComparatorHashImpl
	 */
	public ContentComparator() {
		this.impl = new ContentsComparatorHashImpl();
	}

	/**
	 * Constructor taking comparator implementation
	 * @param impl
	 */
	public ContentComparator(ContentsComparatorImpl<?> impl) {
		this.impl = impl;
	}
	
	/**
	 * Compares two contents.
	 */
	public double compare(Content left, Content right) {
		return compare(left, right, null, 0);
	}
	
	/**
	 * Compares two contents, firing events to the handler.
	 */
	public double compare(Content left, Content right, DiffEventsHandler handler) {
		return compare(left, right, handler, 0);
	}
	
	/**
	 * Compares two contents, firing events to the handler. When during recursion division part size is smaller than
	 * minBlockSize it is considered as different. This may make comparison faster.
	 */
	public double compare(Content left, Content right, DiffEventsHandler handler, int minBlockSize) {
		return impl.compare(left, right, handler, minBlockSize);
	}
	
	/**
	 * Compares two contents in same fashion as compare() methods does. Only difference is that it takes minimum expected value of result.
	 * If from size of contents is obvious that this goal cannot be reached not comparison is done and 0 is returned.
	 */
	public double compareWithExpectation(double minExpectation, Content left, Content right, DiffEventsHandler handler, int minBlockSize) {
		
		double bestPossible = Math.min(left.length(), right.length())/(double)Math.max(left.length(), right.length());
		
		if (bestPossible<minExpectation) {
			return 0;
		}
		
		return compare(left, right, handler, minBlockSize);
	}
}

