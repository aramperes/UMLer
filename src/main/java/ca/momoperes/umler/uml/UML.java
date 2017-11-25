package ca.momoperes.umler.uml;

import ca.momoperes.umler.Main;
import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;

import java.io.File;
import java.util.Collection;

public interface UML {
    void load(File file) throws Exception;

    String getClassName();

    Collection<UMLField> getFields();

    Collection<UMLConstructor> getConstructors();

    Collection<UMLMethod> getMethods();

    static UML forFile(File file) {
        if (file.getName().toLowerCase().endsWith(".java")) {
            return new JavaSourceUML();
        }
        return null;
    }

    static void exportToWord(File target, UML uml) throws Exception {
        MainDocumentPart document = Main.pkg.getMainDocumentPart();
        document.getContent().clear();
        document.addParagraphOfText("Class: " + uml.getClassName());
        document.addParagraphOfText("");
        for (UMLField field : uml.getFields()) {
            document.addObject(genParagraph(field.toString(), field.isStatic()));
        }
        for (UMLConstructor constructor : uml.getConstructors()) {
            document.addParagraphOfText(constructor.toString());
        }
        for (UMLMethod method : uml.getMethods()) {
            document.addObject(genParagraph(method.toString(), method.isStatic()));
        }
        Docx4J.save(Main.pkg, target);
    }

    static P genParagraph(String content, boolean underline) throws Exception {
        String openXML = "<w:p xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                + "<w:pPr>"
                + "<w:rPr>"
                + "{UNDERLINE}"
                + "</w:rPr>"
                + "</w:pPr>"
                + "<w:r>"
                + "<w:rPr>"
                + "{UNDERLINE}"
                + "</w:rPr>"
                + "<w:t>" + content + "</w:t>"
                + "</w:r>"
                + "</w:p>";

        openXML = openXML.replace("{UNDERLINE}", underline ? "<w:u w:val=\"single\"/>" : "");
        return (P) XmlUtils.unmarshalString(openXML);
    }
}
