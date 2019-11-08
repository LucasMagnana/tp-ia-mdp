package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{
        int nbDot = 0;
        int nbGhost;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
            for(int i = 0 ; i < _stategamepacman.getNumberOfPacmans() ; i ++) {
                nbDot += _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(i));
            }
            
            nbGhost = _stategamepacman.getNumberOfGhosts();
            
		
	}
	
        @Override
        public int hashCode(){
            return nbDot + nbGhost;
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
