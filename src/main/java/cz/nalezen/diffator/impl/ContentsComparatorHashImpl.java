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

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import cz.nalezen.diffator.Content;
import cz.nalezen.diffator.utils.IntRange;
import cz.nalezen.diffator.utils.Overlap;

/**
 * Implementation using sparse array - implemented as list of hashmap. So every row exists but just some data in columns.
 * @author Jaroslav Kubos
 *
 */
public class ContentsComparatorHashImpl extends ContentsComparatorImpl<ContentsComparatorHashImplContext> {

	@Override
	protected ContentsComparatorHashImplContext createContext() {
		return new ContentsComparatorHashImplContext();
	}

	@Override
	protected void findBiggestOverlap(Content a, IntRange rangeA, Content b, IntRange rangeB, Overlap res, ContentsComparatorHashImplContext context) {
		int maxLen = 0;
		
		for (int i = rangeA.getBegin()+1; i <= rangeA.getEnd(); i++) {
			
			LinkedHashMap<Integer, Integer> row = context.getBuffer().get(i);
			
			int maxLenA = i-rangeA.getBegin();
			
	        for (Entry<Integer, Integer> e : row.entrySet()) {
	        	
	        	if (e.getKey()<rangeB.getBegin()+1) {
	        		continue;
	        	} else if (e.getKey()>rangeB.getEnd()) {
	        		break;
	        	}
	        	
	        	int j = e.getKey();
	        	
	        	int maxLenB = j-rangeB.getBegin();
	                
	            int adjLen = Math.min(Math.min(maxLenA, maxLenB),  e.getValue()); 
	        	
	            if (adjLen > maxLen) {
	                maxLen = adjLen;
	
	        		res.getA().initialize(i-adjLen, i);
	        		res.getB().initialize(j-adjLen, j);
	            }
	        }
	    }
	}
}