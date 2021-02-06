package backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Контакт для телефонной книги.
 */
public class Contact {
    /**
     * Имя.
     */
    private final String name;

    /**
     * Фамилия.
     */
    private final String surname;

    /**
     * Отчество.
     */
    private final String patronymic;

    /**
     * Адрес. Просто в одну строку. Никаких ухищрений.
     */
    private final String address;

    /**
     * Список номеров телефонов.
     */
    private List<String> phoneNumbers;

    /**
     * Дата рождения. Если её нет - то пустой опшионал.
     */
    private final Optional<LocalDate> birthday;

    /**
     * Адрес электронной почты. Один. Только самый рабочий.
     */
    private final String email;

    /**
     * Единственный и полный конструктор контакта.
     * Ибо чтобы создавать контакт в зависимости от набора данных,
     * требуемся много моих и так скудных умственных способностей.
     * @param name имя.
     * @param surname фамилия.
     * @param patronymic отчество.
     * @param address адрес.
     * @param phoneNumbers номера телефонов.
     * @param birthday день рождения.
     * @param email адрес электронной почты.
     */
    public Contact(String name, String surname, String patronymic,
                   String address, List<String> phoneNumbers,
                   LocalDate birthday, String email){
        this.name = FormatDataChecker.makeNotBlank(name);
        this.surname = FormatDataChecker.makeNotBlank(surname);
        this.patronymic = FormatDataChecker.makeNotBlank(patronymic);
        this.address = FormatDataChecker.makeNotBlank(address);
        this.phoneNumbers = new ArrayList<>();
        if (!Objects.isNull(phoneNumbers)){
            this.phoneNumbers.addAll(
                    phoneNumbers.stream().
                            peek(FormatDataChecker::makeNotBlank).
                            filter(ph ->!ph.isEmpty()).
                            collect(Collectors.toList()));
        }
        this.birthday = Objects.isNull(birthday)? Optional.empty() : Optional.of(birthday);
        this.email = FormatDataChecker.makeNotBlank(email);
    }

    /**
     * Получить извне номера телефонов так,
     * чтобы внутри контакта их случайно не поменять.
     * @return список номеров телефонов.
     */
    public List<String> getPhoneNumbers() {
        return new ArrayList<>(phoneNumbers);
    }

    /**
     * Получить дату рождения.
     * @return опшионал с датой рождения.
     */
    public Optional<LocalDate> getBirthday() {
        return birthday;
    }

    /**
     * Получить адрес.
     * @return адрес.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Получить аджрес электронной почты.
     * @return адрес электронной почты.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Получить имя.
     * @return имя.
     */
    public String getName() {
        return name;
    }

    /**
     * Получить отчество.
     * @return отчество.
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Получить фамилию.
     * @return фамилия.
     */
    public String getSurname() {
        return surname;
    }
}

