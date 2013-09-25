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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cz.nalezen.diffator.Content;

/**
 * Data for HashImpl.
 * @author Jaroslav Kubos
 *
 */
public class ContentsComparatorHashImplContext implements ContentsComparatorContext {

	ArrayList<LinkedHashMap<Integer, Integer>> buffer;
	
	@Override
	public void initialize(Content a, Content b) {
		buffer = new ArrayList<>(a.length());
		
		//define all rows
		for (int i=0;i<a.length()+1;++i) {
			buffer.add(new LinkedHashMap<Integer, Integer>());
		}
		
		//build matrix
		calculateSubstringsLength(a, b);
	}
	
	public ArrayList<LinkedHashMap<Integer, Integer>> getBuffer() {
		return buffer;
	}
	
	/**
	 * http://en.wikibooks.org/w/index.php?title=Algorithm_Implementation/Strings/Longest_common_substring&stable=1
	 * only non-zero values stored
	 */
	private void calculateSubstringsLength(Content a, Content b) {		
		for (int i=1;i<=a.length();i++) {
		    for (int j=1;j<=b.length();j++) {
		        if (a.getItem(i-1).equals(b.getItem(j-1))) {
		        	
		        	LinkedHashMap<Integer, Integer> row = buffer.get(i);
		
		            if (i==1 || j==1) {
		            	row.put(j, 1);
		            }
		            else {
		            	LinkedHashMap<Integer, Integer> rowBef = buffer.get(i-1);
		            	
		            	Integer v = rowBef.get(j-1);
		            	
		            	if (v==null) {
		            		v = 0;
		            	}
		            	
		            	row.put(j, v+1);
		            }
		        }
		    }
		}	    
	}

}
