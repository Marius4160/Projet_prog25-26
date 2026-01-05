
public enum TileType {
    Mur,
    BlocDestructible,
    SolVide,
    Joueur,
    Objectif,
    BlocLeger,
    BlocGlissant,
    BlocEnergivore,
    PileEnergie,
    Explosif;

    public String toSymbol() {
        return switch (this) {
            case Mur ->
                "#";
            case BlocDestructible ->
                "X";
            case SolVide ->
                " ";
            case Joueur ->
                "@";
            case Objectif ->
                ".";
            case BlocLeger ->
                "o";
            case BlocGlissant ->
                "O";
            case BlocEnergivore ->
                "*";
            case PileEnergie ->
                "+";
            case Explosif ->
                "E";
        };
    }

    public static TileType fromSymbol(char symbol) {
        return switch (symbol) {
            case '#' ->
                Mur;
            case 'X' ->
                BlocDestructible;
            case ' ' ->
                SolVide;
            case '@' ->
                Joueur;
            case '.' ->
                Objectif;
            case 'o' ->
                BlocLeger;
            case 'O' ->
                BlocGlissant;
            case '*' ->
                BlocEnergivore;
            case '+' ->
                PileEnergie;
            case 'E' ->
                Explosif;
            default ->
                throw new IllegalArgumentException("Symbole inconnu: " + symbol);
        };
    }
}
