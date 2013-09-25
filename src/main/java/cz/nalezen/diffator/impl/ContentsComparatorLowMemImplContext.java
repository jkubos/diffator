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
 * Data for LowMem impl.
 * @author Jaroslav Kubos
 *
 */
public class ContentsComparatorLowMemImplContext implements ContentsComparatorContext {

	int[][] buffer;
	
	int activeBufferIndex;
	
	@Override
	public void initialize(Content a, Content b) {
		buffer = new int[2][b.length()];
	}
	
	public int[] getActiveBuffer() {
		return buffer[activeBufferIndex];
	}
	
	public int[] getInactiveBuffer() {
		return buffer[otherBufferIndex()];
	}

	public void swapBuffers() {
		activeBufferIndex = otherBufferIndex();
	}
	
	private int otherBufferIndex() {
		return (activeBufferIndex+1)%2;
	}

}
