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

package cz.nalezen.diffator.impl;

import cz.nalezen.diffator.Content;
import cz.nalezen.diffator.DiffEventsHandler;
import cz.nalezen.diffator.DiffEventsHandler.EventSide;
import cz.nalezen.diffator.DiffEventsHandler.EventType;
import cz.nalezen.diffator.utils.IntRange;
import cz.nalezen.diffator.utils.Overlap;

/**
 * Basic implementation containing basic recursive algorithm - subimplementations just add longest substring search.
 * @author Jaroslav Kubos
 *
 * @param <T> Type for comparison calculation data
 */
public abstract class ContentsComparatorImpl<T extends ContentsComparatorContext> {

	/**
	 * 
	 * @param a left content
	 * @param b right content
	 * @param consumer callback (may be null)
	 * @param minBlockSize when block of content is smaller that this it is considered as different
	 * @return
	 */
	public double compare(Content a, Content b,DiffEventsHandler consumer,  int minBlockSize) {
		Overlap overlap = new Overlap();

		//it starts over whole range
		IntRange helpA = new IntRange(a.getRange());
		IntRange helpB = new IntRange(b.getRange());
		
		//create and initialize
        T context = createContext();
        context.initialize(a, b);

        //report begin
        if (consumer!=null) {
        	consumer.onBeforeStart();
        }
        
        //start recursion
		double res = compareIntervalsRecursively(a, helpA, b, helpB, minBlockSize, consumer, overlap, context);
		
		//report end
		if (consumer!=null) {
        	consumer.onDone(res);
        }
		
		return res;
	}
	
	/**
	 * Create context object - where data are stored during comparison. This method is required because
	 * Java can't allocate object of class which is template parameter.
	 */
	protected abstract T createContext();
	
	/**
	 * Find longest substring in given parts of contents.
	 * @param a Left content
	 * @param rangeA Search area in left content
	 * @param b Right content
	 * @param rangeB Search area in right content
	 * @param res Overlap where is stored result
	 * @param context Context object where algorithm may store calculation data.
	 */
	protected abstract void findBiggestOverlap(Content a, IntRange rangeA, Content b, IntRange rangeB, Overlap res, T context);
	
	/**
	 * Recursively divides content to: biggest shared part, part before it, part after it. 
	 * On before&after parts runs itself recursively until minBlockSize is reached.
	 * @param a Left content
	 * @param rangeA Search area in left content
	 * @param b Right content
	 * @param rangeB Search area in right content
	 * @param minBlockSize When search area is smaller than this value then is considered different.
	 * @param consumer Callback handler.
	 * @param overlap Shared object - allocated once.
	 * @param context Context object where algorithm may store calculation data.
	 * @return
	 */
	private double compareIntervalsRecursively(Content a, IntRange rangeA, Content b, IntRange rangeB, int minBlockSize, DiffEventsHandler consumer, 
			Overlap overlap, T context) {
		
		//empty range - quit
		if (rangeA.isEmpty() || rangeB.isEmpty()) {
			return 0;
		}

		double sum = 0;
	
		//search for longest substring - by algorithm delivered by derived class
		overlap.clean();
		findBiggestOverlap(a, rangeA, b, rangeB, overlap, context);

		//one of ranges is empty
		if (overlap.getA().isEmpty() || overlap.getB().isEmpty()) {
			
			//report to callback
			if (consumer!=null) {
				consumer.handle(a, rangeA, EventType.DIFFERENT_PART, EventSide.LEFT);
				consumer.handle(b, rangeB, EventType.DIFFERENT_PART, EventSide.RIGHT);
			}
			
			return 0;
		}
		
		//overlap ranges must have same length
		if (overlap.getA().length()!=overlap.getB().length()) {
			throw new RuntimeException("Overlap size differs!?");
		}
		
		//overlap smaller than required
		if (overlap.getA().length()<minBlockSize) {
			
			//report to callback
			if (consumer!=null) {
				consumer.handle(a, rangeA, EventType.DIFFERENT_PART, EventSide.LEFT);
				consumer.handle(b, rangeB, EventType.DIFFERENT_PART, EventSide.RIGHT);
			}
			
			return 0;
		}
		
		//add smaller overlap ratio to sum
		sum += overlap.getA().length()/(double)Math.max(rangeA.length(), rangeB.length());
	
		//store parameters on stack - avoid memory allocation on heap
		int h1aBegin = rangeA.getBegin();
		int h1aEnd = overlap.getA().getBegin();
		int h1bBegin = rangeB.getBegin();
		int h1bEnd = overlap.getB().getBegin();
		
		int h2aBegin = overlap.getA().getEnd();
		int h2aEnd = rangeA.getEnd();
		int h2bBegin = overlap.getB().getEnd();
		int h2bEnd = rangeB.getEnd();
		
		//which side takes bigger part of its whole content		
		double beforeMaxPart = Math.max((h1aEnd-h1aBegin)/(double)rangeA.length(), (h1bEnd-h1bBegin)/(double)rangeB.length());		
		double afterMaxPart = Math.max((h2aEnd-h2aBegin)/(double)rangeA.length(), (h2bEnd-h2bBegin)/(double)rangeB.length());
		
		//define working area before common part
		rangeA.initialize(h1aBegin, h1aEnd);
		rangeB.initialize(h1bBegin, h1bEnd);

		//parts before current overlap
		sum += beforeMaxPart*compareIntervalsRecursively(a, rangeA, b, rangeB, minBlockSize, consumer, overlap, context);
		
		//report to callback - common part
		if (consumer!=null) {
			rangeA.initialize(h1aBegin, h2aBegin);
			rangeB.initialize(h1bBegin, h2bBegin);
			
			consumer.handle(a, rangeA, EventType.COMMON_PART, EventSide.LEFT);
			consumer.handle(b, rangeB, EventType.COMMON_PART, EventSide.RIGHT);
		}
		
		//define working area after common part
		rangeA.initialize(h2aBegin, h2aEnd);
		rangeB.initialize(h2bBegin, h2bEnd);
		
		//parts after current overlap
		sum += afterMaxPart*compareIntervalsRecursively(a, rangeA, b, rangeB, minBlockSize, consumer, overlap, context);
		
		return sum;
	}
}
