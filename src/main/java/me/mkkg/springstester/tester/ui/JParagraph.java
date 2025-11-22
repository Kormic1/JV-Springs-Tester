package main.java.me.mkkg.springstester.tester.ui;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JParagraph extends JEditorPane {

    public JParagraph(String htmlText) {
        setContentType("text/html");
        setEditable(false);
        setBorder(null);
        setText(wrapHtml(htmlText));
        setCaretColor(getBackground());

        // Link click listener - sends to set URL
        addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                } catch (IOException | URISyntaxException ex) {
                    //noinspection CallToPrintStackTrace
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setBackground(Color bgColor) {
        super.setBackground(bgColor);
        setCaretColor(bgColor); // Keep caret hidden
    }

    private String wrapHtml(String body) {
        if (body.strip().toLowerCase().startsWith("<html>")) return body;

        return """
            <html>
              <body style='
                font-family: Monospaced;
                font-size: 14pt;
                text-align: justify;
                margin: 0;
                padding: 0;
              '>
                %s
              </body>
            </html>
        """.formatted(body.replace("\n", "<br>"));
    }
}