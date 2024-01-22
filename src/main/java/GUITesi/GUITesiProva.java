package GUITesi;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GUITesiProva extends JFrame{
    private String domain;
    private String problem;
    public GUITesiProva() throws IOException {
        // Crea il frame

        // Crea il pannello con il gestore del trascinamento del file
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        JPanel grid = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p1.setLayout(new BorderLayout());
        p2.setLayout(new BorderLayout());
        ImageIcon imageIcon1 = setImage("domain.png");
        imageIcon1.setDescription("Domain");
        ImageIcon imageIcon2 = setImage("problem.png");
        imageIcon2.setDescription("Problem");
        JLabel l1 = new JLabel(imageIcon1);
        JLabel l2 = new JLabel(imageIcon2);
        p1.setBorder(new LineBorder(Color.BLACK));
        p2.setBorder(new LineBorder(Color.BLACK));
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
        JLabel label = new JLabel("Trascina file di domain e problem negli appositi spazi");
        panel.add(label);
        panel.add(grid);

        // Aggiungi il pannello al frame
        add(panel);

        // Rendi visibile il frame
        setVisible(true);
    }

    public ImageIcon setImage(String filename) throws IOException {
        Image image = new ImageIcon(Objects.requireNonNull(getClass().getResource(filename))).getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        return new ImageIcon(resizedImage);
    }

    public static void main(String[] args) throws IOException{
        JFrame frame = new GUITesiProva();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
    }
}

