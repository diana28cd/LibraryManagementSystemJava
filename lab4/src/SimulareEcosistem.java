import java.util.*;

// Clasa abstractă EntitateEcosistem
abstract class EntitateEcosistem {
    protected String nume;
    protected int energie;
    protected int x, y;
    protected double rataSupravietuire;

    public EntitateEcosistem(String nume, int energie, int x, int y, double rataSupravietuire) {
        this.nume = nume;
        this.energie = energie;
        this.x = x;
        this.y = y;
        this.rataSupravietuire = rataSupravietuire;
    }

    public abstract void actioneaza();

    public boolean esteInViata() {
        return energie > 0;
    }

    public void consumaEnergie(int valoare) {
        this.energie -= valoare;
    }

    @Override
    public String toString() {
        return nume + " la poziția (" + x + ", " + y + ") cu energie " + energie;
    }
}

// Clasa Planta
class Planta extends EntitateEcosistem {
    private static final int ENERGIE_REPRODUCERE = 20;

    public Planta(String nume, int energie, int x, int y) {
        super(nume, energie, x, y, 1.0);
    }

    @Override
    public void actioneaza() {
        // Planta crește (energie +10)
        energie += 10;
    }

    public Planta reproduce() {
        if (energie >= ENERGIE_REPRODUCERE) {
            energie /= 2; // Energia este împărțită cu noua plantă
            return new Planta("PlantaNoua", energie, x + 1, y + 1);
        }
        return null;
    }
}

// Interfața Interactiune
interface Interactiune {
    void ataca(Animal prada);

    void reproduce();
}

// Clasa abstractă Animal
abstract class Animal extends EntitateEcosistem implements Interactiune {
    protected int viteza;
    protected String tipHrana;

    public Animal(String nume, int energie, int x, int y, double rataSupravietuire, int viteza, String tipHrana) {
        super(nume, energie, x, y, rataSupravietuire);
        this.viteza = viteza;
        this.tipHrana = tipHrana;
    }

    public abstract void deplaseaza();

    @Override
    public void ataca(Animal prada) {
        if (this.energie > prada.energie) {
            this.energie += prada.energie;
            prada.energie = 0;
        }
    }
}

// Clasa Erbivor
class Erbivor extends Animal {
    public Erbivor(String nume, int energie, int x, int y) {
        super(nume, energie, x, y, 0.8, 2, "plante");
    }

    @Override
    public void actioneaza() {
        deplaseaza();
        consumaEnergie(5);
    }

    @Override
    public void deplaseaza() {
        x += viteza;
        y += viteza;
    }

    @Override
    public void reproduce() {
        System.out.println(nume + " încearcă să se reproducă.");
    }
}

// Clasa Carnivor
class Carnivor extends Animal {
    public Carnivor(String nume, int energie, int x, int y) {
        super(nume, energie, x, y, 0.9, 3, "carne");
    }

    @Override
    public void actioneaza() {
        deplaseaza();
        consumaEnergie(8);
    }

    @Override
    public void deplaseaza() {
        x += viteza;
        y -= viteza;
    }

    @Override
    public void reproduce() {
        System.out.println(nume + " încearcă să se reproducă.");
    }
}

// Clasa Omnivor
class Omnivor extends Animal {
    public Omnivor(String nume, int energie, int x, int y) {
        super(nume, energie, x, y, 0.85, 2, "plante și carne");
    }

    @Override
    public void actioneaza() {
        deplaseaza();
        consumaEnergie(6);
    }

    @Override
    public void deplaseaza() {
        x -= viteza;
        y += viteza;
    }

    @Override
    public void reproduce() {
        System.out.println(nume + " încearcă să se reproducă.");
    }
}

// Clasa Ecosistem
class Ecosistem {
    private List<EntitateEcosistem> entitati = new ArrayList<>();
    private int dimensiuneX;
    private int dimensiuneY;

    public Ecosistem(int dimensiuneX, int dimensiuneY) {
        this.dimensiuneX = dimensiuneX;
        this.dimensiuneY = dimensiuneY;
    }

    public void adaugaEntitate(EntitateEcosistem entitate) {
        entitati.add(entitate);
    }

    public void simuleazaPas() {
        System.out.println("\nSimularea unui pas:");
        List<EntitateEcosistem> noiEntitati = new ArrayList<>();

        for (EntitateEcosistem entitate : entitati) {
            entitate.actioneaza();

            if (entitate instanceof Planta planta) {
                Planta nouaPlanta = planta.reproduce();
                if (nouaPlanta != null) noiEntitati.add(nouaPlanta);
            }
        }

        entitati.addAll(noiEntitati);
        entitati.removeIf(entitate -> !entitate.esteInViata());

        afiseazaStare();
    }

    public void afiseazaStare() {
        System.out.println("Starea ecosistemului:");
        entitati.forEach(System.out::println);
    }
}

// Main class
public class SimulareEcosistem {
    public static void main(String[] args) {
        Ecosistem ecosistem = new Ecosistem(100, 100);

        ecosistem.adaugaEntitate(new Planta("Planta1", 30, 5, 5));
        ecosistem.adaugaEntitate(new Erbivor("Iepure", 50, 10, 10));
        ecosistem.adaugaEntitate(new Carnivor("Lup", 70, 20, 20));
        ecosistem.adaugaEntitate(new Omnivor("Urs", 90, 30, 30));

        for (int i = 0; i < 5; i++) {
            ecosistem.simuleazaPas();
        }
    }
}
