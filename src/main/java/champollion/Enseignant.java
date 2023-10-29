package champollion;

import java.util.HashMap;
import java.util.HashSet;

import static java.lang.Math.round;

public class Enseignant extends Personne {
    private final HashSet<Intervention> lesInterventions = new HashSet<Intervention>();

    // Implémentation par une Map
    private final HashMap<UE, ServicePrevu> enseignements = new HashMap<UE, ServicePrevu>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }

    private float equivalentTD(TypeIntervention type, int volumeHoraire) {
        float result = 0f;
        if (type.equals(TypeIntervention.CM)) {
            result += volumeHoraire * 1.5f;
        } else if (type.equals(TypeIntervention.TD)) {
            result += volumeHoraire;
        } else if (type.equals(TypeIntervention.TP)) {
            result += volumeHoraire * 0.75f;
        }
            return result;

    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD" Pour le calcul : 1 heure
     * de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure de TP vaut 0,75h
     * "équivalent TD"
     *
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     */
    public float heuresPrevues() {
        float heures = 0;
        for (UE ue : enseignements.keySet()) {
            heures += heuresPrevuesPourUE(ue);
        }
        return round(heures);
    }


    public boolean enSousService() {
        return heuresPrevues() < 192;
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant dans l'UE spécifiée en "heures équivalent TD" Pour
     * le calcul : 1 heure de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure
     * de TP vaut 0,75h "équivalent TD"
     *
     * @param ue l'UE concernée
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     */
    public float heuresPrevuesPourUE(UE ue) {
        float heures = 0;
        ServicePrevu serv = enseignements.get(ue);
        if (serv != null) {
            heures = equivalentTD(TypeIntervention.CM, serv.getVolumeCM())
                    + equivalentTD(TypeIntervention.TD, serv.getVolumeTD())
                    + equivalentTD(TypeIntervention.TP, serv.getVolumeTP())
            ;
        }
        return round(heures);
    }

    /**
     * Ajoute un enseignement au service prévu pour cet enseignant
     *
     * @param ue       l'UE concernée
     * @param volumeCM le volume d'heures de cours magitral
     * @param volumeTD le volume d'heures de TD
     * @param volumeTP le volume d'heures de TP
     */
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        if (volumeCM < 0 || volumeTD < 0 || volumeTP < 0)
            throw new IllegalArgumentException("les heures sont negatives");

        ServicePrevu serv = enseignements.get(ue);
        if (serv == null) {
            serv = new ServicePrevu(volumeCM, volumeTD, volumeTD);
            enseignements.put(ue, serv);
        } else {
            serv.setVolumeCM(volumeCM + serv.getVolumeCM());
            serv.setVolumeTD(volumeTD + serv.getVolumeTD());
            serv.setVolumeTP(volumeTP + serv.getVolumeTP());
        }
    }


    public void ajouteIntervention(Intervention intervention) {
        if (!enseignements.containsKey(intervention.getUe())) {
            throw new IllegalArgumentException("La matière ne fait pas partie des enseignements");
        }

        lesInterventions.add(intervention);
    }

    public int resteAPlanifier(UE ue, TypeIntervention type) {
        float planifiees = 0f;
        ServicePrevu servprevu = enseignements.get(ue);
        if (servprevu != null) {
            float aPlanifier = servprevu.getVolume(type);

            for (Intervention inter : lesInterventions) {
                if ((ue.equals(inter.getUe())) && (type.equals(inter.getType()))) {
                    planifiees += inter.getDuree();
                }
            }
            return round(aPlanifier - planifiees);
        } else {
            return 0;
        }


    }
}
