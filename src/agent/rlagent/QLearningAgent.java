package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

/**
 * Renvoi 0 pour valeurs initiales de Q
 *
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {

    /**
     * format de memorisation des Q valeurs: utiliser partout setQValeur car
     * cette methode notifie la vue
     */
    protected HashMap<Etat, HashMap<Action, Double>> qvaleurs;
    Environnement env;

    //AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
    //protected HashMap<Pair<Etat,Action>,Double> qvaleurs;
    /**
     *
     * @param alpha
     * @param gamma
     * @param Environnement
     */
    public QLearningAgent(double alpha, double gamma,
            Environnement _env) {
        super(alpha, gamma, _env);
        qvaleurs = new HashMap<Etat, HashMap<Action, Double>>();
        env = _env;

    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e (plusieurs
     * actions sont renvoyees si valeurs identiques) renvoi liste vide si
     * aucunes actions possibles dans l'etat (par ex. etat absorbant)
     *
     */
    @Override
    public List<Action> getPolitique(Etat e) {
        // retourne action de meilleures valeurs dans e selon Q : utiliser getQValeur()
        // retourne liste vide si aucune action legale (etat terminal)
        List<Action> returnactions = new ArrayList<Action>();
        if (this.getActionsLegales(e).size() == 0) {//etat  absorbant; impossible de le verifier via environnement
            System.out.println("aucune action legale");
            return new ArrayList<Action>();

        }

        //*** VOTRE CODE
        double max = this.getValeur(e);

        for (Action a : this.getActionsLegales(e)) {
            double qv = getQValeur(e, a);
            if (qv >= max) {
                returnactions.add(a);
            }
        }

        return returnactions;

    }

    @Override
    public double getValeur(Etat e) {
        //*** VOTRE CODE
        /*if (qvaleurs.get(e) == null) {
            return 0.0;
        } else {
            double max = -999;
            for (HashMap.Entry<Action, Double> entry : qvaleurs.get(e).entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                }
            }
            return max;
        }*/
        
        List<Action> actions = env.getActionsPossibles(e);
        double max = -999999999;
        for(Action a :  actions){
            double qval = this.getQValeur(e, a);
            if(qval > max){
                max = qval;
            }
        }
        return max;
    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE
        if (this.qvaleurs.get(e) != null && this.qvaleurs.get(e).get(a) != null) {
            return this.qvaleurs.get(e).get(a);
        } else {
            return 0.0;
        }
    }

    @Override
    public void setQValeur(Etat e, Action a, double d) {
        //*** VOTRE CODE

        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...
        if (qvaleurs.get(e) == null) {
            qvaleurs.put(e, new HashMap<Action, Double>());
        }

        qvaleurs.get(e).put(a, d);
        
        if (vmin > d) {
            vmin = d;
        }
        if (vmax < d) {
            vmax = d;
        }

        this.notifyObs();

    }

    /**
     * mise a jour du couple etat-valeur (e,a) apres chaque interaction
     * <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement
     * apres avoir realise une action.
     *
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL) {
            System.out.println("QL mise a jour etat " + e + " action " + a + " etat' " + esuivant + " r " + reward);
        }

        double val = ( (1 - alpha) * (getQValeur(e, a)) ) + ( alpha * (reward + gamma * this.getValeur(esuivant)) );
        setQValeur(e, a, val);

    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    @Override
    public void reset() {
        super.reset();
        //*** VOTRE CODE
        qvaleurs = new HashMap<>();
        vmax = 0.0;
        vmin = 0.0;
        this.episodeNb = 0;
        this.notifyObs();
    }

}
