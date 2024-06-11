# mnb-arfolyam

Query official currency rates from [MNB](https://www.mnb.hu/), based on the official SOAP client at https://www.mnb.hu/arfolyamok.asmx

It queries by default the rate of the given date (current day if no date given), and the rate of the 15th of the previous month.
It tries to find the correct days if those days are holidays or weekends and no rate is stored for that day.

### Usage

By default, for USD and the current time of day:

```bash
$ docker run --rm -it ghcr.io/bonifaido/mnb-arfolyam
Querying currency: USD
2024-06-11 -> 367.51
2024-05-15 -> 356.69
```

USD for a given date:

```bash
$ docker run --rm ghcr.io/bonifaido/mnb-arfolyam 2024-05-12
Querying currency: USD
2023-05-12 -> 340.46
2023-04-14 -> 338.04
```

For a given currency:

```bash
$ docker run --rm -it ghcr.io/bonifaido/mnb-arfolyam -c EUR 2024-06-09
Querying currency: EUR
2024-06-07 -> 389.64
2024-05-15 -> 386.3
```

# Development

## Build
```bash
./mvnw package jib:dockerBuild
```

## Push
```bash
./mvnw jib:build -Djib.serialize=true
```

### License
MIT, see [LICENSE](LICENSE).
