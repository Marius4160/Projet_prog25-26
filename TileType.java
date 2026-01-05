
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
}
