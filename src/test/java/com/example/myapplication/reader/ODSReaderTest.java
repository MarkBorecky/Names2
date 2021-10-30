package com.example.myapplication.reader;

import com.example.myapplication.model.Card;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class ODSReaderTest {

    @Test
    public void shouldMatch() {
        var text = "Kowalski";
        List<String> result = Stream.of(text)
                .filter(ODSReader.startFromCapital)
                .collect(Collectors.toList());
        assertEquals(text, result.get(0));
    }

    @Test
    public void shouldMatch2() {
        assertTrue("Kowalski".matches("[A-ZĄĆĘŁŚÓŹŻ][a-ząęćżźłó]{1,9}"));
    }

    @Test
    public void ShouldReturnOnlyWordWrittenFromCapitalLetter() {
        var text = new String[][]{
                {"", "", "", "", "", "Zero Zorro cccc skdjhsd sd sd dfs", ""},
                {"", "", "", "", "", "", ""}
        };
        var result = ODSReader.getDataDaoList(text);
        var expected = List.of(new Card("Zero Zorro cccc skdjhsd sd sd dfs", List.of("Zero Zorro")));
        for (var i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void ShouldReturnOnlyWordWrittenFromCapitalLetter2() {
        var text = new String[][]{{"", "", "", "", "", "Zero Zero cccc Andrzej sd Kowalski dfs", ""}};
        var result = ODSReader.getDataDaoList(text);
        var expected = List.of(
                new Card("Zero Zero cccc Andrzej Kowalski dfs", List.of("Zero Zero", "Andrzej Kowalski"))
        );
        for (var i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void shouldNotBeEmpty() {
        var empty = " ";
        assertFalse(Strings.isNotBlank(empty));
    }

    @Test
    public void shouldNotBeEmpty2() {
        String empty = null;
        assertFalse(Strings.isNotBlank(empty));
    }

    @Test
    public void shouldGetFirstnameAndLastname() {
        var text = "Do sdf Mark Smith sdf sd fsdf John Doe sf sf sdf.";
        var result = ODSReader.getFullNames(text);
        List<String> expected = List.of("Mark Smith", "John Doe");
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase1() {
        var text = "Tarnowicz Mikołaj syn prosfirni kradzież i zwrot pieniędzy z cerkwi drużyłowickiej – pokuta cerkiewna – wyznaczenie na djaczka";
        var result = ODSReader.getFullNames(text);
        List<String> expected = List.of("Tarnowicz Mikołaj");
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase2() {
        String mainText = "Tarnowicz Mikołaj syn prosfirni kradzież i zwrot pieniędzy z cerkwi drużyłowickiej – pokuta cerkiewna – wyznaczenie na djaczka";
        var text = new String[][]{
                {"header1", "header2", "header3", "header4", "header5", "header6"},
                {"", "", "", "", "", mainText, ""}
        };
        var result = ODSReader.getDataDaoList(text);
        List<Card> expected = List.of(
                new Card(mainText, List.of("Tarnowicz Mikołaj"))
        );
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase5() {
        var text = "Manifest o urodzeniu przez wielką księżną Aleksandrę Józefównę córki Wiery ";
        var result = ODSReader.getFullNames(text);
        List<String> expected = List.of("Aleksandrę Józefównę");
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase6() {
        var text = "Kogaczewska Helena wdowa po duchownym przyjęcie z Djecezji mińskiej do litewskiej ";
        var result = ODSReader.getFullNames(text);
        List<String> expected = List.of("Kogaczewska Helena");
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase7() {
        var text = "„Pignilewski” Jan uczeń żyrowickich szkół duchownych udzielenie miejsca ponomara – zwolnienie za niespełnianie obowiązków – udzielenie miejsca djaczka";
        var result = ODSReader.getFullNames(text);
        List<String> expected = List.of("Pignilewski Jan");
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase8() {
        var text = "Kołosow Justyna Poźniakowowie Filip, Stefania, Melecjusz staroobrzędowcy przyjęcie prawosławia k. 7 (brak k. 7 mej wszystkie karty luźne) (отсутствие 7 л., все листы разрозненные)";
        var result = ODSReader.getFullNames(text);
        List<String> expected = List.of(
                "Kołosow Justyna",
                "Poźniakowowie Filip",
                "Poźniakowowie Stefania",
                "Poźniakowowie Melecjusz"
        );
        assertEquals(expected, result);
    }

    @Test
    public void checkBorderCase9() {
        var text = "Dfg dfg dgf dgddg Poźniakowowie Filip, Stefania, Melecjusz df fdfdf ddffd Ddf dfgdf fdgd";
        var list = ODSReader.getFullNames(text);
        assertEquals(List.of("Poźniakowowie Filip","Poźniakowowie Stefania", "Poźniakowowie Melecjusz"), list);
    }

    @Test
    public void checkBorderCase10() {
        var text = "k. 7 (brak k. 7 mej wszystkie karty luźne) (отсутствие 7 л., все листы разрозненные)";
        var result = ODSReader.getPlainTextWithouRegexCharacters(text);
        System.out.println("result = " + result);
        assertEquals("k 7 brak k 7 mej wszystkie karty luźne отсутствие 7 л, все листы разрозненные", result);
    }

    @Test
    public void testPair() {
        var text = new String[]{"John", "Doe", "Jan", "Kowalski", "Maria", "Currie-Skłodowska"};
        var expected = List.of("John Doe", "Jan Kowalski", "Maria Currie-Skłodowska");
        var result = ODSReader.pairStrings(text);
        assertEquals(expected.size(), result.size());
        expected.forEach(x -> result.contains(x));
    }
}
































