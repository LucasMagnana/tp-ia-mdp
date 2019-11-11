package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{
	
        int M;
        double[] vectPoids;
        Environnement env;
        FeatureFunction featurefunction;
	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		//*** VOTRE CODE
                M = _featurefunction.getFeatureNb();
                vectPoids = new double[M];
                featurefunction = _featurefunction;
                env = _env;
                
		
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
                double qval = 0;
                double[] feature = featurefunction.getFeatures(e,a);
                for(int i=0; i<M; i++){
                    qval += feature[i]*vectPoids[i];
                }
		return qval;

	}
	
	
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant	
		
		//*** VOTRE CODE
                List<Action> tabB = env.getActionsPossibles(esuivant);
                double maxQValB = -999, qValB;
                for(Action b : tabB){
                    qValB = this.getQValeur(esuivant, b);
                    if(qValB > maxQValB){
                        maxQValB = qValB;
                    }
                }
                for(int i=0; i<M;i++){
                    vectPoids[i] = vectPoids[i]+alpha*(reward+gamma*maxQValB-this.getQValeur(e,a))*this.featurefunction.getFeatures(e, a)[i];
                }
		
		
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
	
		//*** VOTRE CODE
		vectPoids = new double[M];
		this.episodeNb =0;
		this.notifyObs();
	}
	
	
}
