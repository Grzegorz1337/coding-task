# coding-task
Coding task related to recruitment process

## Perquisitions
[Docker engine](https://www.docker.com/products/docker-desktop/)

## To download and run this project, please follow these steps

1. git clone https://github.com/Grzegorz1337/coding-task.git
2. cd coding-task
3. cp .env.examople .env
4. Set new database name and root password in .env
5. docker-compose build
6. docker-compose up

## Application info

Coding task will run on port 8080. Available route mapping is only enabled for /award-points using POST
When called, expects JSON in format:

```
{
    "amount":{floating point number}
}
```

And returns JSON in format:
```
{
    "amount": {floating point number}, 
    "awardPoints": {number},
    "transactionDate": {date of transaction}
}
```

When called, it will also create a database entry for this purchase.

## To browse/analyze/delete/export data 

Use included in docker-compose adminer, available by default on port 9000.


## Tests

Tests are executed automatically with building application docker image, however those can be executed
with command
```
./gradlew build
```


For thresholds I've been thinking about creating yaml resource and later mapping it into
nice set/map, however I think that keeping thresholds in static class is for now sufficient.
Also as we have static class, nothing stands on way to convert it to adapter for such yaml
file (keeping class static as it was, and reading resource in static block).

If you have any questions/suggestions, feel free to contact me, or hr representative.