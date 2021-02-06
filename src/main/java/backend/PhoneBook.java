package backend;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static backend.FormatDataChecker.transformStringForSearch;

/**
 * Телефонная книжечка.
 */
public class PhoneBook {

    /**
     * Контакты книжечки.
     */
    private List<Contact> contacts;

    private final ObjectMapper mapper;
    private final File file;

    /**
     * Конструктор книжечки.
     * @param path путь к данным книжечки.
     * @throws IOException          если не получится десериализовать файл.
     * @throws NullPointerException если путь пуст или null.
     */
    public PhoneBook(String path) throws IOException {
        path = FormatDataChecker.makeNotBlank(path);
        if (path.isEmpty()) {
            throw new NullPointerException("Путь к файлу не может быть пустым!");
        }

        file = new File(path);
        mapper = new ObjectMapper();
        contacts = new ArrayList<>();
        deserialize();
    }

    /**
     * Добавить контакт.
     *
     * @param name         имя.
     * @param surname      фамилия.
     * @param patronymic   отчество.
     * @param address      адрес.
     * @param phoneNumbers номера телефонов.
     * @param birthday     день рождения.
     * @param email        адрес электронной почты.
     * @return получилось ли создать контакт.
     * @throws IOException
     */
    public boolean addContact(final String name, final String surname, final String patronymic,
                              String address, List<String> phoneNumbers,
                              LocalDate birthday, String email) throws IOException {
        if ((Objects.isNull(name) || name.isBlank())
                && (Objects.isNull(surname) || surname.isBlank())
                && (Objects.isNull(patronymic) || patronymic.isBlank())) {
            return false;
        }

        if (contacts.stream()
                .filter((contact -> contact.getName().equals(name)))
                .filter(contact -> contact.getSurname().equals(surname))
                .filter(contact -> contact.getPatronymic().equals(patronymic))
                .count() > 0) {
            return false;
        }

        contacts.add(new Contact(name, surname, patronymic, address, phoneNumbers, birthday, email));
        serialize();
        return true;
    }

    /**
     * Удалить контакт из книги.
     *
     * @param hater контакт для удаления.
     * @throws IOException
     */
    public void deleteContact(Contact hater) throws IOException {
        contacts.remove(hater);
        serialize();
    }

    /**
     * Найти среди контактов по началу ФИО.
     *
     * @param startOfName начало ФИО.
     * @return список подходящих контактов.
     */
    public List<Contact> findByFullName(String startOfName) {
        return find(new Predicate<>() {
            final String startWithIt = transformStringForSearch(startOfName);

            @Override
            public boolean test(Contact contact) {
                String fullName = contact.getSurname() + contact.getName() +
                        contact.getPatronymic();
                fullName = transformStringForSearch(fullName);
                return fullName.startsWith(startWithIt);
            }
        });
    }

    /**
     * Найти среди контактов по началу номера телефона.
     *
     * @param startOfNumber начало номера телефона.
     * @return список подходящих контактов.
     */
    public List<Contact> findByPhoneNumber(String startOfNumber) {
        return find(new Predicate<>() {
            final String startWithIt = transformStringForSearch(startOfNumber);

            @Override
            public boolean test(Contact contact) {
                for (var number :
                        contact.getPhoneNumbers()) {
                    if (transformStringForSearch(number).startsWith(startWithIt)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    /**
     * Найти среди контактов по дате рождения.
     *
     * @param date дата рождения.
     * @return список подходящих контактов.
     */
    public List<Contact> findByBirthday(final LocalDate date) {
        return find(new Predicate<>() {
            @Override
            public boolean test(Contact contact) {
                if (contact.getBirthday().isEmpty()) {
                    return false;
                } else {
                    return contact.getBirthday().get().equals(date);
                }
            }
        });
    }

    /**
     * Получить список всех контактов книги.
     *
     * @return все контакты книги.
     */
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    /**
     * Найти контакты с помощью предиката.
     *
     * @param conditionForSearch условие для выбора подходящих контактов.
     * @return список подходящих контактов.
     */
    private List<Contact> find(Predicate<Contact> conditionForSearch) {
        return contacts.stream().
                filter(conditionForSearch).
                collect(Collectors.toList());
    }

    /**
     * Сериализовать текущий список контактов.
     *
     * @throws IOException при ошибке сериализации.
     */
    private void serialize() throws IOException {
        try (FileWriter writer = new FileWriter(file, false)) {
            mapper.writeValue(writer, contacts);
        }
    }

    /**
     * Десериализовать контакты для книги.
     *
     * @throws IOException при ошибки десериализации.
     */
    @SuppressWarnings("unchecked")
    private void deserialize() throws IOException {
        if (!file.createNewFile()) {
            try (FileReader reader = new FileReader(file)) {
                contacts = new ArrayList<Contact>(mapper.readValue(reader, /*new TypeReference<List<Contact>>() { }*/ contacts.getClass()));
            }
        }
    }
}
