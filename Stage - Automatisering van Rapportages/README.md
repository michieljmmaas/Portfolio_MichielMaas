# Werkstage - Java Developer
Ik heb een half jaar stage gelopen bij het Software bedrijf OIS. Hier heb ik hun rapportages geautomatiseerd. 

De applicatie runt op Java Spring. Het heeft een Dashboard waar aanpassingen gemaakt kunnen worden aan de applicatie. De gegevens worden uit een PSQL database gehaald en opgeslagen in een NoSQL Database. De geformatteerd gegevens worden vervolgens in een Thymeleaf Template gezet en daarna opgeslagen als PDF. Deze PDF's worden naar de benodigde klanten gestuurd. De applicatie is erg modulair ontworpen en heeft veel features waar ik trots op ben. 

Bijvoorbeeld:

- De tabellen in de overzichten hebben variabele kolommen. Er wordt een HashMap gegeven met de waarden, en er worden kolommen aangemaakt gebaseerd op keys.
- Sommige rapportages hebben een grafiek. Deze wordt door een headless-chrome browser gegenereerd met Chart.js, opgeslagen als .png en dan toegevoegdg. 
- Het Dashboard heeft een security laag die er voor zorgt dat alleen mensen met de goede role bij de gegevens mogen. 
- Gegevens van een van de rapportages wordt ontvangen via mail (.xml bestand attachment), en dan ingelezen in de No-SQL database. Er zit een safe check op deze module die de actie na twee uur herhaalt als de correcte mail niet is gevonden. Als na drie keer proberen, 6 uur later, het nog steeds niet is gelukt houd hij op en print hij informatie op de console. 

Ook bijgevoegd is mijn Stage Verslag, met mijn handelingen stappen en een aantal screenshots met uitleg. 

