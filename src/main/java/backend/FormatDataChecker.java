package backend;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Класс с вспомогательными инструментами
 * для работы с форматом строки.
 */
public class FormatDataChecker {
    /**
     * Сделать не null и не строкой из пробелов.
     *
     * @param str строка для преобразования.
     */
    public static String makeNotBlank(String str) {
        if (Objects.isNull(str) || str.isBlank()) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * Проверить что это телефонный номер
     * при помощи регулярочки.
     *
     * @param number строка с предполагаемым номером.
     * @return является ли входная строка номером телефона.
     */
    public static boolean isPhoneNumber(String number) {
        return Pattern.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{3,15}$", number);
    }

    /**
     * Проверить что это адрес электронной почты по формату.
     *
     * @param email предполагаемый адрес электронной почты.
     * @return является ли входная строка адресом электронной почты.
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(
                "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})",
                email);
    }

    /**
     * Убрать из строки пробелы, знаки (не буквы и не цифры) и привести в лоукейс.
     *
     * @param str строка.
     * @return преобразованная строка.
     */
    public static String transformStringForSearch(String str) {
        return str.replaceAll("\\s+", "")
                .replaceAll("\\W", "")
                .toLowerCase();
    }
}
