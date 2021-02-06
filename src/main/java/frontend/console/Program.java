package frontend.console;

import backend.PhoneBook;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        PhoneBook phoneBook = null;
        String path = "../../../phonebook.json";
        boolean readAgain;
            try {
                phoneBook = new PhoneBook(path);
            } catch (IOException ex) {
                System.out.println("К сожалению, " +
                        "ошибки ввода-вывода при работе с файлами " +
                        "не позволяют нам работать с дефолтным файлом.");

                end();
            }
        TextConsole textConsole = new TextConsole(phoneBook);
        textConsole.run();

        end();
    }

    private static void end(){
        System.out.println("На этом всё.");
        System.exit(0);
    }
}
