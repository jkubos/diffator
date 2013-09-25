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

import cz.nalezen.diffator.utils.IntRange;

/**
 * Callback handler for two-way diff.
 * @author Jaroslav Kubos
 *
 */
public interface DiffEventsHandler {
	
	/**
	 * Type of reported event - same part or difference?
	 * @author Jaroslav Kubos
	 *
	 */
	public enum EventType {
		COMMON_PART,
		DIFFERENT_PART
	}
	
	/**
	 * Describes Content on which event is reported - for two way diff there is left and right side. 
	 * @author Jaroslav Kubos
	 *
	 */
	public enum EventSide {
		LEFT,
		RIGHT
	}
	
	/**
	 * Callback before diff starts.
	 */
	void onBeforeStart();
	
	/**
	 * Callback when diff is node
	 * @param similarity range 0<=N<=1. Zero means no intersection, one means complete same contents. Value basically means 
	 * "how big part of bigger Content is covered by smaller one."
	 */
	void onDone(double similarity);
	
	/**
	 * All diff events are reported by this method.
	 * @param content reported content
	 * @param range reported range of content
	 * @param eventType common or different part?
	 * @param eventSide left or right Content used for diff?
	 */
	void handle(Content content, IntRange range, EventType eventType, EventSide eventSide);
}
