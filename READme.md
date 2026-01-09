***Projet Sokoban***

**Architecture**

-- Game.java --> Boucle principale du jeu, permet de rejouer et de lancer une partie;;
-- EtatJeu.java --> Définie un record avec à l'interieur toute les informations pratique comme le nombre de bombe, la position du joueur ou encore la liste des actions déjà éffectuées... ;;
-- TileType.java --> Définit les types de chaque éléments du plateau comme les murs ou les BlocLeger par exemples... ;;
-- Grid.java --> Traite de tous ce qui concerne les regles de la grille et gere egalement les explosions qui affectent de facto le grid;;
-- Level.java --> Définit le format des levels que doivent prendre les fichiers Json;;
-- Loader.java --> Permet de charger le fichier Json via la librairie Gson et d'afficher la grille;;
-- Player.java --> Permet de definir la position du joueur la direction qu'il souhaite prendre avec un saisie clavier ainsi que la logique pour jouer manuellement;;
-- Solver.java --> Permet de lancer la boucle soit de maniere manuelle soit de maniere automatique;;

**Choix Logique**

-- Tableau : utilisation d'un tableau simple pour jouer en console avec un affichage utilisant json ;; 
-- Grid : Une copie de la grille est faite pour garder l'immuabilité ;;
-- Historique : rangée dans une liste toutes les actions sont répertoriées afin de pouvoir rejouer n'importe quelle positions ;;
-- Protection et Gameplay : Le joueur n'utilise pas de bombe si il tente de détruire un bloc qui ne peut pas l'être. Coup d'utilisation d'énergie different en fonction du bloc à pousser;; 

**Regles du jeu**

Les touches --> Déplacements(z(Aller vers le haut),q(Aller vers la gauche),s(Aller vers le bas),d(Aller vers la droite)) ;;

Regles --> 
    -Poussée des caisses (coûts variables : léger=1, normal=3, lourd=5).
    -Gestion de l'énergie (consommation à chaque action).
    -Système d'explosifs (b + direction) pour détruire les murs X.
    -Victoire détectée quand toutes les caisses sont sur des cibles.

**Execution du jeu**





25.0.1