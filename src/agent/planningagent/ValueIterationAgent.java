package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration et choisit
 * ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {

    /**
     * discount facteur
     */
    protected double gamma;

    /**
     * fonction de valeur des etats
     */
    protected HashMap<Etat, Double> V;

    /**
     *
     * @param gamma
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        this.gamma = gamma;

        this.V = new HashMap<Etat, Double>();
        for (Etat etat : this.mdp.getEtatsAccessibles()) {
            V.put(etat, 0.0);
        }
    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);

    }

    /**
     *
     * Mise a jour de V: effectue UNE iteration de value iteration (calcule
     * V_k(s) en fonction de V_{k-1}(s')) et notifie ses observateurs. Ce n'est
     * pas la version inplace (qui utilise la nouvelle valeur de V pour mettre a
     * jour ...)
     */
    @Override
    public void updateV() {
        //delta est utilise pour detecter la convergence de l'algorithme
        //Dans la classe mere, lorsque l'on planifie jusqu'a convergence, on arrete les iterations        
        //lorsque delta < epsilon 
        //Dans cette classe, il  faut juste mettre a jour delta 
        this.delta = 0.0;
        //*** VOTRE CODE
        
        vmin = 999;
        vmax = -999;

        double max = -999, sum = 0, delta_tmp;
        HashMap<Etat, Double> V_prec = (HashMap<Etat, Double>) this.V.clone();
        for (HashMap.Entry<Etat, Double> entry : this.V.entrySet()) {
            Etat s = entry.getKey();
            for (Action a : this.mdp.getActionsPossibles(s)) {
                try {
                    for (HashMap.Entry<Etat, Double> voisin : this.mdp.getEtatTransitionProba(s, a).entrySet()) {
                        sum += voisin.getValue() * (this.mdp.getRecompense(s, a, voisin.getKey()) + gamma * V_prec.get(voisin.getKey()));
                    }
                    if (sum > max) {
                        max = sum;
                    }
                    sum = 0;
                } catch (Exception ex) {
                    Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (max > vmax) {
                vmax = max;
            }
            if (max < vmin){
                vmin = max;
            }
            
            delta_tmp = Math.abs(this.V.get(s) - max);
            if (delta_tmp > this.delta) {
                this.delta = delta_tmp;
            }
            entry.setValue(max);
            
            max = 0;
        }

        //mise a jour de vmax et vmin utilise pour affichage du gradient de couleur:
        //vmax est la valeur max de V pour tout s 
        //vmin est la valeur min de V pour tout s
        // ...
        //******************* laisser cette notification a la fin de la methode	
        this.notifyObs();
    }

    /**
     * renvoi l'action executee par l'agent dans l'etat e Si aucune actions
     * possibles, renvoi Action2D.NONE
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
        List<Action> actions = this.getPolitique(e);
        if(actions.isEmpty()){
            return Action2D.NONE;
        } else {
            int rand = (int) (Math.random()*actions.size());
            return actions.get(rand);
        }
    }

    @Override
    public double getValeur(Etat _e) {
        //Renvoie la valeur de l'Etat _e, c'est juste un getter, ne calcule pas la valeur ici
        //(la valeur est calculee dans updateV
        //*** VOTRE CODE

        return this.V.get(_e);
    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans etat (plusieurs actions
     * sont renvoyees si valeurs identiques, liste vide si aucune action n'est
     * possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        //*** VOTRE CODE

        // retourne action de meilleure valeur dans _e selon V, 
        // retourne liste vide si aucune action legale (etat absorbant)
        List<Action> returnactions = new ArrayList<Action>();
        HashMap<Action, Double> actionsRetenues = new HashMap<Action, Double>();
        double sum = 0, max = -999;
        for (Action a : this.mdp.getActionsPossibles(_e)) {
            try {
                for (HashMap.Entry<Etat, Double> voisin : this.mdp.getEtatTransitionProba(_e, a).entrySet()) {
                    sum += voisin.getValue() * (this.mdp.getRecompense(_e, a, voisin.getKey()) + gamma * this.V.get(voisin.getKey()));
                }
                if (sum >= max) {
                    max = sum;
                    actionsRetenues.put(a, max);
                }
                sum = 0;
            } catch (Exception ex) {
                Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for (HashMap.Entry<Action, Double> action : actionsRetenues.entrySet()) {
            if(action.getValue() == max){
                returnactions.add(action.getKey());
            }
        }

        return returnactions;

    }

    @Override
    public void reset() {
        super.reset();
        //reinitialise les valeurs de V 
        //*** VOTRE CODE
        
        for (Map.Entry<Etat, Double> entry:this.V.entrySet()){
			entry.setValue(0.0);
        }

        this.notifyObs();
    }

    public HashMap<Etat, Double> getV() {
        return V;
    }

    public double getGamma() {
        return gamma;
    }

    @Override
    public void setGamma(double _g) {
        System.out.println("gamma= " + gamma);
        this.gamma = _g;
    }

}
