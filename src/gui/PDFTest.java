package gui;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PDFTest extends JComponent {

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame();
        JLabel label = new JLabel(getPDFPreview("document.pdf"));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }

    public static ImageIcon getPDFPreview(String path) throws Exception {
        PDFPage page = new PDFFile(ByteBuffer.wrap(Files.readAllBytes(Paths.get(path)))).getPage(0);
        Rectangle rect = new Rectangle(0, 0, (int) page.getWidth(), (int) page.getHeight());
        Image image = page.getImage(rect.width, rect.height, rect, null, true, true);
        return new ImageIcon(image.getScaledInstance(rect.width / 4, rect.height / 4, Image.SCALE_SMOOTH));
    }
}
