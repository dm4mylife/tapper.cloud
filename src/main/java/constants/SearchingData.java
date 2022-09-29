package constants;

import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

public class SearchingData {

    public static Stream<Arguments> articles() {
        return Stream.of(
                Arguments.of("R01-WED-00171-4"),
                Arguments.of("E2034-20OR2999L"),
                Arguments.of("C30-NC12-206PG-05040")
        );
    }

    private static Stream<Arguments> ids() {
        return Stream.of(
                Arguments.of("65059253"),
                Arguments.of("65717917"),
                Arguments.of("65754114"),
                Arguments.of("66558219")
        );
    }

    private static Stream<Arguments> mainCategories() {
        return Stream.of(
                Arguments.of("Кольцо"), Arguments.of("Серьги"), Arguments.of("Колье"), Arguments.of("Серьги"),
                Arguments.of("Браслет"), Arguments.of("Крест"), Arguments.of("Цепь"), Arguments.of("Броши и заколки"),
                Arguments.of("Запонки и зажимы"), Arguments.of("Часы"), Arguments.of("Пирсинг"), Arguments.of("Комплект"),
                Arguments.of("Оправа")

        );
}

    private static Stream<Arguments> stones() {
    return Stream.of(
            Arguments.of("Агат"), Arguments.of("Аквамарин"), Arguments.of("Александрит"), Arguments.of("Алпанит"),
            Arguments.of("Аметист"), Arguments.of("Аметрин"), Arguments.of("Без камней"), Arguments.of("Берил"),
            Arguments.of("Бирюза"), Arguments.of("Бриллианит"), Arguments.of("Бриллиант"), Arguments.of("Выращенный Бриллиант"),
            Arguments.of("Жемчуг"), Arguments.of("Изумруд"), Arguments.of("Иолит"), Arguments.of("Каучук"),
            Arguments.of("Кварц"), Arguments.of("Керамика"), Arguments.of("Кианит"), Arguments.of("Корунд"),
            Arguments.of("Кристалл"), Arguments.of("Кунцит"), Arguments.of("Лондон Топаз"),
            Arguments.of("Морганит"), Arguments.of("Лунный Камень"), Arguments.of("Наноситал"),
            Arguments.of("Бриллиант"), Arguments.of("Перламутр"), Arguments.of("Пластик"),
            Arguments.of("Оникс"), Arguments.of("Родолит"), Arguments.of("Рубин"),
            Arguments.of("Бриллиант"), Arguments.of("Опал"), Arguments.of("Перидот"),
            Arguments.of("Сапфир"), Arguments.of("Сваровски Камень"), Arguments.of("Силикон"),
            Arguments.of("Стекло"), Arguments.of("Султанит"), Arguments.of("Танзанит"),
            Arguments.of("Топаз"), Arguments.of("Турмалин"), Arguments.of("Фианит"),
            Arguments.of("Халцедон"), Arguments.of("Хризолит"), Arguments.of("Хромдиопсид"),
            Arguments.of("Хрусталь"), Arguments.of("Цаворит"), Arguments.of("Цитрин"),
            Arguments.of("Шнурок"), Arguments.of("Шпинель"), Arguments.of("Эмаль"),
            Arguments.of("Янтарь")


    );
}

    private static Stream<Arguments> collections() {
        return Stream.of(
            Arguments.of("Solo 1920"), Arguments.of("Brilliance"), Arguments.of("Millennium"), Arguments.of("Champagne"),
            Arguments.of("Empire"), Arguments.of("Энигма"), Arguments.of("Arabella"),
            Arguments.of("Black&White"), Arguments.of("Bonita"), Arguments.of("Caprice"), Arguments.of("Contrast"),
            Arguments.of("Estella"), Arguments.of("Festa"), Arguments.of("Fit"), Arguments.of("Flaming Ice"), Arguments.of("Grace"),
            Arguments.of("Infinite love"), Arguments.of("Le Chic"), Arguments.of("Leviev"), Arguments.of("Liola"),
            Arguments.of("Love"), Arguments.of("Lunia"), Arguments.of("Mia"), Arguments.of("Millennium Premium"),
            Arguments.of("Naomi"), Arguments.of("Princess"), Arguments.of("Royal"), Arguments.of("Royal Diamond"),
            Arguments.of("Snowflake"), Arguments.of("Tenero"), Arguments.of("Trendy Oro"),
            Arguments.of("Vittoria"), Arguments.of("Waterfalls"), Arguments.of("Вишня в цвету"),
            Arguments.of("Детская коллекция"), Arguments.of("Драгоценное наследие"),
            Arguments.of("Мой первый бриллиант"), Arguments.of("Мужская"), Arguments.of("Обручальные кольца"),
            Arguments.of("Отражение небес"), Arguments.of("Помолвочные кольца"),
            Arguments.of("Русская классика"), Arguments.of("Серебро с бриллиантами"), Arguments.of("Символ веры"),
            Arguments.of("Танзаниты"), Arguments.of("Трилогия"), Arguments.of("Флирт"),
            Arguments.of("Вне коллекций")

        );

    }

    private static Stream<Arguments> categoryWithStones() {
        return Stream.of(
            Arguments.of("Кольцо с бриллиантом"), Arguments.of("Серьги с сапфиром"),
            Arguments.of("Золотые подвески с изумрудом"),
            Arguments.of("Колье с жемчугом"), Arguments.of("Браслеты плетения бисмарк"),
            Arguments.of("Кресты с бриллиантами"), Arguments.of("Золотые цепи плетения сингапур"),
            Arguments.of("Броши из изумруда"), Arguments.of("Запонки с бриллиантом"),
            Arguments.of("Пирсинг золотой")

        );
    }



}
