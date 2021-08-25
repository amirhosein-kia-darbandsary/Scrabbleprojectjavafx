package tile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Dictionary {
    private static Dictionary dictionaryInstance;
    private Set<String> dictionary = new HashSet<>();

//    private ArrayList<String> dictionary = new ArrayList();

    private Dictionary(String fileName) {
        initialDictionary(fileName);
    }

    public static Dictionary getDictionaryInstance() {
        if (dictionaryInstance == null) {
            dictionaryInstance = new Dictionary("words.txt");
        }
        return dictionaryInstance;
    }

    public void initialDictionary(String fileName) {
        Scanner scanner1 = null;
        Scanner scanner2 = null;

        try {
            scanner1 = new Scanner(new File(fileName));

            while (scanner1.hasNextLine()) {
                scanner2 = new Scanner(scanner1.nextLine());
                while (scanner2.hasNext()) {
                    String s = scanner2.next();
                    dictionary.add(s);
                }
                scanner2.close();
            }
        } catch (FileNotFoundException ex) {
        } finally {
            if (scanner1 != null) {
                scanner1.close();
            }
            if (scanner2 != null) {
                scanner2.close();
            }
        }

    }


    public boolean contains(String word) {
        word = word.toUpperCase();

//        Iterator<String> i = dictionary.iterator();
//        while (i.hasNext()) {
//                System.out.println(i.next());
//
//        }
        if (dictionary.contains(word)) {

            return true;
        }


        return false;
    }
}
