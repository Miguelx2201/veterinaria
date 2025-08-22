package app;

import model.Pet;
import model.Veterinary;

import java.util.Arrays;

public class Aplicacion {
    public static void main(String[] args) {
        Veterinary veterinary1 = new Veterinary("Calle 23 #23-28", "Peludos el Berlin", "1091885531");
        System.out.println(veterinary1.getListPets().toString());
        Pet rocky = new Pet("1", "Rocky", "Gato", "Criollo", (byte)3, "Blanco con gris", 4.5);
        System.out.println(rocky.addDiseases("Caida de pelo"));
        System.out.println(Arrays.toString(rocky.getListDiseases()));
        veterinary1.addPet(rocky);
        System.out.println(veterinary1.getListPets().toString());
        Pet foundPet = veterinary1.searchPet("1");
        System.out.println(foundPet.toString());
        veterinary1.removePet(rocky);
        System.out.println(veterinary1.getListPets().toString());
    }
}
