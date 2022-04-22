package in.apcfss.struts.Utilities;

	import java.io.*;
	import java.util.*;
	import org.xml.sax.*;
	import javax.xml.parsers.*;
	import javax.xml.transform.*;
	import org.xml.sax.helpers.*;
	import javax.xml.transform.sax.*;
	import javax.xml.transform.stream.*;
	import com.lowagie.text.*;
	import com.lowagie.text.pdf.*;

	public class PdfToXml {
		static StreamResult streamResult;
		static TransformerHandler handler;
		static AttributesImpl atts;

		public static void main(String[] args) throws IOException {

			try {
				Document document = new Document();
				document.open();
				PdfReader reader = new PdfReader("E:\\HighCourtsCauseList\\APHC012022-02-0925921001.pdf");
				PdfDictionary page = reader.getPageN(1);
				PRIndirectReference objectReference = (PRIndirectReference) page.get(PdfName.CONTENTS);
				PRStream stream = (PRStream) PdfReader
						.getPdfObject(objectReference);
				byte[] streamBytes = PdfReader.getStreamBytes(stream);
				PRTokeniser tokenizer = new PRTokeniser(streamBytes);

				StringBuffer strbufe = new StringBuffer();
				while (tokenizer.nextToken()) {
					if (tokenizer.getTokenType() == PRTokeniser.TK_STRING) {
						strbufe.append(tokenizer.getStringValue());
					}
				}
				String test = strbufe.toString();
				streamResult = new StreamResult("E:\\HighCourtsCauseList\\data.xml");
				initXML();
				process(test);
				closeXML();
				document.add(new Paragraph(".."));
				document.close();
			} catch (Exception e) {
			}
		}

		public static void initXML() throws ParserConfigurationException,
				TransformerConfigurationException, SAXException {
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
					.newInstance();

			handler = tf.newTransformerHandler();
			Transformer serializer = handler.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			serializer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			handler.setResult(streamResult);
			handler.startDocument();
			atts = new AttributesImpl();
			handler.startElement("", "", "Roseindia", atts);
		}

		public static void process(String s) throws SAXException {
			String[] elements = s.split("\\|");
			atts.clear();
			handler.startElement("", "", "Message", atts);
			handler.characters(elements[0].toCharArray(), 0, elements[0].length());
			handler.endElement("", "", "Message");
		}

		public static void closeXML() throws SAXException {
			handler.endElement("", "", "Roseindia");
			handler.endDocument();
		}
	}
