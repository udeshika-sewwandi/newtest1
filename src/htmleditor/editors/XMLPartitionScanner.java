package htmleditor.editors;

import org.eclipse.jface.text.rules.*;

public class XMLPartitionScanner extends RuleBasedPartitionScanner {
	public final static String HTML_COMMENT = "__html_comment";
	public final static String HTML_TAG = "__html_tag";
	public final static String HTML_DEFAULT = "__html_default";

	public XMLPartitionScanner() {

		IToken htmlComment = new Token(HTML_COMMENT);
		IToken tag = new Token(HTML_TAG);
		IToken defaultHTML = new Token(HTML_DEFAULT);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("<!--", "-->", htmlComment);
		rules[1] = new TagRule(tag);

		setPredicateRules(rules);
	}
}
