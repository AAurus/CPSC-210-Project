package model;

import enums.ScoreType;
import enums.StatType;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.DataHandler;
import persistence.FileIOHandler;
import utility.Utility;

import javax.naming.InvalidNameException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FileIOHandlerTest {
    // A series of tests for the FileIOHandlerTest.
    private FileIOHandler ioHandler;

    @BeforeEach
    void setUp() {
        ioHandler = new FileIOHandler();
    }

    @Test
    void testReadItemProficiency() {
        Proficiency testItemProficiency = new Proficiency("Sword", new BigDecimal(5.670));
        JSONObject parsedItemProficiency = new JSONObject(testItemProficiency);

        Proficiency readItemProficiency = ioHandler.readProficiency(parsedItemProficiency);
        Assertions.assertEquals(testItemProficiency, readItemProficiency);
    }

    @Test
    void testReadScoreProficiency() {
        Proficiency testScoreProficiency = new Proficiency(ScoreType.STRENGTH, new BigDecimal(3.0090));
        JSONObject parsedScoreProficiency = new JSONObject(testScoreProficiency);

        Proficiency readScoreProficiency = ioHandler.readProficiency(parsedScoreProficiency);
        Assertions.assertEquals(testScoreProficiency, readScoreProficiency);
    }

    @Test
    void testReadFeature() {
        Modifier testMod = new Modifier(5);

        Proficiency testProficiency = new Proficiency(ScoreType.STRENGTH, new BigDecimal(5));

        Feature testSubFeature1 = new Feature("Test SubFeature 1", "description");
        Feature testSubFeature2 = new Feature("Test SubFeature 2", "lorem ipsum");

        Feature testScoreFeature = new Feature("Test Score Feature", ScoreType.CON_CHECK, testMod);

        Feature testStatFeature = new Feature("Test Stat Feature", StatType.MAX_HIT_POINTS, testMod);

        Feature testProficiencyFeature = new Feature("Test Proficiency Feature", testProficiency);

        ArrayList<Feature> testFeatureList1 = new ArrayList<>();
        testFeatureList1.add(testSubFeature1);
        testFeatureList1.add(testSubFeature2);

        Feature testMultiFeature = new Feature("Test Multi Feature", testFeatureList1);

        Feature testDescFeature = new Feature("Test Description Feature", "Test Description");

        Feature testOmniFeature = new Feature("Test Feature With Everything",
                                      new ArrayList<>(Utility.listOf(testScoreFeature, testStatFeature,
                                                                     testProficiencyFeature,
                                                                     testMultiFeature, testDescFeature)));

        JSONObject parseScoreFeature = new JSONObject(testScoreFeature);
        JSONObject parseStatFeature = new JSONObject(testStatFeature);
        JSONObject parseProficiencyFeature = new JSONObject(testProficiencyFeature);
        JSONObject parseMultiFeature = new JSONObject(testMultiFeature);
        JSONObject parseDescFeature = new JSONObject(testDescFeature);
        JSONObject parseOmniFeature = new JSONObject(testOmniFeature);

        Feature readScoreFeature = ioHandler.readFeature(parseScoreFeature);
        Feature readStatFeature = ioHandler.readFeature(parseStatFeature);
        Feature readProficiencyFeature = ioHandler.readFeature(parseProficiencyFeature);
        Feature readMultiFeature = ioHandler.readFeature(parseMultiFeature);
        Feature readDescFeature = ioHandler.readFeature(parseDescFeature);
        Feature readOmniFeature = ioHandler.readFeature(parseOmniFeature);

        Assertions.assertEquals(testScoreFeature, readScoreFeature);
        Assertions.assertEquals(testStatFeature, readStatFeature);
        Assertions.assertEquals(testProficiencyFeature, readProficiencyFeature);
        Assertions.assertEquals(testMultiFeature, readMultiFeature);
        Assertions.assertEquals(testDescFeature, readDescFeature);
        Assertions.assertEquals(testOmniFeature, readOmniFeature);
    }

    @Test
    void testReadCharacter() {
        Character testCharacter = new Character("Test Character", 10, 12, 14, 16, 18, 20);
        testCharacter.addFeature(new Feature("Test Feature", ScoreType.STRENGTH, new Modifier(5)));
        testCharacter.addEquippedItem(new InventoryItem("Test Item"));
        testCharacter.setRace("Test Race");
        testCharacter.setBackground("Test Background");
        testCharacter.addClass("Test Class 1");
        testCharacter.addClass("Test Class 2");
        testCharacter.setLevel(1, 10);

        JSONObject parseCharacter = new JSONObject(testCharacter);

        Character readCharacter = ioHandler.readCharacter(parseCharacter);
        Assertions.assertEquals(testCharacter.hashCode(), readCharacter.hashCode());
    }

    @Test
    void testReadWriteFile() {
        InventoryItem testItem = new InventoryItem("Test Item", "Test description");
        Feature testFeature = new Feature("Test Feature", ScoreType.STRENGTH, new Modifier(5));
        Character testCharacter = new Character("Test Character", 10, 12, 14, 16, 18, 20);
        testCharacter.addFeature(testFeature);
        testCharacter.addEquippedItem(testItem);
        testCharacter.setRace("Test Race");
        testCharacter.setBackground("Test Background");
        testCharacter.addClass("Test Class 1");
        testCharacter.addClass("Test Class 2");
        testCharacter.setLevel(1, 10);

        ArrayList<InventoryItem> testItemList = new ArrayList<>();
        ArrayList<Feature> testFeatureList = new ArrayList<>();
        ArrayList<Character> testCharacterList = new ArrayList<>();

        testItemList.add(testItem);
        testFeatureList.add(testFeature);
        testCharacterList.add(testCharacter);

        DataHandler testData = new DataHandler(testCharacterList, testItemList, testFeatureList);
        try {
            ioHandler.writeSave("Test Save", testData);
        } catch (IOException e) {
            Assertions.fail("Unexpected IOException on write");
        }
        try {
            File testSave = new File(FileIOHandler.SAVE_FILE_DIRECTORY_PATH + "/Test-Save.json");
            DataHandler loadData = ioHandler.loadSave(testSave);
            Assertions.assertEquals(testData, loadData);
            testSave.delete();
        } catch (IOException e) {
            Assertions.fail("Unexpected IOException on read");
        }
    }
}
