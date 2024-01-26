package tesi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainGUI extends JFrame{
    int initActions;
    int initPredicates;
    private String domain;
    private String problem;
    public MainGUI() throws IOException {
        // Crea il frame
        setTitle("Euristiche di Pruning");
        // Crea il pannello con il gestore del trascinamento del file
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        //panel.setLayout(new GridLayout(1,2));
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        //p1.setLayout(new BorderLayout());
        //p2.setLayout(new BorderLayout());
        /*
        ImageIcon imageIcon1 = setImage("src/main/java/GUITesi/domain.png");
        imageIcon1.setDescription("Domain");
        ImageIcon imageIcon2 = setImage("src/main/java/GUITesi/problem.png");
        imageIcon2.setDescription("Problem");
        */
        JLabel l1 = new JLabel("Rilasciare domain qui");
        JLabel l2 = new JLabel("Rilasciare problem qui");
        l1.setSize(100,100);
        l2.setSize(100,100);
        p1.setBorder(new LineBorder(Color.BLACK));
        p2.setBorder(new LineBorder(Color.BLACK));
        p1.setSize(100,100);
        p2.setSize(100,100);
        p1.setVisible(true);
        p2.setVisible(true);
        p1.add(l1,BorderLayout.CENTER);
        p2.add(l2,BorderLayout.CENTER);

        // Imposta il TransferHandler per gestire il trascinamento dei file
         p1.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport info) {
                // Accetta solo file
                return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport info) {
                if (!canImport(info)) {
                    return false;
                }

                try {
                    // Estrai la lista dei file trascinati
                    Transferable t = info.getTransferable();
                    java.util.List<File> fileList = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                    // Processa i file trascinati come preferisci
                    for (File file : fileList) {
                        System.out.println("File trascinato: " + file.getAbsolutePath());
                        domain = file.getAbsolutePath();
                        // Aggiungi qui il codice per fare qualcosa con il file
                        p1.setBackground(Color.GREEN);
                    }

                    return true;
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
        p2.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport info) {
                // Accetta solo file
                return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport info) {
                if (!canImport(info)) {
                    return false;
                }

                try {
                    // Estrai la lista dei file trascinati
                    Transferable t = info.getTransferable();
                    java.util.List<File> fileList = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

                    // Processa i file trascinati come preferisci
                    for (File file : fileList) {
                        System.out.println("File trascinato: " + file.getAbsolutePath());
                        problem = file.getAbsolutePath();
                        // Aggiungi qui il codice per fare qualcosa con il file
                        p2.setBackground(Color.GREEN);
                    }

                    return true;
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
        grid.add(p1);
        grid.add(p2);
        JButton button = new JButton("Inizia");
        JLabel label = new JLabel("Trascina i file per iniziare");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(domain==null || problem == null) {
                    label.setText("Inserisci i file");
                    panel.validate();
                }else if(!problem.contains(".pddl") || !domain.contains(".pddl")){
                    label.setText("Formato errato");
                    panel.validate();
                }else{
                    try {
                        ParserInPDDL parser = new ParserInPDDL();
                        Domain domain1 = parser.parseDomain(domain);
                        Problem problem1 = ParserInPDDL.parseProblem(problem);
                        initActions = domain1.getActions().size();
                        initPredicates = domain1.getPredicates().size();

                        IntermediateModel<Action, ObjectPDDL, Predicate> model = new IntermediateModel<Action, ObjectPDDL, Predicate>(domain1.getActions(), problem1.getObjects2(), domain1.getPredicates(), problem1.getInitialState(), problem1.getGoalState());
                        Resolution<Predicate,ObjectPDDL, Action> resolution = new Resolution<Predicate,ObjectPDDL, Action>(model);
                        resolution.init();

                        int finalActions = domain1.getActions().size();
                        int finalPredicates = domain1.getPredicates().size();
                        ResultGUI gui = new ResultGUI(initPredicates-finalPredicates, initActions-finalActions);
                        ParserOutPDDL parserOut = new ParserOutPDDL(domain1.getName());
                        ParserOutPDDL.writeProblemFile("finalProblem.pddl", problem1);
                        ParserOutPDDL.writeDomainFile("finalDomain.pddl", domain1);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    dispose();

                }
            }
        });


        panel.add(label, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);
        // Aggiungi il pannello al frame
        add(panel);

        // Rendi visibile il frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setVisible(true);
    }

    public ImageIcon setImage(String filename) throws IOException {
        Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource(filename))).getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        return new ImageIcon(resizedImage);
    }

    public String getDomain() {
        return domain;
    }

    public String getProblem() {
        return problem;
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new MainGUI();
    }
    private static class ResultGUI extends JFrame{
        ResultGUI(int pr, int ac){
            JLabel l1 = new JLabel("Azioni rimosse:",SwingConstants.CENTER);
            JLabel l2 = new JLabel("Predicati rimossi:",SwingConstants.CENTER);
            JLabel l3 = new JLabel(""+ac, SwingConstants.CENTER);
            JLabel l4 = new JLabel(""+pr,SwingConstants.CENTER);
            JPanel panel = new JPanel(new GridLayout(2,2));
            panel.add(l1);
            panel.add(l2);
            panel.add(l3);
            panel.add(l4);
            add(panel);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 100);
            setVisible(true);
        }
    }
}

