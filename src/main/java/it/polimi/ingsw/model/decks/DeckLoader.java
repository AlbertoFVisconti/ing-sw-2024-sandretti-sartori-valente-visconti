package it.polimi.ingsw.model.decks;

import org.json.JSONArray;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DeckLoader class allows to create objects to load decks' content from files.
 * Expanding subclasses must define the load method that actually looks through the provided file and
 * returns the loaded deck content.
 * For optimization's sake, a DeckLoader automatically stores the content the first time it's requested to,
 * further queries will only require the DeckLoader to provide the previously stored content.
 *
 * @param <T>
 */
public abstract class DeckLoader<T extends Drawable> {
    protected final String filename;
    private List<T> loadedContent = null;

    /**
     * Allows to create a Deck object with the content loaded by the object.
     * The first time this method is called, the content will be loaded/constructed.
     * From the second time on, this operation will be skipped.
     *
     * @return a new Deck object with the content loaded form the file.
     * @throws IOException the first time errors could occur trying to load the content.
     */
    public Deck<T> getDeck() throws IOException {
        return new Deck<>(this.getContent());
    }

    /**
     * Build a new DeckLoader that operates on the file whose name is provided.
     *
     * @param filename the name of the file which contains the deck's content
     */
    protected DeckLoader(String filename) {
        this.filename = filename;
    }

    /**
     * Retrieves the provided deck's content.
     * If it's the first call for this object, it will actually load the data from the file.
     * Otherwise, it will simply return the previously loaded data.
     *
     * @return a list of elements T representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    private ArrayList<T> getContent() throws IOException {
        if (loadedContent == null) {
            this.loadedContent = this.load();
        }

        return new ArrayList<>(loadedContent);

    }

    /**
     * Loads the deck's contents from the provided file. This method must be implemented
     * by subclasses to specify how the deck's content is loaded
     *
     * @return a list of elements T representing the deck's content
     * @throws IOException if there's a problem when trying to read the file
     */
    protected abstract List<T> load() throws IOException;

    /**
     * Builds a JSONArray containing the provided json file's data.
     * Helper method used internally in the process of loading the deck from a file.
     *
     * @param fileName the name of the file to use to build the JSONArray.
     * @return the JSONArray containing the provided file's data.
     * @throws IOException if there's a problem when trying to read the file.
     */
    protected static JSONArray buildJSONArrayFromFile(String fileName) throws IOException {
        FileReader reader = new FileReader(fileName);
        StringBuilder jsonString = new StringBuilder();

        int c;
        while ((c = reader.read()) != -1) {
            jsonString.append((char) c);
        }
        reader.close();

        return new JSONArray(jsonString.toString());
    }
}
