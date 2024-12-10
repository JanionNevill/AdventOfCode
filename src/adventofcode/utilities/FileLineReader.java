package adventofcode.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileLineReader {

    public List<String> readLines(URL filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath.toURI())))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (URISyntaxException e) {
            System.err.println(String.format("Failed to read file '%s'", filePath));
            e.printStackTrace();
            return (new ArrayList<>());
        } catch (FileNotFoundException exptn) {
            System.err.println(String.format("File '%s' cannot be found", filePath));
            exptn.printStackTrace();
        } catch (IOException exptn) {
            System.err.println(String.format("Error reading file '%s'", filePath));
            exptn.printStackTrace();
        }

        return (lines);
    }

}
