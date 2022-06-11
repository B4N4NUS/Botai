import com.formdev.flatlaf.FlatDarculaLaf;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

public class GUI extends JFrame {
    ArrayList<String> questions;
    ArrayList<String> answers;
    JTextArea text;
    TextButton current, next;
    boolean showAns = false;
    int currentInd = 0;

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(new File("src/main/resources/data.xls"));
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

            GUI gui = new GUI(questions, answers);

            fis.close();
            formulaEvaluator.clearAllCachedResultValues();
            wb.close();
        } catch (Exception exception) {
            System.out.println("Cant get data from xls");
        }
    }

    public GUI(ArrayList<String> questions, ArrayList<String> answers) {
        this.answers = answers;
        this.questions = questions;

        build();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
