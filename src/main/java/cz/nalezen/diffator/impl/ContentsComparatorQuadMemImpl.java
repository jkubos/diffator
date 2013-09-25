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
import cz.nalezen.diffator.utils.IntRange;
import cz.nalezen.diffator.utils.Overlap;

/**
 * Implementation using M*N memory
 * @author Jaroslav Kubos
 *
 */
public class ContentsComparatorQuadMemImpl extends ContentsComparatorImpl<ContentsComparatorQuadMemImplContext> {

	@Override
	protected ContentsComparatorQuadMemImplContext createContext() {
		return new ContentsComparatorQuadMemImplContext();
	}

	@Override
	protected void findBiggestOverlap(Content a, IntRange rangeA, Content b, IntRange rangeB, Overlap res, ContentsComparatorQuadMemImplContext context) {	
		int maxLen = 0;

		for (int i = rangeA.getBegin()+1; i <= rangeA.getEnd(); i++) {
			
			int maxLenA = i-rangeA.getBegin();
			
	        for (int j = rangeB.getBegin()+1; j <= rangeB.getEnd(); j++) {
	        	
	        	int maxLenB = j-rangeB.getBegin();
                    
                int adjLen = Math.min(Math.min(maxLenA, maxLenB),  context.getBuffer()[i][j]); 
	        	
                if (adjLen > maxLen) {
                    maxLen = adjLen;

            		res.getA().initialize(i-adjLen, i);
            		res.getB().initialize(j-adjLen, j);
                }
	        }
	    }
	}

}