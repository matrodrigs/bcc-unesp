import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.util.Locale;

public class HardwoodSpecies {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // If the input is empty, exit
        if (!scanner.hasNextLine()) return;

        // Read the number of test cases
        int testCases = Integer.parseInt(scanner.nextLine().trim());
        
        // Skip the blank line after the number of test cases
        scanner.nextLine(); 
        
        for (int i = 0; i < testCases; i++) {
            // TreeMap automatically keeps the keys (species) sorted alphabetically
            TreeMap<String, Integer> speciesCount = new TreeMap<>();
            int totalTrees = 0;
            
            // Read lines until an empty line is found (which separates test cases)
            while (scanner.hasNextLine()) {
                String species = scanner.nextLine();
                
                if (species.isEmpty()) {
                    break; 
                }
                
                // If the tree exists, add 1 to its count. If not, set it to 1.
                speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
                totalTrees++;
            }
            
            // Calculate and print the results
            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                double percentage = (entry.getValue() * 100.0) / totalTrees;
                
                // Print formatted output (Locale.US ensures a dot as decimal separator)
                System.out.printf(Locale.US, "%s %.4f\n", entry.getKey(), percentage);
            }
            
            // Print a blank line ONLY BETWEEN test cases, not at the end
            if (i < testCases - 1) {
                System.out.println();
            }
        }
        
        scanner.close();
    }
}