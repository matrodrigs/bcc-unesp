import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.util.Locale;

public class HardwoodSpecies {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        if (!scanner.hasNextLine()) return;

        int testCases = Integer.parseInt(scanner.nextLine().trim());
        scanner.nextLine(); 
        
        for (int i = 0; i < testCases; i++) {
            TreeMap<String, Integer> speciesCount = new TreeMap<>();
            int totalTrees = 0;
            
            while (scanner.hasNextLine()) {
                String species = scanner.nextLine();
                
                if (species.isEmpty()) {
                    break; 
                }
                
                speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
                totalTrees++;
            }
            
            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                double percentage = (entry.getValue() * 100.0) / totalTrees;
                
                System.out.printf(Locale.US, "%s %.4f\n", entry.getKey(), percentage);
            }
            
            if (i < testCases - 1) {
                System.out.println();
            }
        }
        
        scanner.close();
    }
}