package backend;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PhoneBook {
    private String pathForSerializedJsonFile;
    private List<Contact> contacts;

    public PhoneBook(String path) throws IOException {

        if (Objects.isNull(path)|| path.isBlank()){
            throw new NullPointerException("Путь к файлу не может быть пустым!");
        }
        pathForSerializedJsonFile = path;


        try{
            deserialize();
        }
        catch(FileNotFoundException ex){
            //todo logger
            contacts = new ArrayList<>();
        }
        catch(IOException ex){
            //todo logger
            throw ex;
        }
    }

    public boolean addContact(final String name, final String surname, final String patronymic,
                              String address, List<String> phoneNumbers,
                              String birthday, String email){
        if ((Objects.isNull(name)||name.isBlank())
            &&(Objects.isNull(surname)||surname.isBlank())
            &&(Objects.isNull(patronymic)||patronymic.isBlank())){
        return false;
        }

        if (contacts.stream()
                .filter((contact -> contact.getName().equals(name)))
                .filter(contact -> contact.getSurname().equals(surname))
                .filter(contact -> contact.getPatronymic().equals(patronymic))
                .count() > 0){
            return false;
        }

        contacts.add(new Contact(name, surname, patronymic, address, phoneNumbers, birthday, email));
        serialize();
        return true;
    }

    public void deleteContact(Contact hater){
        contacts.remove(hater);
        serialize();
    }

    public List<Contact> findByFullName(String startOfName){
        return find(new Predicate<Contact>() {
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

    public List<Contact> findByPhoneNumber(String startOfNumber){
        return find(new Predicate<Contact>() {
            final String startWithIt = transformStringForSearch(startOfNumber);
            @Override
            public boolean test(Contact contact) {
                for (String number:
                     contact.getPhoneNumbers()) {
                    if (transformStringForSearch(number).startsWith(startWithIt)){
                        return true;
                    }
                }

                return false;
            }
        });
    }

    public List<Contact> findByBirthday(String date){
        return find(new Predicate<Contact>() {
            final String startWithIt = transformStringForSearch(date);
            @Override
            public boolean test(Contact contact) {
               return transformStringForSearch(contact.getBirthday()).startsWith(startWithIt);
            }
        });
    }

    public List<Contact> getAllContacts(){
        return new ArrayList<>(contacts);
    }

    private List<Contact> find(Predicate<Contact> conditionForSearch){
        return contacts.stream().filter(conditionForSearch).collect(Collectors.toList());
    }

    private String transformStringForSearch(String str){
        return str.replaceAll("\\s+","")
                .replaceAll("\\W","")
                .toLowerCase();
    }

    private void serialize() {
        try(FileWriter writer = new FileWriter(pathForSerializedJsonFile, false)){
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, contacts);
        }
        catch (IOException ex){
            //todo logger
        }
    }

    private void deserialize() throws IOException {
        try(FileReader reader = new FileReader(pathForSerializedJsonFile)){
            ObjectMapper mapper = new ObjectMapper();
            contacts = (ArrayList<Contact>) (mapper.readValue(reader, ArrayList.class).stream().
                    filter(o -> o.getClass().equals(Contact.class)).collect(Collectors.toList()));
        }
    }
}
