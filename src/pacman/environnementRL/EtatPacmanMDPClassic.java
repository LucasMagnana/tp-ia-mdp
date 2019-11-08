package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
import java.util.Objects;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{
        int closestDot;
        int nbGhost;
        int nbGhostAround;
        int nbDot;
        int score;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
            
            StateAgentPacman myPacman = _stategamepacman.getPacmanState(0);
            
            score = _stategamepacman.getScore();
            closestDot = _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
            nbGhost = _stategamepacman.getNumberOfGhosts();
            nbDot = _stategamepacman.getFoodEaten();
            
            
            for (int i = 0 ; i < 6 ; i++){
                for (int j = 0 ; j < 6 ; j ++){
                    if (  _stategamepacman.isGhost(myPacman.getX() + i - 3 , myPacman.getY() + j - 3) )
                        nbGhostAround++;
                }
            }
		
	}
	
        @Override
        public int hashCode(){
            int dejaImplemente = super.hashCode();
            
            return Objects.hash(dejaImplemente, nbGhost, closestDot, nbGhostAround, nbDot, score);
        }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EtatPacmanMDPClassic other = (EtatPacmanMDPClassic) obj;
        if (this.closestDot != other.closestDot) {
            return false;
        }
        if (this.nbGhost != other.nbGhost) {
            return false;
        }
        if (this.nbGhostAround != other.nbGhostAround) {
            return false;
        }
        if (this.nbDot != other.nbDot) {
            return false;
        }
        if (this.score != other.score) {
            return false;
        }
        return true;
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
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}



	

}
