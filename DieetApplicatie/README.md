# Dieetapplicatie
Afgelopen jaar ben ik flink gaan diëten. Ik ben in 20 weken zo'n 20 kilo afgevallen. Dit ging door een heel streng dieet te volgen van weinig koolhydraten en kilo calorieën. Ik heb een [infograph](Infograph.png) gemaakt die beschrijft wat ik heb gedaan en heeft nog wat andere leuke feitjes. 

Ik hield eerst alles bij wat ik moest eten in een ExcelSheet, maar dit liep snel uit de hand. Om mij te helpen heb ik daarom een eigen Java Web Applicatie voor me zelf gemaakt. Hier in kon ik bijhouden wat ik kon eten en hoeveel ik aan het afvallen was. 

Deze applicatie gebruikt de volgende technologieën:
 - Java (Spring Boot)
 - Thymeleaf
 - Bootstrap
 - SQL Database
 
## Maaltijd Overzicht
In dit overzicht kon ik invullen wat ik wilde eten. Ik kreeg dan een overzicht van alle voedingswaardenm en kon zien of ik over bepaalde streefwaarden heen ging. 

![VoedingOverzichtDemo](maaltijd_overzicht.gif)

## Gewicht Overzicht
In dit overzicht kon ik mijn gewicht bijhouden. Zo kon ik zien hoe ver ik was, hoeveel ik nog moest afvallen, en hoe hard het ging. De licht grijze lijn is gemaakt door Lineaire Regressie toe te passen op de gegevens, om zo te voorspellen wanneer ik op mijn streef gewicht uitkwam. 

![GewichtOverzichtDemo](gewicht_overzicht.gif)


## Voeding
In dit overzicht kon ik voedingsmiddelen toevoegen en gerrechten maken. De voedingswaarden werden gelijk goed omgezet aan de het aantal porties dat ik eet, en de gerrechten zijn gesorteerd op wanneer ze voor het laatste heb gegeten, zodat ik genoeg variatie had. 
![VoedingOverzichtDemo](voeding_overzicht.gif)


[De GitHub Repo staat Public op mijn profile, dus neem een kijkje!](https://github.com/michieljmmaas/Keto-Michiel )


