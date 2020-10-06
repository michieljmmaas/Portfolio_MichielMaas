package io.algoritme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.debug.DummyDebugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;

/**
 * * Dit algertime gebruikt een genetische methode om het korst mogelijk pad te
 * bereken tussen alle steden op de kaart. Bij het maken van dit algeritme heb
 * ik veel gebruik gemaakt van de volgende Tutorial:
 * https://github.com/CodingTrain/website/tree/master/CodingChallenges/CC_035_TSP/CC_35.4_TSP_GA
 * die bij de volgende youtube tutorial hoort:
 * https://www.youtube.com/watch?v=BAejnwN4Ccw&list=PLRqwX-V7Uu6ZncE7FtTEn53sK-BjR2aLf
 *
 * Er is nog wel veel casting om dat ik met ArrayListwerk ivp lists, maar dat is
 * nu nog niet zo erg
 * 
 * @author Michiel
 *
 */

public class StedenTourAlgortimeImpl implements StedenTourAlgoritme, Debuggable {

	private Debugger debug = new DummyDebugger();
	private ArrayList<Stad> shortestPath;
	private ArrayList<Afstand> afstanden;
	private ArrayList<StedenTourHelper> population;
	private int populationCount;
	private int generaties;
	private double mutationRate;
	private int record;
	private Kaart kaart;
	private ArrayList<Stad> steden;

	@Override
	public List<Stad> bereken(Kaart kaart, List<Stad> stedenLists) {
		//Eerst initializeren we alle variabelen
		initialize(kaart, stedenLists);

		// Bereken a* pad van stad naar stad voor minder algeritme gebruik
		calcDistanceIndividual(steden, kaart);

		// Random setup
		setUp(steden);

		// Loop die door blijft rekenen. Voor nu is dit een finite getal, later
		// misschien op bijstellen
		for (int i = 0; i < generaties; i++) {

			// Berken fitness zodat je weet je mee werkt
			calculateFitness(population, steden);

			// Normalizeer fitness zodat je het omkan zetten in kans
			normalizeFitness(population);

			// Maak de volgende generatie
			nextGeneration(population);

		}

		return shortestPath;
	}

	
	/**
	 * In deze methode maken we alle variabelen aan die gebruikt zullen worden in het algoritme
	 * @param kaart, de gegeven kaart waar op de berekening gemaakt moet worden
	 * @param stedenLists, een lijst met steden op de kaart
	 */
	public void initialize(Kaart kaart, List<Stad> stedenLists) {
		afstanden = new ArrayList<Afstand>();
		populationCount = 1000; // Hier kunnen we nog mee wisselen
		generaties = 75; // Arbitrair gekozen, later nog naar kijken hoe dit het best kan
		mutationRate = 0.1; // Arbitrait
		record = Integer.MAX_VALUE;
		population = new ArrayList<StedenTourHelper>();
		this.kaart = kaart;

		steden = new ArrayList<>(stedenLists.size());
		steden.addAll(stedenLists);
	}
	
	
	/**
	 * Deze method genereert een set van willekeurige volgordes van steden om mee te
	 * beginnen
	 * 
	 * @param steden
	 *            de gegeven lijst met steden die bezoekt moeten worden
	 */
	public void setUp(ArrayList<Stad> steden) {
		for (int i = 0; i < populationCount; i++) {
			ArrayList<Stad> random = new ArrayList<>(steden.size());
			random.addAll(steden);
			Collections.shuffle(random);
			StedenTourHelper sth = new StedenTourHelper(random, Integer.MAX_VALUE);
			population.add(sth);

		}
	}

	/**
	 * Deze methode berekent de de Fitness Score van alle StedenTourHelpers in de
	 * Population
	 * 
	 * @param population,
	 *            de Population. Een List met allemaal verschillende volgordes
	 * @param steden,
	 *            de lijst met steden op deze kaart
	 */
	public void calculateFitness(ArrayList<StedenTourHelper> population, ArrayList<Stad> steden) {
		// Voor elke order in de population worden de tussenliggende paden bij elkaar
		// opgeteld
		for (int i = 0; i < population.size(); i++) {
			int distance = calcDistanceSum(population.get(i).getOrder());
			if (distance < record) {
				record = distance;
				shortestPath = population.get(i).getOrder();
				debug.debugSteden(kaart, shortestPath);
			}
			// Bepaal de fitness van de deze order, en doe dat met een Power zodat de
			// exponentieel oploopt bij de kans berekening
			population.get(i).setFitness(1 / (Math.pow(distance, 8) + 1));
		}

	}

	/**
	 * Om makkelijker te rekenen met de fitness scores, worden alle Fitness Scores
	 * genormalizeerd
	 * 
	 * @param population,
	 *            de gehele population. List met volgordes met steden
	 */
	private void normalizeFitness(ArrayList<StedenTourHelper> population) {
		double sum = 0;
		// Eerst worden alle fitness Scores bij elkaar opgeteld
		for (StedenTourHelper sth : population) {
			sum += sth.getFitness();
		}
		// En daarna worden ze genormalizeerd
		for (StedenTourHelper sth : population) {
			sth.setFitness(sth.getFitness() / sum);
		}

	}

	/**
	 * Deze methode maakt een nieuw set met StedenTourHelpers om daar op door te
	 * rekenen
	 * 
	 * @param population,
	 *            de huidige population
	 * @param prob,
	 *            de mogelijkheid van mutatie
	 */
	private void nextGeneration(ArrayList<StedenTourHelper> population) {
		// Maak een nieuwe lege ArrayList aan
		ArrayList<StedenTourHelper> newPopulation = new ArrayList<StedenTourHelper>();
		// En om een populatie net zo groot te maken, gaan we dit af. Dit is gewoon een
		// forloop die inttereet, maar ik vond dit minder typen
		for (int i = 0; i < population.size(); i++) {
			// In dit gedeelte worden de ouders gekozen. De PickOne methode pakt een parent
			// gebaseerd op hun fitness score, Hoe hoger hoe vaker
			ArrayList<Stad> orderA = pickOne(population);
			ArrayList<Stad> orderB = pickOne(population);

			// Daarna worden er van de parents een random kind gemaakt met de volgordes die
			// zij hadden
			ArrayList<Stad> order = crossOver(orderA, orderB);

			// Een beetje mutatie om door te proberen
			mutate(order, mutationRate);

			// Vervolgende worden de kinderen in een nieuwe lijst gevoegd, en daarna wordt
			// de huidige populatie met ze vervangen
			StedenTourHelper pop = new StedenTourHelper(order, Integer.MAX_VALUE);
			newPopulation.add(pop);
		}

		this.population = newPopulation;

	}

	/**
	 * Deze methode pakt een Volgorde uit de populatie gebaseerd op de fitness score
	 * 
	 * https://www.youtube.com/watch?v=ETphJASzYes - Het is deze video
	 * 
	 * In feiten heeft elk persoon uit de population een kans om gekozen te worden,
	 * dat is de fitness score. Hoe hoger de fitness score, hoe groter de kans dat
	 * je wordt gekozen. Er wordt een random getalen gekozen tussen 0 en 1, en aan
	 * de hand daarvan wordt bepaald welk lid uit de populatie het is zijn. Alle
	 * kansen zijn bij elkaar opgesteld 1, want ze zijn genormalizeerd. Het random
	 * gekozen getal is de kans dat het dat persoon was.
	 * 
	 * @param list,
	 *            de population @return, een volgorde van steden
	 */
	private ArrayList<Stad> pickOne(ArrayList<StedenTourHelper> list) {

		// Eerst wordt er een random getal gepakt tussen 0 en 1
		int index = 0;
		Random random = new Random();
		double r = random.nextDouble();

		// Als de mogelijkheid niet in dit stukje zat, kijken we naar de volgende
		// persoon in de populatie
		while (r > 0) {
			r = r - list.get(index).getFitness();
			index++;
		}

		// Omdat je altijd op telt en niet kan checken of je kans als is geweest, wordt
		// er nog een index van af getrokken.
		index--;
		return list.get(index).getOrder();
	}

	/**
	 * Deze methode voegt twee volgordes samen om te kijken of daar iets goeds uit
	 * komt
	 * 
	 * @param orderA,
	 *            eerste volgorde
	 * @param orderB,
	 *            tweede volgorde
	 * @return een samenvoegsel van de twee volgordes
	 */
	private ArrayList<Stad> crossOver(ArrayList<Stad> orderA, ArrayList<Stad> orderB) {
		// Er wordt een random begin en random eindpunt bedacht voor de eerste array,
		// waar de code direct van wordt overgenomen
		Random r = new Random();
		int size = orderA.size();

		// Kies twee getallen, na elkaar met mistens 1 ertussen
		int start = r.nextInt(orderA.size());
		int end = r.nextInt(orderB.size() - start);
		end = end + start + 1;

		// Dit is een mock stad om ruimtes te vullen - Hier moet ik een goed alternatief
		// voor vinden
		Stad mock = new Stad(Coordinaat.op(0, 0), "Mock");

		// Maak een copy van orderA, want deze zijn vaak grotendeels het zelfde
		ArrayList<Stad> newOrder = new ArrayList<>(orderA.size());
		newOrder.addAll(orderA);

		// Vervang alle steden in de lijst die je wilt ruilen met B voor Mock steden
		// zodat je weet welke je moet hebben
		for (int i = 0; i < end; i++) {
			if (start > i || i > end) {
				newOrder.set(i, mock);
			}
		}

		// Ga nu de lijst weer af en vervang alle mock steden met steden uit lijst b,
		// die nog niet in de overgebleven steden van orderA zitten.
		for (int j = 0; j < size; j++) {
			if (newOrder.get(j).equals(mock)) {
				for (int b = 0; b < size; b++) {
					if (!newOrder.contains(orderB.get(b))) {
						newOrder.set(j, orderB.get(b));
						break; // Hier is een break, anders wordt de lijst anders achteruit ingevuld
					}
				}
			}
		}

		return newOrder;
	}

	/**
	 * Deze methode wissel willekeurig twee steden in de lijst, om te kijken of die
	 * er beter van wordt.
	 * 
	 * @param order
	 * @param mutationRate
	 */
	private void mutate(ArrayList<Stad> order, double mutationRate) {
		Random r = new Random();

		// kijk voor elke stad om je wil kijken wat je wil wisselen
		for (int i = 0; i < order.size(); i++) {
			if (r.nextDouble() < mutationRate) {

				// Kies een stad uit de lijst, en verwissel die met de gene die erop volgt. Als
				// je out of bounds gaat, begint hij weer vooran
				int indexA = r.nextInt(order.size());
				int indexB = (indexA + 1) % order.size();
				Collections.swap(order, indexA, indexB);

			}
		}

	}

	/**
	 * Deze methode berekent de tussen elke mogelijke stad, zodat er maar een keer
	 * een A* check gemaakt hoeft te worden over de steden.
	 * 
	 * @param steden
	 *            De te bezoeken steden
	 * @param kaart
	 *            de Kaart waarom dit gebeurd
	 */
	private void calcDistanceIndividual(ArrayList<Stad> steden, Kaart kaart) {

		SnelstePadAlgortimeImpl alg = new SnelstePadAlgortimeImpl();

		ArrayList<Stad> temp = new ArrayList<Stad>(steden);

		while (temp.size() > 1) {
			Stad start = temp.remove(0);
			for (Stad eind : temp) {
				Pad p = alg.bereken(kaart, start.getCoordinaat(), eind.getCoordinaat());
				Afstand add = new Afstand(start, eind, p.getTotaleTijd());

				// De berekende mogelijkheden gaan in deze array, om te gebruiken als Look-up
				afstanden.add(add);
			}
		}

		// De gaan nu alleen de ene kant op, dus hier maak ik ze de andere kant op
		ArrayList<Afstand> reverseList = new ArrayList<Afstand>();
		for (Afstand a : afstanden) {
			Afstand reverse = new Afstand(a.getEind(), a.getStart(), a.getAfstand());
			reverseList.add(reverse);
		}
		afstanden.addAll(reverseList);

	}

	/**
	 * Deze methode berekent de som van alle afstanden in de volgorde lijst van te
	 * bezoeken steden
	 * 
	 * @param steden,
	 *            een lijst met steden die in deze volgorde worden bezocht
	 * @return De som van deze afstanden
	 */
	private int calcDistanceSum(ArrayList<Stad> steden) {
		int total = 0;
		for (int i = 0; i < steden.size() - 1; i++) {
			for (Afstand a : afstanden) {
				if (steden.get(i).equals(a.getStart()) && steden.get(i + 1).equals(a.getEind())) {
					total = total + a.getAfstand();
				}
			}
		}
		return total;
	}

	@Override
	public void setDebugger(Debugger debugger) {
		this.debug = debugger;

	}

}
