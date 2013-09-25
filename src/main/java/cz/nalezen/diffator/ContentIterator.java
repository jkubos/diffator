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

import java.util.Iterator;

import cz.nalezen.diffator.utils.IntRange;

/**
 * Lightweight iterator of Content.
 * @author Jaroslav Kubos
 *
 */
public class ContentIterator implements Iterator<String> {

	private Content content;
	private IntRange range;
	private int actIndex;

	/**
	 * Constructs iterator.
	 * @param content iterated content
	 * @param range range within content
	 */
	ContentIterator(Content content, IntRange range) {
		this.content = content;
		this.range = range;
		actIndex = range.getBegin();
	}
	
	@Override
	public boolean hasNext() {
		return actIndex<range.getEnd();
	}

	@Override
	public String next() {
		return content.getItem(actIndex++);
	}

	/**
	 * Not supported as in the given semantic does not make a sense.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
