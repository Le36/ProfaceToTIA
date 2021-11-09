
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Works currently -> Proface Symbol List Export .csv -> .txt Import to TIA as
 * .db source
 */
public class Main {

    public static void main(String[] args) {

        boolean firstLine = true;
        boolean onArray = false;
        int arraySize = 0;
        String onArrayName = "";
        String onArrayType = "";
        String onArrayKeep = "";
        String prevKeep = "";
        TypeChecker tyyppi = new TypeChecker();

        boolean retainChange = false;
        ArrayList<String> lista = new ArrayList<>();

        try (Scanner TiedLukija = new Scanner(Paths.get("testi.txt"))) {
            while (TiedLukija.hasNextLine()) {
                lista.add(TiedLukija.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " ERROR");
        }

        System.out.println("Full .db, yes / anything");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().contains("yes")) {
            System.out.println("Name your DATA_BLOCK");
            System.out.println("DATA_BLOCK \"" + sc.nextLine() + "\"");
            System.out.println("{ S7_Optimized_Access := 'TRUE' }");
            System.out.println("VERSION : 0.1\nNON_RETAIN");
        }

        for (int i = 0; i < lista.size(); i++) {
            String haettava = lista.get(i);
            String[] palat = haettava.split(";", -1);

            String name = palat[0];
            String type = palat[2];
            String keep = palat[5];

            String name1 = "";

            if (lista.size() != i + 1) {
                String next = lista.get(i + 1);
                String[] palat1 = next.split(";", -1);

                name1 = palat1[0];
            }

            if (tyyppi.tyyppi(type).equals("Skip")) {
                continue;
            }

            if (firstLine && !keep.equals("1")) {
                System.out.println("VAR");
                System.out.println(name + " : " + tyyppi.tyyppi(type) + ";");
                firstLine = false;
                continue;
            }

            if (!name.equals("@") && name1.equals("@")) {
                onArrayName = name;
                onArrayType = type;
                onArrayKeep = keep;
                onArray = true;
            }

            if (onArray) {
                arraySize++;
            }

            if (!keep.equals(prevKeep) && !name.equals("@")) {
                if (onArray && !prevKeep.equals(onArrayKeep)) {
                    retainChange = true;
                } else if (!onArray) {
                    retainChange = true;
                }
            }

            if (keep.equals("1") && retainChange) {
                System.out.println("END_VAR");
                System.out.println("VAR RETAIN");
                prevKeep = keep;
                retainChange = false;
            }

            if (!keep.equals("1") && retainChange) {
                System.out.println("END_VAR");
                System.out.println("VAR");
                prevKeep = keep;
                retainChange = false;
            }

            if (onArray && !name1.equals("@")) {
                System.out.println(onArrayName + " : " + " Array[0.." + (arraySize - 2) + "] of " + tyyppi.tyyppi(onArrayType) + ";");
                arraySize = 0;
                onArray = false;
            }

            if (!onArray && !name.equals("@")) {

                System.out.println(name + " : " + tyyppi.tyyppi(type) + ";");
            }
        }
        System.out.println("END_VAR");
    }
}
