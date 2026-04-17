package utils;

import ui.SaveAsUi;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class TextHandler {
    private static String getFileName(String prompt) {
        String input = JOptionPane.showInputDialog(null, prompt);

        if (input == null) {
            JOptionPane.showMessageDialog(null, "Erro: Operação cancelada pelo usuário.");
            return null;
        }

        String fileName = input.trim();
        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erro: Nenhum nome de arquivo foi digitado.");
            return null;
        }

        return fileName.replaceAll("\\..*", "");
    }

    private static File findByNameIgnoringExtension(String baseName) {
        File currentDir = new File(".");
        File[] matches = currentDir.listFiles((dir, name) -> {
            int lastDot = name.lastIndexOf('.');
            String nameWithoutExtension = (lastDot > 0) ? name.substring(0, lastDot) : name;
            return nameWithoutExtension.equalsIgnoreCase(baseName);
        });

        if (matches == null || matches.length == 0) {
            return null;
        }

        return matches[0];
    }

    public static String open() {
        try {
            String baseName = getFileName("Qual é o nome do arquivo que você deseja abrir?");
            if (baseName == null) {
                return null;
            }

            File fileToOpen = findByNameIgnoringExtension(baseName);
            if (fileToOpen == null) {
                JOptionPane.showMessageDialog(null, "Erro: não foi possível encontrar um arquivo com esse nome.");
                return null;
            }

            FileInputStream in = new FileInputStream(fileToOpen);
            Scanner scanner = new Scanner(in);
            StringBuilder txt = new StringBuilder();

            while (scanner.hasNextLine()) {
                txt.append(scanner.nextLine());
                txt.append("\n");
            }
            in.close();
            scanner.close();

            return txt.toString();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage());
        }

        return null;
    }

    public static void save(String text) {
        try {
            String fileName = getFileName("Qual nome deseja dar ao arquivo? (Será salvo como .txt)");
            if (fileName == null) {
                return;
            }
            fileName = fileName + ".txt";

            FileOutputStream out = new FileOutputStream(fileName);

            out.write(text.getBytes());
            out.close();

            JOptionPane.showMessageDialog(null, "Texto salvo com sucesso em " + fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro: não foi possível escrever no arquivo.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage());
        }
    }

    public static void saveAs(String text) {
        try {
            String extension = SaveAsUi.askExtension(null);
            if (extension == null) {
                JOptionPane.showMessageDialog(null, "Erro: Operação cancelada pelo usuário.");
                return;
            }

            String fileName = getFileName("Qual nome deseja dar ao arquivo?");
            if (fileName == null) {
                return;
            }
            fileName = fileName + extension;

            FileOutputStream out = new FileOutputStream(fileName);

            out.write(text.getBytes());
            out.close();

            JOptionPane.showMessageDialog(null, "Texto salvo com sucesso em " + fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro: não foi possível escrever no arquivo.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage());
        }
    }
}