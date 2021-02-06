package frontend.console;

import backend.PhoneBook;

import java.io.IOException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) throws IOException {
        //todo logger
        PhoneBook phoneBook = null;
        String path = "../../../phonebook.json";
        boolean readAgain;
        do{
            readAgain = false;
            try{
                phoneBook = new PhoneBook(path);
            }
            catch (IOException ex){
                System.out.println("К сожалению, " +
                        "ошибки ввода-вывода при работе с файлами " +
                        "не позволяют нам работать с дефолтным файлом.");
                System.out.println("Хотите его заменить? 0 - нет, 1 - да.");
                Scanner in = new Scanner(System.in);
                if (in.nextLine().trim().equals("1")){
                    readAgain = true;
                }
                else{
                    end();
                }
            }
        } while (readAgain);

        TextConsole textConsole = new TextConsole(phoneBook);
        textConsole.run();

        end();
    }

    private static void end(){
        System.out.println("На этом всё.");
        System.exit(0);
    }
}
