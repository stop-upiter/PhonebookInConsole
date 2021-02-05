package frontend;

import backend.Contact;
import backend.PhoneBook;

import java.sql.SQLOutput;
import java.util.*;

public class TextConsole {
    private final PhoneBook phoneBook;
    private boolean firstCommand = true;
    private boolean keepGoing = true;
    private Scanner in = new Scanner(System.in);
    private Contact currentContact = null;
    public TextConsole(PhoneBook phoneBook){
        if (Objects.isNull(phoneBook)){
            throw new NullPointerException("Телефонная книга не может быть null!");
        }
        this.phoneBook = phoneBook;
    }

    public void start(){
        do {
            if (firstCommand){
                welcome();
                firstCommand = false;
            }
            showMenu();
            handleCommand(in.nextLine());
        }while (keepGoing);
    }

    private void showMenu(){
        System.out.println("\n0 - получить больше информации");
        System.out.println("1 - показать все контактики");
        System.out.println("2 - показать текущеий сохранённый контактик");
        System.out.println("3 - добавить новый контактик");
        System.out.println("4 - заняться поиском контактиков");
        System.out.println("5 - удалить текущий сохранённый контактик");
        System.out.println("6 - закрыть книжечку");
    }

    private void welcome(){
        System.out.println("Солнышко, добро пожаловать в Телефонную Книжечку!" +
                "\nЧтобы пользоваться книжечкой, используй следующие команды:");
    }

    private void handleCommand(String command){
        switch (command){
            case "0": showMoreInfo();
            break;
            case "1" : showAllContacts();
            break;
            case "2": showCurrentContact();
            break;
            case "3": addNewContact();
            break;
            case "4" : find();
            break;
            case "5": deleteCurrentContactFromPhoneBook();
            break;
            case "6": stop();
            break;
            default: unknownCommand();
        }
    }

    private void addNewContact() {
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

        System.out.print("Ввод телефонных номеров. Когда закончите вводить телефоны, введите ЕНТЕР:");
        List<String> phones = new ArrayList<>();
        boolean encore = true;
        do{
            String phone = in.nextLine();
            if (phone.isBlank()){
                encore = false;
            }
            else{
                phones.add(phone);
            }
        } while (encore);

        System.out.print("День рождения:");
        String birthday = in.nextLine();

        System.out.print("Е-мэйл:");
        String email = in.nextLine();

        if (!phoneBook.addContact(name, surname, patronymic, address, phones, birthday, email)){
            System.out.println("Ай-яй! Контактик не был создан!");
        }
        else {
            List<Contact> tmp = phoneBook.getAllContacts();
            currentContact = tmp.get(tmp.size()-1);
            System.out.println("Контактик создан и сохранён в текущий!");
        }
    }

    private void stop() {
        System.out.println("Книжечка закрыта, мяу!");
        keepGoing = false;
    }

    private void showMoreInfo() {
        System.out.println("Мяу! Все контактики в книжечке сохраняются и запоминаются!\n" +
                "Так что если ты закроешь книжечку, а потом откроешь её - " +
                "то твои контактики не потеряются!");
        System.out.println("Чтобы удалить контактик, нужно сначала найти его при помощи поиска и сохранить как текущий! " +
                "\nЕсли найдётся только один контактик - то он сохранится автомачески.");
        System.out.println("При добавлении нового контактика, он сохраняется как текущий.");
        System.out.println("При поиске не учитываются пробелы и нет разницы между большими и маленькими буквами!");
        System.out.println("Удачи и приятного дня =)");
    }

    private void deleteCurrentContactFromPhoneBook() {
        if (Objects.isNull(currentContact)){
            System.out.println("Не с кем рвать связи! Нет сохранённого контактика.");
        }
        else{
            System.out.println("Попрощайтесь со следующим человечком: ");
            System.out.println(contactConsoleText(currentContact));
            phoneBook.deleteContact(currentContact);
            currentContact = null;
        }
    }

    private void showCurrentContact() {
        if (Objects.isNull(currentContact)){
            System.out.println("Нет сохранённого контактика.");
        }
        else {
            System.out.println("Человечек на быстром доступе: ");
            System.out.println(contactConsoleText(currentContact));
        }
    }

    private void unknownCommand() {
        System.out.println("Таких команд рабочие котики не знают =(");
    }

    private void find() {
        List<Contact> founded = null;
        System.out.println("Каким образом ищем?");
        System.out.println("1 - по ФИО");
        System.out.println("2 - по дате рождения");
        System.out.println("3 - по номеру телефона");

        switch (in.nextLine()){
            case "1" : founded = phoneBook.findByFullName(in.nextLine());
                break;
            case "2" : founded = phoneBook.findByBirthday(in.nextLine());
                break;
            case "3": founded = phoneBook.findByPhoneNumber(in.nextLine());
            break;
            default: unknownCommand();
            return;
        }

        if (founded.size() == 0){
            System.out.println("Котики долго листали книжку лапками, " +
                    "но не нашли ничего похожего =(");
        }
        else if (founded.size()==1){
            System.out.println("Мяу! Мы всё нашли!");
            System.out.println(contactConsoleText(founded.get(0)));
            currentContact = founded.get(0);
        }
        else {
            System.out.println("Найдено " + founded.size() + " подходящих контактиков!");
            for (int i = 0;i<founded.size();i++) {
                System.out.println((i+1)+") "+ contactConsoleText(founded.get(i)));
            }

            System.out.print("Какой из них вам нужен? Введите номер.\n" +
                    "Если никто вам не люб, введите что-то другое: ");
            try{
                int index = in.nextInt() - 1;
                in.nextLine();
                currentContact = founded.get(index);
            } catch (InputMismatchException ex){
                //todo logger
            }
        }

    }

    private void showAllContacts() {
        List<Contact> all = phoneBook.getAllContacts();

        if(all.size()==0){
            System.out.println("Туть пока пусто");
            return;
        }

        for (var contact: all){
            System.out.println(contactConsoleText(contact));
        }
    }

    private String contactConsoleText(Contact contact){
        if (Objects.isNull(contact)){
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (!contact.getSurname().isEmpty()){
            stringBuilder.append("Фамилия: ").append(contact.getSurname()).append("\n");
        }

        if (!contact.getName().isEmpty()){
            stringBuilder.append("Имя: ").append(contact.getName()).append("\n");
        }

        if (!contact.getPatronymic().isEmpty()){
            stringBuilder.append("Отчество: ").append(contact.getPatronymic()).append("\n");
        }

        if (!contact.getAddress().isEmpty()){
            stringBuilder.append("Адрес: ").append(contact.getAddress()).append("\n");
        }

        if (!contact.getPhoneNumbers().isEmpty()){
            if (contact.getPhoneNumbers().size()>1){
                stringBuilder.append("Телефоны: ");
            }
            else{
                stringBuilder.append("Телефон: ");
            }

            for (int i = 0; i<contact.getPhoneNumbers().size();i++){
                stringBuilder.append(contact.getPhoneNumbers().get(i));
                if (i<contact.getPhoneNumbers().size()-1){
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("\n");
        }

        if (!contact.getBirthday().isEmpty()){
            stringBuilder.append("День рождения: ").append(contact.getBirthday()).append("\n");
        }

        if (!contact.getEmail().isEmpty()){
            stringBuilder.append("Е-мэйл: ").append(contact.getEmail()).append("\n");
        }

        return stringBuilder.toString();
    }

}
