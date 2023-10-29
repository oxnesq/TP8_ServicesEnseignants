package champollion;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChampollionJUnitTest {
	Enseignant untel;
	UE uml, java;
		
	@BeforeEach
	public void setUp() {
		untel = new Enseignant("untel", "untel@gmail.com");
		uml = new UE("UML");
		java = new UE("Programmation en java");		
	}
	

	@Test
	public void testNouvelEnseignantSansService() {
		assertEquals(0, untel.heuresPrevues(),
                        "Un nouvel enseignant doit avoir 0 heures prévues");
	}
	
	@Test
	public void testAjouteHeures() {
                // 10h TD pour UML
		untel.ajouteEnseignement(uml, 0, 10, 0);

		assertEquals(10, untel.heuresPrevuesPourUE(uml),
                        "L'enseignant doit maintenant avoir 10 heures prévues pour l'UE 'uml'");

                // 20h TD pour UML
                untel.ajouteEnseignement(uml, 0, 20, 0);
                
		assertEquals(10 + 20, untel.heuresPrevuesPourUE(uml),
                         "L'enseignant doit maintenant avoir 30 heures prévues pour l'UE 'uml'");		

	}

	@Test
	void ajoutHeuresNegatives() {
		try {
			untel.ajouteEnseignement(uml, -5, 0, 0);
			fail("La valeur est négative mais a ete ajt");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	void calculService() {
		// 10hTD -> 10 équivalent TD
		untel.ajouteEnseignement(uml, 0, 10, 0);
		// 20hCM -> 30 équivalent TD
		untel.ajouteEnseignement(uml, 20, 0, 0);
		// 20hTP -> 15 équivalent TD
		untel.ajouteEnseignement(java, 0, 0, 20);

		assertEquals(10+30+15, untel.heuresPrevues(),"Les équivalents TD ne calculent pas bien");
	}
	
}
