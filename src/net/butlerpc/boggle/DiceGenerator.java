package net.butlerpc.boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DiceGenerator {

    static final int SIDES_ON_A_CUBE = 6;

    /**
     * All boggle pieces, updated version. Q represents Qu
     */
    private static String[] getUpdatedPieceList() {
        return new String[] {
                "AAEEGN",
                "ABBJOO",
                "ACHOPS",
                "AFFKPS",
                "AOOTTW",
                "CIMOTU",
                "DEILRX",
                "DELRVY",
                "DISTTY",
                "EEGHNW",
                "EEINSU",
                "EHRTVW",
                "EIOSST",
                "ELRTTY",
                "HIMNUQ",
                "HLNNRZ"
        };
    }

    /**
     * All boggle pieces, original version. Q represents Qu
     */
    private static String[] getLegacyPieceList() {
        return new String[] {
                "AACIOT",
                "ABILTY",
                "ABJMOQ",
                "ACDEMP",
                "ACELRS",
                "ADENVZ",
                "AHMORS",
                "BIFORX",
                "DENOSW",
                "DKNOTU",
                "EEFHIY",
                "EGKLUY",
                "EGINTV",
                "EHINPS",
                "ELPSTU",
                "GILRUW"
        };
    }

    public static List<Character> getRandomDice(int amount) {
        List<Character> dieList = new ArrayList<Character>(amount);
        String[] strings = getUpdatedPieceList();
        Random r = new Random();
        int j;
        for (int i = 0; i < amount; i++) {
            j = r.nextInt(SIDES_ON_A_CUBE);
            dieList.add(strings[i % strings.length].toCharArray()[j]);
        }
        Collections.shuffle(dieList);
        return dieList;
    }
}
