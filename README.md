# mnb-arfolyam

Query official currency rates from [MNB](https://www.mnb.hu/), based on the official SOAP client at https://www.mnb.hu/arfolyamok.asmx

### Build
```bash
./mvnw package jib:dockerBuild
```

### Usage

```bash
$ docker run --rm ghcr.io/bonifaido/mnb-arfolyam USD
Querying currency: USD
2023-05-12 -> 340.46
2023-04-14 -> 338.04
```

### License
MIT, see [LICENSE](LICENSE).
