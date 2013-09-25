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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cz.nalezen.diffator.utils.IntRange;

/**
 * Class wrapping content - basically array of Strings. Strings may be words, lines of file, blocks of text, ...
 * @author Jaroslav Kubos
 *
 */
public class Content {

	private String[] items;
	private IntRange range;
	private String contentId;
	
	/**
	 * Construct content by keeping reference do delivered items array. Memory efficient but keep in mind not to change items array during Content lifespan.
	 * @param items items of content (words/lines/blocks)
	 */
	public Content(String[] items) {
		initialize(items);
	}
	
	/**
	 * Constructs content from List. This is expensive method because it instantiate String array of the same size and copy content.
	 * @param items items of content (words/lines/blocks)
	 */
	public Content(List<String> items) {
		String[] wordsArr = new String[items.size()];
		items.toArray(wordsArr);
		
		initialize(wordsArr);
	}
	
	/**
	 * Internal initialization
	 * @param items
	 */
	private void initialize(String[] items) {
		this.items = items;
		
		range = new IntRange(0, items.length);
	}
	
	/**
	 * Standard equals operation, compares arrays using java.util.Arrays.equals() method.
	 */
	@Override
	public boolean equals(Object other) {
		Content otherContent = (Content)other;
		
		return Arrays.equals(items, otherContent.items);
	}
	
	/**
	 * Content may be marked by some ID for external purposes.
	 * @return
	 */
	public String getContentId() {
		return contentId;
	}

	/**
	 * Set some ID to this content.
	 * @param contentId
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	/**
	 * Get internal representation of items array.
	 * @return
	 */
	public String[] getItems() {
		return items;
	}
	
	/**
	 * Range of this Content, 0<=X<N
	 * @return 
	 */
	public IntRange getRange() {
		return range;
	}

	/**
	 * Access to items by index.
	 * @param index index in items
	 * @return
	 */
	public String getItem(int index) {
		return items[index];
	}
	
	/**
	 * Get number of items.
	 * @return
	 */
	public int length() {
		return items.length;
	}
	
	/**
	 * Iterable for simple usage in foreach.
	 * @param range
	 * @return
	 */
	public Iterable<String> rangeIterable(final IntRange range) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return new ContentIterator(Content.this, range);
			}
		};
	}
	
	/**
	 * Debug print of whole Content to stdout
	 */
	public void print() {
		print(getRange());
	}
	
	/**
	 * Debug print of just range from Content 
	 * @param range range within Content
	 */
	public void print(IntRange range) {
		System.out.println("-------------[length="+length()+", displayed length="+range.length()+" id="+contentId+"]-------------");
		
		int counter = 0;
		
		for (String s : rangeIterable(range)) {
			System.out.print(s+" ");
			
			if (counter>0 && counter%10==0) {
				System.out.println();
			}
		}
		
		System.out.println();
	}
}
