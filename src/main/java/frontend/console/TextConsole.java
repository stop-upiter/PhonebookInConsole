package frontend.console;

import backend.Contact;
import backend.FormatDataChecker;
import backend.PhoneBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

/**
 * Текстовая консоль для работы с телефонной книгой.
 */
public class TextConsole {
    /**
     * Логгер работы текстовой консоли.
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * Сканнер для ввода данных с консоли.
     */
    private final Scanner in = new Scanner(System.in);

    /**
     * Телефонная книга, с которой работает консоль.
     */
    private final PhoneBook phoneBook;

    /**
     * Выполнялись ли уже какие-то команды в этой консоли?
     */
    private boolean firstCommand = true;

    /**
     * Продолжать ли консоли выполнять команды?
     */
    private boolean keepGoing = true;

    /**
     * Видно ли меню со списком команд?
     */
    private boolean menuIsVisible = true;

    /**
     * Текущий сохранённый контакт.
     */
    private Contact currentContact = null;

    /**
     * Конструктор текстовой консоли.
     * @param phoneBook телефонная книга,
     *                  с которой будет работать консоль.
     */
    public TextConsole(PhoneBook phoneBook) {
        logger.info("Текстовая консоль для телефонной книги создаётся.");
        if (Objects.isNull(phoneBook)) {
            logger.error("Телефонная книга - null.");
            throw new NullPointerException(
                    "Телефонная книга не может быть null!");
        }

        this.phoneBook = phoneBook;
        logger.info("Текстовая консоль для телефонной книги создана.");
    }

    /**
     * Работа консоли - считывание и обработка команды.
     * @throws IOException
     */
    public void run() throws IOException {
        logger.info("Консоль начинает работу с пользователем.");
        do {
            if (firstCommand) {
                welcome();
                firstCommand = false;
            }

            if (menuIsVisible) {
                showMenu();
            }

            logger.info("Ввод команды.");
            handleCommand(in.nextLine());
        } while (keepGoing);
        logger.info("Консоль завершила работу с пользователем.");
    }

    /**
     * Показать меню со списком команд.
     */
    private void showMenu() {
        System.out.println("\n0 - получить больше информации");
        System.out.println("1 - показать все контактики");
        System.out.println("2 - показать текущий сохранённый контактик");
        System.out.println("3 - добавить новый контактик");
        System.out.println("4 - заняться поиском контактиков");
        System.out.println("5 - удалить текущий сохранённый контактик");
        System.out.println("6 - закрыть книжечку");
    }

    /**
     * Поприветствовать пользователя.
     */
    private void welcome() {
        System.out.println("Солнышко, добро пожаловать в Телефонную Книжечку!" +
                "\nЧтобы пользоваться книжечкой, используй следующие команды:");
    }

    /**
     * Обработать команду и среагировать на неё.
     * @param command команда.
     * @throws IOException
     */
    private void handleCommand(String command) throws IOException {
        logger.info("Команда: "+command);
        switch (command.trim().toLowerCase()) {
            case "0":
            case "info":
            case "help":
            case "инфа":
            case "помогите":
            case "памагити":
            case "что":
                showMoreInfo();
                break;
            case "1":
            case "all":
            case "все":
            case "список":
                showAllContacts();
                break;
            case "2":
            case "buffer":
            case "last":
            case "current":
            case "saved":
            case "сохранённый":
            case "буффер":
            case "последний":
            case "текущий":
                showCurrentContact();
                break;
            case "3":
            case "add":
            case "new":
            case "добавить":
            case "создать":
            case "новый":
                addNewContact();
                break;
            case "4":
            case "find":
            case "search":
            case "google":
            case "найди":
            case "найти":
            case "ищи":
                find();
                break;
            case "5":
            case "delete":
            case "remove":
            case "удали":
            case "вон":
                deleteCurrentContactFromPhoneBook();
                break;
            case "6":
            case "exit":
            case "stop":
            case "end":
            case "close":
            case "выйти":
            case "стоп":
            case "пока":
            case "закрыть":
                stop();
                break;
            case "hide":
            case "скрыть":
                menuIsVisible = false;
                break;
            case "menu":
            case "hint":
            case "меню":
                menuIsVisible = true;
                break;
            case "":
                break;
            default:
                unknownCommand();
        }
    }

    /**
     * Добавить новый контакт в телефонную книгу.
     * @throws IOException
     */
    private void addNewContact() throws IOException {
        logger.info("Начало ввода информации для нового контакта.");
        System.out.println("Чтобы пропустить поле, просто сразу нажмите ЕНТЕР. \nНо будьте внимательны! " +
                "Контактики с пустыми ФИО или дубликаты создать не получится!");

        System.out.print("Имя:");
        String name = in.nextLine();

        System.out.print("Фамилия:");
        String surname = in.nextLine();

        System.out.print("Отчество:");
        String patronymic = in.nextLine();

        System.out.print("Адрес:");
        String address = in.nextLine();

        List<String> phones = new ArrayList<>();
        System.out.println("Чтобы закончить ввод номеров телефона введите ЕНТЕР.");
        boolean encore = true;
        do {
            System.out.print("Телефон:");
            String phone = in.nextLine();
            if (phone.isBlank()){
                encore = false;}
            else if (!FormatDataChecker.isPhoneNumber(phone)) {
                logger.warn("Был введен номер "+phone+" неверного формата");
                System.out.println("Это не телефонный номер! Попробуйте ещё:");
            }
            else {
                phones.add(phone);
            }
        } while (encore);

        System.out.print("День рождения:");
        logger.info("Начала ввода дня рождения.");
        LocalDate birthday = readLocalDateFromConsole();
        logger.info("Конец ввода дня рождения.");

        boolean first = true;
        String email = "";
        do{
            if (first){
                first = false;
            }
            else {
                logger.warn("Был введен электронный адрес "+email+" неверного формата");
                System.out.println("Это не е-мэйл! Формат какой-то не такой...");
            }

            System.out.print("Е-мэйл:");
            email = FormatDataChecker.makeNotBlank(in.nextLine());
            encore = !email.isEmpty() && !FormatDataChecker.isEmail(email);

        } while (encore);

        logger.info("Конец ввода информации для нового контакта.");

        if (name.isBlank() && surname.isBlank() && patronymic.isBlank()){
            logger.warn("Введен контакт с пустым ФИО. Контакт не создан.");
            System.out.println("Пустой контакт! Не создан!");
        }
        else if (!phoneBook.addContact(name, surname, patronymic, address, phones, birthday, email)) {
            logger.warn("Введен контакт с уже существующим в книге ФИО. Контакт не создан.");
            System.out.println("Ай-яй! Дубликат! Контактик не был создан!");
        } else {

            List<Contact> tmp = phoneBook.getAllContacts();
            currentContact = tmp.get(tmp.size() - 1);
            logger.info("Контакт создан: "+
                    currentContact.getSurname()+" "+currentContact.getName() + " "+currentContact.getPatronymic());
            logger.info("Контакт сохранён как текущий.");
            System.out.println("Контактик создан и сохранён в текущий!");
        }
    }

    /**
     * Остановка работы консоли.
     */
    private void stop() {
        logger.info("Работа консоли завершается по команде.");
        System.out.println("Книжечка закрыта, мяу!");
        keepGoing = false;
    }

    /**
     * Показать дополнительную информацию.
     */
    private void showMoreInfo() {
        logger.info("Вывод дополнительной информации.");
        System.out.println("Мяу! Все контактики в книжечке сохраняются и запоминаются!\n" +
                "Так что если ты закроешь книжечку, а потом откроешь её - " +
                "то твои контактики не потеряются!");
        System.out.println("Чтобы удалить контактик, нужно сначала найти его при помощи поиска и сохранить как текущий! " +
                "\nЕсли найдётся только один контактик - то он сохранится автомачески.");
        System.out.println("При добавлении нового контактика, он сохраняется как текущий.");
        System.out.println("При поиске не учитываются пробелы и нет разницы между большими и маленькими буквами!");
        System.out.println("Удачи и приятного дня =)");
    }

    /**
     * Удалить текущий контакт.
     * @throws IOException
     */
    private void deleteCurrentContactFromPhoneBook() throws IOException {
        logger.info("Начало удаления текущего контакта.");
        if (Objects.isNull(currentContact)) {
            logger.info("Текущего контакта нет. Удаление не произошло.");
            System.out.println("Не с кем рвать связи! Нет сохранённого контактика.");
        } else {
            System.out.println("Попрощайтесь со следующим человечком: ");
            System.out.println(contactConsoleText(currentContact));
            phoneBook.deleteContact(currentContact);
            logger.info("Из книги был удалён контакт: "+
                    currentContact.getSurname()+" "+currentContact.getName() + " "+currentContact.getPatronymic());
            currentContact = null;
        }
    }

    /**
     * Показать текущий сохраненный контакт.
     */
    private void showCurrentContact() {
        if (Objects.isNull(currentContact)) {
            logger.info("Нет текущего контакта для просмотра.");
            System.out.println("Нет сохранённого контактика.");
        } else {
            logger.info("Текущий контакт: "+
                    currentContact.getSurname()+" "+currentContact.getName() + " "+currentContact.getPatronymic());
            System.out.println("Человечек на быстром доступе: ");
            System.out.println(contactConsoleText(currentContact));
        }
    }

    /**
     * Реагировать на неизвестную команду.
     */
    private void unknownCommand() {
        logger.warn("Консоль не знает, как обработать команду.");
        System.out.println("Таких команд рабочие котики не знают =(");
    }

    /**
     * Найти контакт.
     */
    private void find() {
        logger.info("Начинается поиск в телефонной книге.");
        List<Contact> founded;
        logger.info("Выбор способа поиска.");
        if (menuIsVisible) {
            System.out.println("Каким образом ищем?");
            System.out.println("1 - по ФИО");
            System.out.println("2 - по дате рождения");
            System.out.println("3 - по номеру телефона");
        }

        switch (in.nextLine().toLowerCase().trim()) {
            case "1":
            case "fio":
            case "фио":
                logger.info("Поиск по ФИО.");
                founded = phoneBook.findByFullName(in.nextLine());
                break;
            case "2":
            case "date":
            case "bd":
            case "birthday":
            case "birth":
            case "день":
            case "др":
            case "день рождения":
            case "день рожденья":
                logger.info("Поиск по дате рождения.");
                founded = phoneBook.findByBirthday(readLocalDateFromConsole());
                break;
            case "3":
            case "phone":
            case "number":
            case "numb":
            case "ph":
            case "телефон":
            case "номер":
                logger.info("Поиск по номеру телефона.");
                founded = phoneBook.findByPhoneNumber(in.nextLine());
                break;
            case "":
                logger.info("Отмена поиска.");
                System.out.println("Ну не хочешь искать, ну и не надо...");
                return;
            default:
                unknownCommand();
                return;
        }

        if (founded.size() == 0) {
            logger.info("Ничего не найдено.");
            System.out.println("Котики долго листали книжку лапками, " +
                    "но не нашли ничего похожего =(");
        } else if (founded.size() == 1) {
            System.out.println("Мяу! Мы всё нашли!");
            logger.info("Найден один элемент.");
            System.out.println(contactConsoleText(founded.get(0)));
            currentContact = founded.get(0);
        } else {
            logger.info("Найдено "+founded.size() + " элементов.");
            System.out.println("Найдено " + founded.size() + " подходящих контактиков!");
            for (int i = 0; i < founded.size(); i++) {
                System.out.println((i + 1) + ") " + contactConsoleText(founded.get(i)));
            }

            System.out.print("Какой из них вам нужен? Введите номер.\n" +
                    "Если никто вам не люб, введите что-то другое: ");
            try {
                logger.info("Выбор нового текущего контакта.");
                int index = in.nextInt() - 1;
                in.nextLine();
                if (index > -1 && index < founded.size()) {
                    logger.info("Новый текущий контакт: "+
                            currentContact.getSurname()+" "+currentContact.getName() + " "+currentContact.getPatronymic());
                    currentContact = founded.get(index);
                }
                else{
                    logger.info("Новый контакт не выбран.");
                }
            } catch (InputMismatchException ex) {
                logger.info("Новый контакт не выбран.");
            }
        }

    }

    /**
     * Вывести все контакты.
     */
    private void showAllContacts() {
        logger.info("Вывод всех контактов.");
        List<Contact> all = phoneBook.getAllContacts();

        if (all.size() == 0) {
            System.out.println("Туть пока пусто");
            return;
        }

        for (var contact : all) {
            System.out.println(contactConsoleText(contact));
        }
    }

    //todo сделать это toString-ом
    /**
     * @param contact контакт.
     * @return представление контакта в текстовом виде для данной консоли.
     */
    private String contactConsoleText(Contact contact) {
        if (Objects.isNull(contact)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (!contact.getSurname().isEmpty()) {
            stringBuilder.append("Фамилия: ").append(contact.getSurname()).append("\n");
        }

        if (!contact.getName().isEmpty()) {
            stringBuilder.append("Имя: ").append(contact.getName()).append("\n");
        }

        if (!contact.getPatronymic().isEmpty()) {
            stringBuilder.append("Отчество: ").append(contact.getPatronymic()).append("\n");
        }

        if (!contact.getAddress().isEmpty()) {
            stringBuilder.append("Адрес: ").append(contact.getAddress()).append("\n");
        }

        if (!contact.getPhoneNumbers().isEmpty()) {
            if (contact.getPhoneNumbers().size() > 1) {
                stringBuilder.append("Телефоны: ");
            } else {
                stringBuilder.append("Телефон: ");
            }

            for (int i = 0; i < contact.getPhoneNumbers().size(); i++) {
                stringBuilder.append(contact.getPhoneNumbers().get(i));
                if (i < contact.getPhoneNumbers().size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("\n");
        }

        if (contact.getBirthday().isPresent()) {
            stringBuilder.append("День рождения: ").append(contact.getBirthday().get()).append("\n");
        }

        if (!contact.getEmail().isEmpty()) {
            stringBuilder.append("Е-мэйл: ").append(contact.getEmail()).append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Считать дату из консоли.
     * @return считанную дату.
     */
    private LocalDate readLocalDateFromConsole(){
        int year = -1;
        logger.info("Начало ввода года.");
        while (year<1850 || year>2020){
            System.out.print("год ");
            try{
                year = in.nextInt();
                logger.info("Ввод числа.");
            }
            catch (InputMismatchException ex){
                logger.info("Введено не число.");
                System.out.println("Давайте будем реалистами");
            }
        }
        logger.info("Введен год: "+year);

        int month = -1;
        logger.info("Начало ввода месяца.");
        while (month<1 || month >12){
            System.out.print("номер месяца ");
            try{
                month = in.nextInt();
                logger.info("Ввод числа.");
            }
            catch (InputMismatchException ex){
                logger.info("Введено не число.");
                System.out.println("Возможно, вы живёте на Марсе, " +
                        "однако мы не марсиане и таких месяцев не знаем.");
            }
        }
        logger.info("Введен месяц: "+ month);

        int day = -1;
        logger.info("Начало ввода дня.");
        while (day < 1 || day > Month.of(month).length(Year.isLeap(year))){
            System.out.print("день ");
            try{
                day = in.nextInt();
                logger.info("Ввод числа.");
            }
            catch (InputMismatchException ex){
                logger.info("Введено не число.");
                System.out.println("Упс, столько дней в этом месяце явно нет!");
            }
        }
        logger.info("Введен день: "+ day);
        in.nextLine();

        return LocalDate.of(year,month,day);
    }
}
