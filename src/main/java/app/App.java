package app;

import model.Pet;
import model.Veterinary;

import javax.swing.*;



public class App {
    public static void main(String[] args) {
        Veterinary veterinary = new Veterinary("Calle 29", "Amigos peludos", "320", "amigospeludos@gmail.com", "1091");
        boolean salir = false;
        int opcion;
        while (!salir) {
            JOptionPane.showMessageDialog(null,"""
                    
                    *Bienvenido a la veterinaria Amigos Peludos*
                    
                    A continuación podra seleccionar la acción que desee realizar:
                    1. Agregar mascota.
                    2. Eliminar mascota.
                    3. Buscar mascota.
                    4. Actualizar mascota.
                    5. Visualizar la información de la veterinaria.
                    6. Salir del sistema.
                    """);
            opcion = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la opcion que desea relizar: "));
            switch (opcion) {
                case 1 -> {
                    String id = JOptionPane.showInputDialog(null, "Ingrese el id de la mascota: ");
                    String name =  JOptionPane.showInputDialog(null, "Ingrese el nombre de la mascota: ");
                    String species = JOptionPane.showInputDialog(null, "Ingrese la especie de la mascota: ");
                    String breed  = JOptionPane.showInputDialog(null, "Ingrese la raza de la mascota: ");
                    byte age = Byte.parseByte(JOptionPane.showInputDialog(null, "Ingrese la edad de la mascota: "));
                    String color = JOptionPane.showInputDialog(null, "Ingrese el color de la mascota: ");
                    double weight = Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese el peso de la mascota: "));
                    Pet pet = new Pet(id,name,species,breed,age,color,weight);
                    boolean sick = Boolean.parseBoolean(JOptionPane.showInputDialog(null, "Su mascota tiene alguna " +
                            "enfermedad? (true/false): "));
                    if(sick) {
                        int numDiseases = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad de" +
                                " enfermedades que tiene la mascota:"));
                        String[] diseases = new String[numDiseases];
                        for(int i = 0; i < diseases.length; i++) {
                            diseases[i] = JOptionPane.showInputDialog(null, "Cual es la enfermedad numero "+(i+1)+":");
                        }
                        pet.setListDiseases(diseases);
                    }
                    veterinary.addPet(pet);
                }
                case 2 -> {
                    String id = JOptionPane.showInputDialog(null, "Ingrese el id de la mascota que desea eliminar: ");
                    veterinary.removePet(id);
                }
                case 3 -> {
                    String id =  JOptionPane.showInputDialog(null, "Ingrese el id de la mascota que desea buscar: ");
                    veterinary.searchPet(id);
                }
                case 4 -> {
                    String idUpdate = JOptionPane.showInputDialog(null,"Ingrese el id de la mascota que desea actualizar: ");
                    String id = JOptionPane.showInputDialog(null, "Ingrese el id de la mascota: ");
                    String name =  JOptionPane.showInputDialog(null, "Ingrese el nombre de la mascota: ");
                    String species = JOptionPane.showInputDialog(null, "Ingrese la especie de la mascota: ");
                    String breed  = JOptionPane.showInputDialog(null, "Ingrese la raza de la mascota: ");
                    byte age = Byte.parseByte(JOptionPane.showInputDialog(null, "Ingrese la edad de la mascota: "));
                    String color = JOptionPane.showInputDialog(null, "Ingrese el color de la mascota: ");
                    double weight = Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese el peso de la mascota: "));
                    Pet pet = new Pet(id,name,species,breed,age,color,weight);
                    boolean sick = Boolean.parseBoolean(JOptionPane.showInputDialog(null, "Su mascota tiene alguna " +
                            "enfermedad? (true/false): "));
                    if(sick) {
                        int numDiseases = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad de" +
                                " enfermedades que tiene la mascota:"));
                        String[] diseases = new String[numDiseases];
                        for (int i = 0; i < diseases.length; i++) {
                            diseases[i] = JOptionPane.showInputDialog(null, "Cual es la enfermedad numero " + (i + 1) + ":");
                        }
                        pet.setListDiseases(diseases);
                    }
                    veterinary.updatePet(idUpdate, pet);
                }
                case 5 -> JOptionPane.showMessageDialog(null, veterinary.toString());
                case 6 -> salir = true;
                default -> JOptionPane.showMessageDialog(null, "Ingrese una opcion valida.");
            }
        }
    }
}
