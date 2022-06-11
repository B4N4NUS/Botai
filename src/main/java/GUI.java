import com.formdev.flatlaf.FlatDarculaLaf;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

public class GUI extends JFrame {
    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    TextButton current, next;
    boolean showAns = false;
    int currentInd = 0;

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        GUI gui = new GUI();
    }

    public GUI() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Pick xls file");
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION ) {
                FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
                HSSFWorkbook wb = new HSSFWorkbook(fis);
                HSSFSheet sheet = wb.getSheetAt(0);
                FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
                for (Row row : sheet) {
                    try {
                        questions.add(row.getCell(0).getStringCellValue());
                        answers.add(row.getCell(1).getStringCellValue());
                    } catch (Exception ex) {
                        break;
                    }
                }

                fis.close();
                formulaEvaluator.clearAllCachedResultValues();
                wb.close();
            } else {
                throw new Exception("Cant get file");
            }

            build();
            setVisible(true);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // System.out.println("Cant get data from xls");
            dispose();
            System.exit(0);
        }



    }

    private void build() {
        Random rnd = new Random();
        currentInd = rnd.nextInt(0, questions.size());
        setTitle("Question - "+(currentInd+1));
        setSize(new Dimension(600,500));
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.weighty = 1;

        add(current = new TextButton(), cons);
        current.text = questions.get(currentInd);
        current.setMinimumSize(new Dimension(600,400));
        current.addActionListener(e-> {
            if (showAns) {
                showAns = false;
                current.text = questions.get(currentInd);
            } else {
                showAns = true;
                current.text = answers.get(currentInd);
            }
            setTitle("Question - "+(currentInd+1));
            repaint();
        });
        cons.gridx++;
        cons.weightx = 1;
        cons.weighty = 0.1;
        add(next = new TextButton(), cons);
        next.text = "Next";
        next.addActionListener(e-> {
            showAns = false;
            currentInd = rnd.nextInt(0,questions.size());
            current.text = questions.get(currentInd);
            setTitle("Question - "+(currentInd+1));
            repaint();
        });
       // pack();
    }

    @Override
    public void dispose() {
        super.dispose();
        System.exit(0);
    }
}
