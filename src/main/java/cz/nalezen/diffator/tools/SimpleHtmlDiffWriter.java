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

package cz.nalezen.diffator.tools;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import cz.nalezen.diffator.Content;
import cz.nalezen.diffator.DiffEventsHandler;
import cz.nalezen.diffator.utils.IntRange;

/**
 * Sample diff events handler producing HTML into stream.
 * @author Jaroslav Kubos
 *
 */
public class SimpleHtmlDiffWriter implements DiffEventsHandler {
	
	/**
	 * Strategy enum for new lines.
	 * @author Jaroslav Kubos
	 *
	 */
	public enum NewLinesStrategy {
		/**
		 * Do not print any new lines
		 */
		none,
		
		/**
		 * Print new line after each item
		 */
		perItem,
		
		/**
		 * Print new line after each block of common/different content
		 */
		perBlock
	}
	
	private PrintStream out;
	private String title = "diff";
	private NewLinesStrategy newLinesStrategy = NewLinesStrategy.perBlock;
	private String itemsSeparator = " ";
	
	/**
	 * Simple constructor
	 * @param out Stream where will be page printed
	 * @throws UnsupportedEncodingException
	 */
	public SimpleHtmlDiffWriter(OutputStream out) throws UnsupportedEncodingException {
		this.out = new PrintStream(out, true, "utf-8");
	}

	/**
	 * Prints HTML code for events.
	 */
	@Override
	public void handle(Content content, IntRange range, EventType eventType, EventSide eventSide) {
		String style = "";
		boolean print = true;
		
		if (eventType==EventType.DIFFERENT_PART) {
			if (eventSide==EventSide.LEFT) {
				style += "background-color: #c33;";
			} else {
				style += "background-color: #3a3;";
			}
		} else {			
			print = eventSide==EventSide.LEFT;
			style += "background-color: #ccc;";
		}
		
		if (print) {
			out.println("<span style='"+style+"'>");
			
			for (String item : content.rangeIterable(range)) {
				out.println(item+itemsSeparator);
				
				if (newLinesStrategy==NewLinesStrategy.perItem) {
					out.println("<br/>");
				}
			}
			
			out.println("</span>");
			
			if (newLinesStrategy==NewLinesStrategy.perBlock) {
				out.println("<br/>");
			}
		}
	}

	/**
	 * Prints HTML header.
	 */
	@Override
	public void onBeforeStart() {
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset='UTF-8'>");
		out.println("<title>"+getTitle()+"</title>");
		out.println("</head>");
		out.println("<body>");
	}

	/**
	 * Prints HTML footer including similarity value. 
	 */
	@Override
	public void onDone(double similarity) {
		out.println("<br/>");
		out.println("<div>Contents similarity is: "+similarity+"</div>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * Title of page printed as <title>.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Strategy for printing new lines. Default is "NewLinesStrategy.perBlock".
	 * @return
	 */
	public NewLinesStrategy getNewLinesStrategy() {
		return newLinesStrategy;
	}

	public void setNewLinesStrategy(NewLinesStrategy newLinesStrategy) {
		this.newLinesStrategy = newLinesStrategy;
	}

	/**
	 * Separator string for items. By default space - " ".
	 * @return
	 */
	public String getItemsSeparator() {
		return itemsSeparator;
	}

	public void setItemsSeparator(String itemsSeparator) {
		this.itemsSeparator = itemsSeparator;
	}	
}
