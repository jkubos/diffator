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

/**
 * Simple holder of two ranges - semantic is given by usage in diff. Depicts two ranges of data in two Contents.
 * @author Jaroslav Kubos
 *
 */
public class Overlap {		
	private IntRange a = new IntRange();
	private IntRange b = new IntRange();
	
	public Overlap() {
	}
	
	/**
	 * Get first range.
	 * @return
	 */
	public IntRange getA() {
		return a;
	}
	
	/**
	 * Get second range.
	 * @return
	 */
	public IntRange getB() {
		return b;
	}
	
	/**
	 * Clean both ranges.
	 */
	public void clean() {
		a.clean();
		b.clean();
	}
}