package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;

/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions
 * phi_i(s,a)
 *
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction {

    private double[] vfeatures;

    private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon 
    //--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles

    public FeatureFunctionPacman() {

    }

    @Override
    public int getFeatureNb() {
        return 4;
    }

    @Override
    public double[] getFeatures(Etat e, Action a) {
        vfeatures = new double[4];
        StateGamePacman stategamepacman;
        //EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

        //calcule pacman resulting position a partir de Etat e
        if (e instanceof StateGamePacman) {
            stategamepacman = (StateGamePacman) e;
        } else {
            System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
            return vfeatures;
        }

        StateAgentPacman pacmanstate_next = stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));

        //*** VOTRE CODE
        
        //BIAIS
        vfeatures[0] = 1.0;
        
        //FANTOMES
        /*if (stategamepacman.isGhost(pacmanstate_next.getX() - 1, pacmanstate_next.getY())) {
            vfeatures[1]++;
        }
        if (stategamepacman.isGhost(pacmanstate_next.getX() + 1, pacmanstate_next.getY())) {
            vfeatures[1]++;
        }
        if (stategamepacman.isGhost(pacmanstate_next.getX(), pacmanstate_next.getY() - 1)) {
            vfeatures[1]++;
        }
        if (stategamepacman.isGhost(pacmanstate_next.getX(), pacmanstate_next.getY() + 1)) {
            vfeatures[1]++;
        }*/
        
        vfeatures[1] = getGhostNumber(stategamepacman, pacmanstate_next);
        
        //DOTS
        if (stategamepacman.getMaze().isFood(pacmanstate_next.getX(), pacmanstate_next.getY())) {
            vfeatures[2]=1;
        }
        
        //DIST/MAPSIZE
        double sizeMap = (double)stategamepacman.getMaze().getSizeX()*stategamepacman.getMaze().getSizeY();
        vfeatures[3] = (double)(stategamepacman.getClosestDot(pacmanstate_next))/sizeMap;
        
        return vfeatures;
    }

    public void reset() {
        vfeatures = new double[4];

    }
    
    	private int getGhostNumber(StateGamePacman state, StateAgentPacman next){
		int cpt = 0;
		StateAgentPacman ghost;
		for(int i=0; i<state.getNumberOfGhosts(); i++){
			ghost = state.getGhostState(i);
                        if (ghost.getX() == next.getX() && ghost.getY() == next.getY()){
                            cpt++;
                        }
			for(int j=0; j<4; j++){
				int npos[] = state.getNextPosition(new ActionPacman(j), ghost);
				if(npos[0]==next.getX() && npos[1]==next.getY()){
					cpt++;
					break;
				}
			}
		}
		return cpt;
	}

}
