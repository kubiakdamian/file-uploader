# File uploader - multithreaded

Projekt zrealizowany na potrzeby przedmiotu "Programowanie współbieżne".

## Uruchamianie projektu

Aby poprawnie uruchomić projekt, najpierw należy go zbudować poleceniem:

```
mvn clean install
```

A następnie z folderu głównego uruchomić:

Serwer:
```
java -jar ./server/target/server.jar 9090
```

Klient:
```
java -jar ./client/target/client.jar localhost 9090
```

## Funkcjonalności

### Serwer

* Odbieranie wiadomości klienta.
* Wysyłanie odpowiedzi do klienta.
* Trzymanie plików klienta w przeznaczonym dla niego folderze.
* Wypisywanie do konsoli aktualnie przetwarzanych operacji.
* Zwracanie klientowi plików znajdujących się na serwerze.
* Obsługa do 3 klientów jednocześnie.

### Klient
* Rejestracja na serwerze.
* Logowanie na serwer.
* Wylogowanie z serwera.
* Wysyłanie plików na serwer.
* Pobieranie plików z serwera.
* Wypisywanie do konsoli aktualnie przetwarzanych operacji.

#

W aplikacji w wielu miejscach pominięto walidację danych, ze względu na to, że nie była ona głównym założeniem projektu. Proces wysyłania plików zastąpiono symulatorem przetwarzania.