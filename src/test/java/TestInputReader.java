import static org.junit.Assert.*;

import com.LingualeoExport.InputReader;
import jdk.internal.util.xml.impl.Input;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Logger;

public class TestInputReader {
    public static Logger log = Logger.getLogger(TestInputReader.class.getName());
    @Test
    public void testInputReader(){
        InputStream is = new ByteArrayInputStream("Just test. Просто тест. Тест просто. Простой тест. Тест.".getBytes());
        //InputReader inputReader = new InputReader(is);

        Set<Map.Entry<String, Integer>> EntrySet = InputReader.wordsMap.entrySet();

        int count = 0;
        for(Map.Entry<String, Integer> pair : EntrySet){
            if(conditionForCycle(pair)){
                count++; //Проверяем каждую пару в карте, если соответствует, то инкрементируем счётчик.
            }
        }
        assertEquals("OOoops! Test was failed. Something gone wrong. Check condition for cycle.", 5, count);
        log.info("Тест пройден. Количество слов в карте: " + count);
    }
    public boolean conditionForCycle(Map.Entry<String, Integer> pair){
        if((pair.getKey().equals("test") && pair.getValue() == 1) || (pair.getKey().equals("просто") && pair.getValue() == 2)
                || (pair.getKey().equals("тест") && pair.getValue() == 4) || (pair.getKey().equals("простой") && pair.getValue() == 1)
                || (pair.getKey().equals("just") && pair.getValue() == 1)){
            return true;
        }
        return false;
    }
}
