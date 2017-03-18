package htmleditor.editors;

import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public class HTMLEditor extends TextEditor {
	private ColorManager colorManager;
	private XMLDocumentProvider documentProvider;
	private static final String CONTENTASSIST_PROPOSAL_ID = 
			"newhtmleditor.editors.HTMLEditor.ContentAssistProposal";

	public HTMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	
	@Override
	public void createActions(){
		super.createActions();
		ResourceBundle bundle = 
				ResourceBundle.getBundle("htmleditor.editors.HTMLEditorMessages");
	   
	   IAction action = new ContentAssistAction(bundle, "ContentAssistProposal.", this); 
       action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
       setAction(CONTENTASSIST_PROPOSAL_ID, action);
       setActionActivationCode(CONTENTASSIST_PROPOSAL_ID,' ', -1, SWT.CTRL);
	}
	
/*	public XMLDocumentProvider getDocumentProvider() {
		return documentProvider;
	}*/
	
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}


}
