import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TextButton extends JButton {
    public String text;

    public TextButton() {

    }

    @Override
    public void paintComponent(Graphics g) {
        if (ui != null) {
            ColorUIResource color = (ColorUIResource) UIManager.get("ComboBox.background");
            ColorUIResource fontColor = (ColorUIResource) UIManager.get("ComboBox.foreground");
            Graphics scratchGraphics = (g == null) ? null : g.create();
            try {
                Font font = getFont();
                assert g != null;
                FontMetrics fm = g.getFontMetrics();
                int width = fm.stringWidth(text);
                int height = fm.getHeight();

                Graphics2D g2 = (Graphics2D) g.create();
                RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHints(qualityHints);

                g2.setPaint(new GradientPaint(new Point(0, 0), new Color(color.getRed(), color.getGreen(),
                        color.getBlue()), new Point(0, getHeight()), new Color(color.getRed(), color.getGreen(),
                        color.getBlue())));
                g2.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, 20, 20);

                g2.setPaint(new GradientPaint(new Point(0, 0), new Color(fontColor.getRed(), fontColor.getGreen(),
                        fontColor.getBlue()), new Point(0, getHeight()), new Color(fontColor.getRed(), fontColor.getGreen(),
                        fontColor.getBlue())));

                JTextArea area = new JTextArea(text);
                area.paintComponents(g);

                String[] data = text.split("\n");
                ArrayList<String> realData = new ArrayList<>();
                for(int i = 0; i < data.length; i++) {
                    String aboba = "";
                    if (fm.stringWidth(data[i])+20 > getWidth()) {
                        while (fm.stringWidth(data[i])+20 > getWidth()) {
                            if (fm.stringWidth(aboba)+20 > getWidth()) {
                                realData.add(aboba);
                                aboba = "";
                            }
                            aboba += data[i].charAt(0);
                            data[i] = data[i].substring(1);
                        }
                        realData.add(aboba);
                        realData.add(data[i]);
                    } else {
                        realData.add(data[i]);
                    }
                }

                for(int i = 0; i < realData.size(); i++) {
                    g2.drawString(realData.get(i), 5, height*i + height);
                }
//                for(int i = 0; i < data.length; i++) {
//                    System.out.println(data[i]);
//                }


                g2.dispose();
                //System.out.println("sosi");
                //ui.update(scratchGraphics, this);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                assert scratchGraphics != null;
                scratchGraphics.dispose();
            }
        }
    }
}
