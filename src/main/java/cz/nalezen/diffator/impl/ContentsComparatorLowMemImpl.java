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
 * Implementation using 2*N memory
 * @author Jaroslav Kubos
 *
 */
public class ContentsComparatorLowMemImpl extends ContentsComparatorImpl<ContentsComparatorLowMemImplContext> {

	@Override
	protected ContentsComparatorLowMemImplContext createContext() {
		return new ContentsComparatorLowMemImplContext();
	}

	/**
	 * Calculate substring on interesting area again and again.
	 */
	@Override
	protected void findBiggestOverlap(Content a, IntRange rangeA, Content b, IntRange rangeB, Overlap res, ContentsComparatorLowMemImplContext context) {
	    int cost = 0;
	    int maxLen = 0;

		for (int i = 0; i < rangeA.length(); ++i) {
			for (int j = 0; j < rangeB.length(); ++j) {
				int indA = rangeA.getBegin()+i;
				int indB = rangeB.getBegin()+j;

				if (!a.getItem(indA).equals(b.getItem(indB))) {
					cost = 0;
				} else {					
					if ((i == 0) || (j == 0)) {
						cost = 1;
					} else {
						cost = context.getActiveBuffer()[j - 1] + 1;
					}
				}
				
				context.getInactiveBuffer()[j] = cost;
				
				if (cost > maxLen) {
					maxLen = cost;

					res.getA().initialize(indA-maxLen+1, indA+1);
					res.getB().initialize(indB-maxLen+1, indB+1);
				}
			}
			
			context.swapBuffers();
		}
	}
}
