package htmleditor.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Point;

public class HTMLContentAssistantProcessor implements IContentAssistProcessor{

	private final static String[] PROPTAGS1 =
			   new String[] { "<html>","<head>","<title>","<body>","<div>","<table>","<tr>","<td>","<ol>","<ul>","<a src=\">" };
	
	private final static String[] PROPTAGS2 =
			new String[] { "</html>","</head>","</title>","</body>","</div>","</table>","</tr>","</td>","</ol>","</ul>","\"></a>" };
	
	private final static String[] STYLETAGS =
			new String[]{"h1","h2","h3","h4","h5","h6","b","i","s","p"};
	
	private final static String[] STYLELABELS =
			new String[]{"height1","height2","height3","height4","height5","height6","bold","italic","strong","paragraph"};
	
	private int offsetIncrement;
	
	public HTMLContentAssistantProcessor() {
		offsetIncrement = 0;
	}
	
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		
		Point selectedRange = viewer.getSelectedRange();
		List<ICompletionProposal> proposalList = new ArrayList<ICompletionProposal>();
		
		if (selectedRange.y > 0) {			
			try {
				String currentText = document.get(selectedRange.x, selectedRange.y);
				
				computeStyleProposals(currentText,offset,proposalList);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		} else {
			String qualifier = getQualifier(document, offset);
			 
			  // Compute completion proposals
			  computeContentProposals(qualifier, offset, proposalList);
			}
		 // Create completion proposal array
	    ICompletionProposal[] proposals = new ICompletionProposal[proposalList.size()];
	 
	   // and fill with list elements
	    proposalList.toArray(proposals);
	 
	   // Return the proposals
	    return proposals; 
	}

	private void computeStyleProposals(String currentText, int offset,
			List<ICompletionProposal> proposalList) {
		
		int currentStrLen = currentText.length();
		
		for(int i=0; i< STYLETAGS.length ;i++){
			String styleTag = STYLETAGS[i]; 
			
			String newReplacementText = "<"+styleTag+">" + currentText + "</"+styleTag+">";
			
			int cursorPosition = styleTag.length()*2+styleTag.length();
			// Compute a suitable context information
		    IContextInformation contextInfo = 
		         new ContextInformation(null, STYLELABELS[i]+" Style");
			
		    CompletionProposal completionProposal = new CompletionProposal(newReplacementText, offset , currentStrLen, cursorPosition);
			
		    proposalList.add(completionProposal);
		}
		
	}

	private void computeContentProposals(String str, int offset, List<ICompletionProposal> propList) {
		int strLen = str.length();
		
		for(int i=0; i< PROPTAGS1.length; i++){
			String startProp = PROPTAGS1[i];
			
			if(startProp.startsWith(str)){
				String text = startProp + PROPTAGS2[i];
				
				int cursor = startProp.length();
				
				CompletionProposal completionProp = new CompletionProposal(text, offset - strLen, strLen , cursor);
				
				propList.add(completionProp);
				offsetIncrement=0;
			}			
		}	
		
	}

	private String getQualifier(IDocument document, int offset) {
		// Use string buffer to collect characters
		   StringBuffer buf = new StringBuffer();
		   int i=0, offsetVal=offset;
		   
		  /* while(i==0){
			   try {
				   //read characters forwards
				char character = document.getChar(offsetVal++);
				offsetIncrement++;
				
				//stops when '>' found
				if(character == '>'){
					break;
				}
				
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			   
		   }*/
		   
		   while (true) {
		     try {
		 
		       // Read character backwards
		       char c = document.getChar(--offset);
		 
		       // This was not the start of a tag
		       if (c == '>' || Character.isWhitespace(c))
		          return "";
		 
		       // Collect character
		       buf.append(c);
		 
		       // Start of tag. Return qualifier
		       if (c == '<')
		           return buf.reverse().toString();
		 
		     } catch (BadLocationException e) {
		 
		       // Document start reached, no tag found
		       return "";
		     }
		   }
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}


}
