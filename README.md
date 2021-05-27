# Lagerverwaltung -- Prüfungsaufgabe für den DAAD-Stipendientest des INAI.kg 2020

## Aufgabe

Entwickeln Sie ein einfaches Informationssystem für ein Warenlager in Java. Als Ausgangspunkt erhalten Sie dieses UML-Klassendiagramm:

![](https://github.com/fgr/lagerverwaltung-starter-project-inaikg-2020/blob/master/lagerverwaltung-1.png "UML-Klassendiagramm Lagerverwaltung")

## Anforderungen an die Lagerverwaltung

Die Software soll folgende Funktionen bieten:

- Die Software soll dazu geeignet sein, einen Lagerbestand zu verwalten (u.a. Menge, Preise und Namen von Artikeln). 
- Es sollen sowohl Wareneingänge gebucht als auch Bestellungen ausgeführt werden können. Bei beiden Vorgängen wird jeweils mit verbucht, welcher Mitarbeiter dafür zuständig war und es wird zunächst geprüft, ob der Mitarbeiter berechtigt ist, den Warenbestand zu aktualisieren (Objektvariable berechtigteMitarbeiter in Objekt Lagerverwaltung). Bei der Buchung eines Wareneingangs wird auch jeweils der aktuelle Preis mit übergeben, der dann einheitlich auch für die betreffenden bereits eingelagerten Artikel/Lagerposten gelten soll.
- Bei der Bearbeitung einer Bestellung soll jeweils geprüft werden, ob der Mitarbeiter berechtigt ist, die Bestellung auszulösen (Objektvariable berechtigteMitarbeiter in Objekt Lagerverwaltung), und ob überhaupt genügend Waren vorhanden sind. Die bestellten Waren sind dann natürlich dem Lager zu entnehmen. Bestellungen können (um die Implementierung dieses Projektes einfach zu halten) immer nur vollständig oder gar nicht ausgeführt werden.
- Dateiarbeit: Alle relevanten Geschäftsaktionen (Wareneingang, Bestellungsausführung, Erteilen oder Entziehen von Berechtigungen an Mitarbeiter) sollen jeweils mit dem aktuellen Zeitstempel versehen in eine Datei geschrieben werden.

## Testgetriebene Entwicklung

Überlegen Sie, welche (Unit-)Tests für das Überprüfen des korrekten Programmverhaltens notwendig und sinnvoll sind. Entwickenl Sie mit Hilfe von JUnit **umfangreiche Unit-Tests**, mit denen Sie die Korrektheit Ihres Programms nachweisen.

## Rahmenbedingungen

Folgende Rahmenbedingungen **müssen** zum erfolgreichen Bearbeiten dieser Aufgabe unbedingt erfüllt sein:

- Erstellen Sie zusätzlich zum Projekt-Quellcode auch ein Erklär-Video (von ca. fünf bis ca. zehn Minuten Länge), in dem Sie Ihre Entwicklungsentscheidungen beschreiben und auf Ihre Überlegungen zum Design und zur Implementierung eingehen.
- **Abgabetermin**: 21. Mai, 23 Uhr CEST (Mitteleuropäische Sommerzeit)
- Die Abgabe wird ausschließlich über das entsprechende GitHub-Repository bis zum angegebenen Abgabedatum akzeptiert. Machen Sie die finale Version mit einem entsprechenden Commit-Kommentar kenntlich. Ansonsten wird die letzte vor Ablauf der Frist hochgeladene Version zur Bewertung herangezogen.
- Das Übernehmen von fremdem Quell-Code, ohne diesen zu kennzeichnen, gilt als Plagiat und wird als Betrugsversuch mit der Nichtanerkennung dieser Leistung bewertet.
- JavaDoc-Kommentare: Alle Klassen sowie `public`-Methoden sollen mit vollständigen Javadoc-Kommentaren inkl. der Nutzung von `@param` und `@return` beschrieben werden.
- umfangreiche JUnit-Tests
- Maven:
  - Verwenden Sie für das Set-Up, das Bauen und die Ausführung des Projektes _Maven_
  - Ihre Tests müssen automatisch mit Maven ausgeführt werden können!
