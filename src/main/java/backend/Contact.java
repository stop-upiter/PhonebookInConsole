package backend;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect
public class Contact {
    private final String name;
    private final String surname;
    private final String patronymic;
    private final String address;
    @JsonDeserialize(as = ArrayList.class)
    private final List<String> phoneNumbers;
    private final String birthday;
    private final String email;


    public Contact(String name, String surname, String patronymic,
                   String address, List<String> phoneNumbers,
                   String birthday, String email){
        if (Objects.isNull(name) || name.isBlank()){
            this.name = "";
        }
        else {
            this.name = name;
        }

        if (Objects.isNull(surname) || surname.isBlank()){
            this.surname = "";
        }
        else {
            this.surname = surname;
        }

        if (Objects.isNull(patronymic) || patronymic.isBlank()){
            this.patronymic = "";
        }
        else {
            this.patronymic = patronymic;
        }

        if (Objects.isNull(address) || address.isBlank()){
            this.address = "";
        }
        else {
            this.address = address;
        }

        if (Objects.isNull(phoneNumbers)){
            this.phoneNumbers = new ArrayList<>();
        }
        else {
            this.phoneNumbers = phoneNumbers;
        }

        if (Objects.isNull(birthday)){
            this.birthday= "";
        }
        else {
            this.birthday = birthday;
        }

        if (Objects.isNull(email) || email.isBlank()){
            this.email = "";
        }
        else {
            this.email = email;
        }
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSurname() {
        return surname;
    }
}

