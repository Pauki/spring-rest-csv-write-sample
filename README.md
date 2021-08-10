# spring-rest-csv-write-sample
Sample Spring Boot app with one REST service (contact) - service retrieves data from query parameters and save it as new line in CSV file. Duplicates data (exists in CSV file) will not be saved again.

Location, name and csv separator should be changed via application.properties.

Application is accesible after start on localhost and port 8080, test it using http://localhost:8080/contact?firstName=Franta&lastName=Nov%C3%A1k&email=franta.novak@email.cz
