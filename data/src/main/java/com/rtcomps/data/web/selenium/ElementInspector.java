package com.rtcomps.data.web.selenium;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;

public class ElementInspector {
	public static final int NOT_FOUND = -1;
	public static final int ELEM_POSITION_NEXT_SIBLING = 123;
	public static final int ELEM_POSITION_PREV_SIBLING = 321;
	public static final int ELEMENT_NODE = 1;
	public static final int TEXT_NODE = 3;
	// no-break-space from one encore1 Risk/Run Trade Status Batch page's button label
	private static String NO_BREAK_SPACE= "\u00a0";
	private static String LABEL_DELIM=":";
	
	private static String VALUE_DELIM ="@#";
	private static String ATTR_DELIM ="__";
	private static String TRIM_CHARS =":";
	private static String[] NON_TRIMMABLE_STRING_LIST = {":"};
	private static String TRIM_ERR = "Label trimming error: label contains only trimming chars - TRIM_CHARS = "+TRIM_CHARS;
	
	public static String trimLabel(String label) {
		if (label == null ) {
			return null;
		}
		String spaceTrimmed = label.replaceAll(NO_BREAK_SPACE, "").trim();
		if (isNontrimmableString(spaceTrimmed)) {
			return spaceTrimmed;
		}
		if ( spaceTrimmed.isEmpty()) {
		    return spaceTrimmed;
		}
		if ( TRIM_CHARS.contains(spaceTrimmed.substring(0, 1)) ) {
			// trim left
			if ( spaceTrimmed.length() < 2) {
				throw new IllegalStateException(TRIM_ERR);
			}
			return trimLabel(spaceTrimmed.substring(1));
		} else {
			int len = spaceTrimmed.length();
			if (TRIM_CHARS.contains(spaceTrimmed.substring(len-1, len)) ) {
				// trim right
				if ( len < 2) {
					throw new IllegalStateException(TRIM_ERR);
				}
				return trimLabel(spaceTrimmed.substring(0,len-1));
			} else {
				return spaceTrimmed;//done
			}
		}
		
	}
	
	private static boolean isNontrimmableString(String str) {
		for (String notrimStr : NON_TRIMMABLE_STRING_LIST) {
			if ( notrimStr.equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	private static String toLowerCase(String str) {
		return (str == null)? null : str.toLowerCase();
	}
	
	public static String toLowerCaseXpathQuery(String value) {
		return "translate(" + value +", 'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')";
	}
	
	public static Optional<WebElement> filterElementByTrimmedLabelOnText(List<WebElement> elements, String trimmedLabel) {
		for (WebElement element : elements) {
			if (equalsEncore1Label(trimmedLabel, getText(element))) {
				return Optional.of(element); // first matched only
			}
		}
		return Optional.absent();
	}
	
	public static String getText(WebElement element) {
		String text = element.getText();
		if (!StringUtils.isEmpty(text)) {
			return text;
		} else {
			//get first child text if any
			Optional<WebElement> firstChild = ElementFinderUtil.find(element, By.xpath(".//*[1]"));
			return firstChild.isPresent() ? firstChild.get().getText() : null;
		}
	}
	
	public static boolean equalsEncore1Label(String label, String rawEncore1Label) {
		if (label == null && rawEncore1Label == null ) {
			return true;
		} else if (label == null && rawEncore1Label !=null) {
			return false;
		} else if ( label != null && rawEncore1Label ==null) {
			return false;
		}
		if (trimLabel(label).equalsIgnoreCase(trimLabel(rawEncore1Label))) {
			return true;
		} else {
			String[] segments = rawEncore1Label.split(LABEL_DELIM);
			String labelRoot = segments[segments.length -1];
			if (trimLabel(label).equalsIgnoreCase(trimLabel(labelRoot))) {
				//live usage: E & A Deliveries > Delivery Settlement > Obligations <TD>Account ID: From:</TD>
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static Optional<WebElement> filterElementByTrimmedLabelOnAttribute(List<WebElement> elements, String trimmedLabel, String attributeName) {
		for (WebElement element : elements) {
			if (equalsEncore1Label(trimmedLabel, element.getAttribute(attributeName))) {
				return Optional.of(element); // first matched only
			}
		}
		return Optional.absent();
	}
	
	public static String serializeElementContent(WebElement elm, JavascriptExecutor js) {
	    return (String) js.executeScript(
	            "var nodes = arguments[0].childNodes;" +
	            "var text = '';" +
	            "for (var i = 0; i < nodes.length; i++) {" +
	            "    if (nodes[i].nodeType == Node.TEXT_NODE) {" +
	            "        text += '" + VALUE_DELIM +"' + nodes[i].textContent;" +
	            "    } else if (nodes[i].nodeType == Node.ELEMENT_NODE ) {" +
	            "        text += '" + VALUE_DELIM +"' + 'ELM__' + nodes[i].tagName +'__' + nodes[i].getAttribute('type') ;" +
	                "}" +
	            "}" +
	            "return text;"
	            , elm);
	}
	
	public static String[] removeEmptyLabels(String[] rawLabels) {
		List<String> filtered = new ArrayList<String>();
		for (String rawLabel : rawLabels) {
			if (!StringUtils.isEmpty(CharMatcher.INVISIBLE.removeFrom(rawLabel).trim())) {
				filtered.add(rawLabel);
			}
		}
		return filtered.toArray(new String[filtered.size()]);
	}
	
    public static String[] splitNodeTextToLabels(String nodeText) {
    	String trimmed = trimLabel(nodeText);
    	return (trimmed.equals(LABEL_DELIM))? new String[]{trimmed} : nodeText.split(LABEL_DELIM);
    }
    
	public static boolean isEmptySelectOnEmptyOptions(Select select, String text) {
		return (select.getOptions().isEmpty() && (text == null || StringUtils.isEmpty(text.trim())))? true : false;
	}
    
    public static String getVisibleText(Select select, String userInput) {
    	List<WebElement> options = select.getOptions();
    	for (WebElement opt: options) {
    		if (equalsEncore1Label(userInput, opt.getText())) {
    			return opt.getText();
    		} else if (userInput == null && StringUtils.isEmpty(opt.getText())) {
    			return opt.getText();
    		}
    	}
    	throw new RuntimeException("Dropdown option not found: " + userInput);
    }
    
	public static int getLabeledChildElementPosition(String serializedNodeContent, String trimmedLabel,  String childTagName, List<String> childTagTypes) {
		ElementContentParser parser = new ElementContentParser();
		return parser.getLabeledChildElementPosition(trimmedLabel, serializedNodeContent, childTagName, childTagTypes);
	}
	
	public static int getLabeledChildElementPosition(String serializedNodeContent, String trimmedLabel,  String childTagName) {
		return getLabeledChildElementPosition(serializedNodeContent, trimmedLabel, childTagName, new ArrayList<String>());
	}
	
	public static Optional<WebElement> getNextSiblingElement(WebElement currentElement) {
		String nextSiblingQuery = ".//following-sibling::*[1]";
		return ElementFinderUtil.find(currentElement, By.xpath(nextSiblingQuery));
	}
	
	public static List<WebElement> getEncore1SectionElements(WebElement sectionHeaderElement) {
		String allTableSiblingsQuery = ".//following-sibling::table";
		String endElementQuery = ".//td[@class='SubHeading']";
		return getSectionBodyElements(sectionHeaderElement, allTableSiblingsQuery, endElementQuery);
	}
	
	public static List<WebElement> getMenuSectionElements(WebElement sectionHeaderElement) {
		String allListSiblingsQuery = ".//following-sibling::li";
		String endElementQuery = ".//span[@class='x-menu-text occ-nav-text']";
		return getSectionBodyElements(sectionHeaderElement, allListSiblingsQuery, endElementQuery);
	}
	
	private static List<WebElement> getSectionBodyElements(WebElement sectionHeader, String bodyElementsQuery, String endElementQuery) {
		List<WebElement> bodyElements = new ArrayList<WebElement>();
		List<WebElement> allCandidateElements = sectionHeader.findElements(By.xpath(bodyElementsQuery));
		for (WebElement table : allCandidateElements) {
			if (isSectionEndElement(table,endElementQuery)) {
				break;
			} else {
				bodyElements.add(table);
			}
		}
		return bodyElements;
	}
	
	private static boolean isSectionEndElement(WebElement element, String endElementQuery) {
		return ElementFinderUtil.find(element, By.xpath(endElementQuery)).isPresent();
	}
	
	public static List<WebElement> waitUntilElementsPresent(WebDriver driver, By locator, long timeoutSecs) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutSecs);
		List<WebElement> allElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return allElements;
	}
	
	public static String getFileElementSystemAbsPath(String filePath) {
		File f = new File(filePath);
		return f.getAbsolutePath();
	}
	
	private static class ElementContentParser {
		
		public int getLabeledChildElementPosition(String label, String serizliedNodeContent, String tagName, List<String> tagTypes) {
			// content list
			NodeContent nodeContent = createNodeContentArray(serizliedNodeContent, label,tagName, tagTypes);
			if ( !nodeContent.isLabelFound()) {
				return NOT_FOUND;
			}
			if ( nodeContent.isLeftLabeling(tagName, tagTypes)) {
				return nodeContent.getLeftLabelingNodePosition(tagName, tagTypes);
			} else {
				return nodeContent.getRightLabelingNodePosition(tagName, tagTypes);
			}
		}
		
		private NodeContent createNodeContentArray(String serizliedNodeContent, String label, String tagName, List<String> tagTypes) {
			int TAG_NAME = 1, TAG_TYPE=2;
			int totalTextNodes = 0, totalMatchedTags =0;
			
			String[] values = serizliedNodeContent.split(VALUE_DELIM);
			List<Node> nonNullNodes = new ArrayList<Node>();
			int labelPosition = 1, elmPosition = 1, nodeIndex=0, firstMatchedLabelIndex = NOT_FOUND;
			for(String val : values) {
				if (!StringUtils.isEmpty(CharMatcher.invisible().removeFrom(val).trim())) {
					String[] parts = val.split(ATTR_DELIM);
					if (parts.length == 1) {
						nonNullNodes.add(new Node(labelPosition,TEXT_NODE,null,null,parts[0]));
						if (trimLabel(parts[0]).equalsIgnoreCase(label) && firstMatchedLabelIndex == NOT_FOUND) {
							// first match found
						   firstMatchedLabelIndex = nodeIndex;
						}
						labelPosition++;
						totalTextNodes++;
					} else {
						Node node = new Node(elmPosition,ELEMENT_NODE,parts[TAG_NAME],parts[TAG_TYPE]);
						nonNullNodes.add(node);
						if (node.isTagOf(tagName, tagTypes)) {
						   totalMatchedTags++;
						}
						elmPosition++;
					}
					nodeIndex++;
				}
			}
			
			return new NodeContent(label,nonNullNodes.toArray(new Node[nonNullNodes.size()]),firstMatchedLabelIndex, totalTextNodes,totalMatchedTags);
		}
	}
	
	private static class Node {

		private int position;
		private int type;
		private String tagName;
		private String tagType;
		
		public Node(int position, int type, String tagName, String tagType) {
			super();
			this.position = position;
			this.type = type;
			this.tagName = tagName;
			this.tagType = tagType;
		}
		
		public Node(int position, int type, String tagName, String tagType,
				String tagValue) {
			this.position = position;
			this.type = type;
			this.tagName = tagName;
			this.tagType = tagType;
		}

		public int getPosition() {
			return position;
		}

		public int getType() {
			return type;
		}

		public String getTagName() {
			return tagName;
		}

		public String getTagType() {
			return tagType;
		}

		public boolean isTagOf(String tagName, List<String> tagTypes) {
			return getTagName().equalsIgnoreCase(tagName) && tagTypes.contains(toLowerCase(getTagType()));
		}

	}
	
	private static class NodeContent {

		private Node[] nodes;
		private int matchedLabelIndex;
		private int totalTextNodes,totalMatchedTags;

		public NodeContent(String label, Node[] nodes, int matchedLabelIndex, int totalTextNodes,int totalMatchedTags) {
			this.nodes = nodes;
			this.matchedLabelIndex = matchedLabelIndex;
			this.totalTextNodes = totalTextNodes;
			this.totalMatchedTags = totalMatchedTags;
		}
		
		public boolean isLabelFound() {
			return matchedLabelIndex != NOT_FOUND;
		}
		
		public boolean areLabelsAndTagsMismatched() {
			return totalTextNodes != totalMatchedTags;
		}
		
		public boolean isLeftLabeling(String tagName, List<String> tagTypes) {
			if ( areLabelsAndTagsMismatched() ) {
				/**
				 * More like an exception than a rule here and only one case so far at page
				 * exchange/processing status/show errs_warnings/filter/To:
				 * 
				 * Note: Do context-sensitive decision if needed later.
				 */
				return true;
			}
			int firstLabelIndex = leftMostLabelIndex();
			int nodeIndex = firstMatchedNodeIndex(tagName,tagTypes);
			if (nodeIndex == NOT_FOUND) {
				return isMatchedLabelInRightMostPosition(tagName,tagTypes);
			}
			return firstLabelIndex < nodeIndex;
		}
		
		public int getLeftLabelingNodePosition(String tagName, List<String> tagTypes) {
			// first node on the right side of the label
			if (isMatchedLabelInRightMostPosition(tagName,tagTypes)) {
				return ELEM_POSITION_NEXT_SIBLING;
			}
			for (int i = matchedLabelIndex; i<nodes.length;i++ ) {
				if (tagTypes.isEmpty()) {
					if (tagName.equalsIgnoreCase(nodes[i].getTagName())) {
						return nodes[i].getPosition();
					} 
				} else {
					if (tagName.equalsIgnoreCase(nodes[i].getTagName())
					    && tagTypes.contains(toLowerCase(nodes[i].getTagType()))) {
						return nodes[i].getPosition();
					} 	
				}
			}
			return NOT_FOUND;
		}
		
        public int getRightLabelingNodePosition(String tagName, List<String> tagTypes) {
			// first node on the left side of the label
        	if (isMatchedLabelInLeftMostPosition(tagName,tagTypes)) {
				return ELEM_POSITION_PREV_SIBLING;
			}
			for (int i = matchedLabelIndex-1; i>=0; i--) {
				if (tagTypes.isEmpty()) {
					if (tagName.equalsIgnoreCase(nodes[i].getTagName())) {
						return nodes[i].getPosition();
					} 
				} else {
					if (tagName.equalsIgnoreCase(nodes[i].getTagName())
					    && tagTypes.contains(toLowerCase(nodes[i].getTagType()))) {
						return nodes[i].getPosition();
					} 	
				}
			}
			return NOT_FOUND;
		}
        
        private boolean isMatchedLabelInRightMostPosition(String tagName,List<String> tagTypes) {
			int lastLabelIndex = rightMostLabelIndex();
			int nodeIndex = firstMatchedNodeIndex(tagName,tagTypes);
			return nodeIndex == NOT_FOUND && lastLabelIndex == matchedLabelIndex;
        }
        
        private boolean isMatchedLabelInLeftMostPosition(String tagName,List<String> tagTypes) {
			int firstLabelIndex = leftMostLabelIndex();
			int nodeIndex = firstMatchedNodeIndex(tagName,tagTypes);
			return nodeIndex == NOT_FOUND && firstLabelIndex == matchedLabelIndex;
        }

		private int leftMostLabelIndex() {
			for (int i =0;i<nodes.length;i++) {
				if (nodes[i].getType() == TEXT_NODE ) {
					return i;
				}
			}
			return NOT_FOUND;
		}
		
		private int rightMostLabelIndex() {
			for (int i =nodes.length-1;i>=0;i--) {
				if (nodes[i].getType() == TEXT_NODE ) {
					return i;
				}
			}
			return NOT_FOUND;
		}
		
		private int firstMatchedNodeIndex(String tagName, List<String> tagTypes) {
			for (int i=0;i<nodes.length;i++) {
				if (tagTypes.isEmpty()) {
					if (tagName.equalsIgnoreCase(nodes[i].getTagName())) {
						return i;
					} 
				} else {
					if (tagName.equalsIgnoreCase(nodes[i].getTagName())
					    && tagTypes.contains(toLowerCase(nodes[i].getTagType()))) {
						return i;
					} 	
				}
			}
			return NOT_FOUND;
		}

	}
}
