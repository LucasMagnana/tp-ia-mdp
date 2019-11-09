package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
import java.util.Objects;

/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning
 * tabulaire
 *
 */
public class EtatPacmanMDPClassic implements Etat, Cloneable {

    int closestDot;
    int nbGhostAround;
    int nbDot;
    int score;
    String environnementProche;

    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {

        StateAgentPacman myPacman = _stategamepacman.getPacmanState(0);

        score = _stategamepacman.getScore();
        closestDot = _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
        nbDot = _stategamepacman.getFoodEaten();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (_stategamepacman.isGhost(myPacman.getX() + i - 3, myPacman.getY() + j - 3)) {
                    nbGhostAround++;
                }
            }
        }

        String str = _stategamepacman.toString();
        ArrayList<ArrayList<Character>> maze = new ArrayList<>();
        maze.add(new ArrayList<>());
        int j = 0, positionX = 0, positionY = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                maze.add(new ArrayList<>());
                j++;
            } else if (str.charAt(i) == 'P') {
                positionY = j;
                maze.get(j).add(str.charAt(i));
            } else {
                maze.get(j).add(str.charAt(i));
            }
        }
        maze.remove(maze.size() - 1);
        positionX = positionX % (maze.get(0).size());
        for (int i = 0; i < maze.get(positionY).size(); i++) {
            if (maze.get(positionY).get(i) == 'P') {
                positionX = i;
                break;
            }
        }

        environnementProche = new String();
        if (positionX != 0 && positionY != 0) {
            for (int i = 0; i < 3; i++) {
                if(i != 0) 
                    environnementProche += '\n';
                for (int k = 0; k < 3; k++) {
                    environnementProche += maze.get(positionY - 1 + i).get((positionX - 1 + k));
                }
            }

            /*System.out.println(str);
            System.out.println("");
            System.out.println(environnementProche);
            System.out.println("");*/
        }
    }

    @Override
    public int hashCode() {
        int dejaImplemente = super.hashCode();

        return Objects.hash(dejaImplemente, closestDot, nbGhostAround, nbDot, score, environnementProche);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EtatPacmanMDPClassic)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EtatPacmanMDPClassic other = (EtatPacmanMDPClassic) obj;

        return closestDot == other.closestDot && nbGhostAround == other.nbGhostAround
                && nbDot == other.nbDot && score == other.score && environnementProche.equals(other.environnementProche);
    }

    @Override
    public String toString() {

        return "";
    }

    public Object clone() {
        EtatPacmanMDPClassic clone = null;
        try {
            // On recupere l'instance a renvoyer par l'appel de la 
            // methode super.clone()
            clone = (EtatPacmanMDPClassic) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implementons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }

        // on renvoie le clone
        return clone;
    }

}
