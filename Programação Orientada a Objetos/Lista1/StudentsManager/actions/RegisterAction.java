package actions;

import models.*;
import utils.InputHelper;

public class RegisterAction implements MenuAction {
    public int execute(Student[] students, int totalStudents) {
        System.out.println("~~~ Informacoes basicas ~~~");
        String name = InputHelper.readString("Nome: ");
        String rg = InputHelper.readString("RG: ");
        String ra = InputHelper.readString("RA: ");
        String cellphone = InputHelper.readPhone("Numero de celular: ");
        System.out.println("~~~ Data de aniversario ~~~");
        int day = InputHelper.readInt("Dia: ", 1, 31);
        int month = InputHelper.readInt("Mes: ", 1, 12);
        int year = InputHelper.readInt("Ano: ", 1950, 2026);
        System.out.println("~~~ Endereco ~~~");
        String city = InputHelper.readString("Cidade: ");
        String street = InputHelper.readString("Rua: ");
        String cep = InputHelper.readString("CEP: ");
        int number = InputHelper.readInt("Numero da casa: ", 1, 10000);
        System.out.println("~~~ Informacao academica ~~~");
        float cr = InputHelper.readFloat("Coeficiente de rendimento: ", 1, 10);

        Address newAddress = new Address(street, city, cep, number);
        Birthday newBirthday = new Birthday(day, month, year);
        students[totalStudents] = new Student(name, rg, ra, cellphone, newBirthday, newAddress, cr);
        
        return totalStudents + 1;
    }
}
