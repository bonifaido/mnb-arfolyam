# mnb-arfolyam

Query official currency rates from [MNB](https://www.mnb.hu/), based on the official SOAP client at https://www.mnb.hu/arfolyamok.asmx

### Build
```bash
./mvnw package jib:dockerBuild
```

### Usage

For a given date:

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

### License
MIT, see [LICENSE](LICENSE).
