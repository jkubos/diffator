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

/**
 * Data for QuadMem impl.
 * @author Jaroslav Kubos
 *
 */
public class ContentsComparatorQuadMemImplContext implements ContentsComparatorContext {

	int[][] buffer;
	
	@Override
	public void initialize(Content a, Content b) {
		buffer = new int[a.length()+1][b.length()+1];
		calculateSubstringsLength(a, b);
	}
	
	int[][] getBuffer() {
		return buffer;
	}
	
	private void calculateSubstringsLength(Content a, Content b) {		
		for (int i=1;i<=a.length();i++) {
		    for (int j=1;j<=b.length();j++) {
		        if (a.getItem(i-1).equals(b.getItem(j-1))) {
		            if (i==1 || j==1) {
		            	buffer[i][j] = 1;
		            }
		            else {
		            	buffer[i][j] = buffer[i-1][j-1]+1;
		            }
		        }
		    }
		}
	}

}
