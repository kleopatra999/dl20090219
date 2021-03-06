package de.liga.dart.ligateam.service;

import de.liga.dart.common.service.Service;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.ligateam.model.TeamWunsch;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Ligaklasse;
import de.liga.dart.model.Ligateam;
import de.liga.dart.model.Spielort;
import de.liga.dart.ligaklasse.model.LigaklasseFilter;

import java.util.List;
import java.util.Map;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:24:41
 */
public interface LigateamService extends Service {
    /**
     * Speichert das team und sein spiel, bzw.
     * l�scht das ligateamspiel, falls es entfernt wurde!
     * insert / update
     *
     * @param team
     */
    void saveLigateam(Ligateam team) throws DartValidationException;

    void saveLigateam(Ligateam team, List<TeamWunsch> wuensche)
            throws DartValidationException;

    Ligateam findLigateamById(long teamid);

    /**
     * all order by team name
     *
     * @param liga - liga zum Filtern, null fuer ALLE Teams (aller Ligen)
     * @return
     */
    List<Ligateam> findAllTeamsInLiga(Liga liga);

    /**
     * @param liga
     * @param klasse
     * @param ort
     * @param keineGruppe - false: keine Auswirkung, true: nur Teams, die derzeit keinem LigateamSpiel zugeordnet sind
     * @return
     */
    List<Ligateam> findTeamsByLigaKlasseOrt(Liga liga, LigaklasseFilter klasse,
                                            Spielort ort, boolean keineGruppe);

    /**
     * Volltextsuche nach teamnamen
     * @param liga   -  leer (alle Ligen) oder eine Liga
     * @param pattern - Namensbestandteil (nicht case-sensitiv)
     * @return
     */
    List<Ligateam> findTeamsLikeNameByLiga(String pattern, Liga liga);

    List<Ligateam> findAllTeams();

    /**
     * @param aLiga
     * @param validate - true: Pr�fung, ob Team in keiner Gruppe eingeteilt. falls doch: Exception
     * @throws DartException
     * @throws DartValidationException (wenn validate:true und validierung fehlgeschlagen)
     */
    void deleteLigateam(Ligateam aLiga, boolean validate) throws DartException;

    /**
     * finde alle Kandidaten fuer die Auswahl in der Wunschlist,
     * d.h. alle teams in der gleichen liga, die noch NICHT
     * in der Wunschlist des angegenen Teams sind (und natuerlich
     * auch nicht das angeg. Team selbst)
     *
     * @param filterKlasse - eine klasse oder null (zum filtern des ergebnisses)
     * @param filterLiga   - eine liga oder null (zum filtern des ergebnisses)
     * @param team         - nie null
     * @return list mit anderen teams
     */
    List<Ligateam> findWunschListCandidates(Ligateam team, Liga filterLiga,
                                            Ligaklasse filterKlasse);

    /**
     * Alle LigaTeam Wuensche der angeg. Liga loeschen
     * @param liga - wenn leer, werden ALLE Ligen bearbeitet!!
     */
    void deleteAllLigateamWunsch(Liga liga);

    String queryTeamInfos(Ligateam team);
}
