
/*
 * Client 
 * 
 * Version 0.1
 * 
 * Contains or imports Sokoban maps from file server
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Client {

    /**
     * Constructor
     * <p/>
     * Initializes standard Client
     */
    public Client() {

    }

    /**
     * Returns the Map (class) representation from predefined FilePath
     *
     * @return Map
     * @throws IOException
     */
    public Map getMapFromFile() throws IOException {

        Vector<String> map = new Vector<String>();

        BufferedReader fileBr = new BufferedReader(
                new FileReader("/Users/David/IdeaProjects/BPT/src/sample-data/00_sample.in"));

        String line = null;

        while (fileBr.ready()) {
            line = fileBr.readLine();
            map.add(line);
        } // End while
        fileBr.close();

        return new Map(map);
    }

    /**
     * Returns the Map (class) representation from specified file
     *
     * @param pFilePath
     * @return Map
     * @throws IOException
     */
    public Map getMapFromFile(String pFilePath) throws IOException {

        Vector<String> map = new Vector<String>();

        BufferedReader fileBr = new BufferedReader(
                new FileReader(pFilePath));

        String line = null;

        while (fileBr.ready()) {
            line = fileBr.readLine();
            map.add(line);
        } // End while
        fileBr.close();

        return new Map(map);
    }


}
