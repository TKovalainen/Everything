#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void clearBuffer();
void paavalikko(double saldo);
double saldochekc(double saldo);
double nostotapahtuma(double saldo);

// Main-funktion tarkoitus ohjelmassa on lukea käyttäjän syöttämä tilinumero ja avata tilinumeroa vastaavan niminen tiedosto.
// Tiedostosta luetaan tilitiedot joihin kuuluu tilin pinkoodi ja saldo. Tilinumeron syötön jälkeen käyttäjää pyydetään syöttämään
// pin-koodi ja jos käyttäjän syöttämä pin-koodi on oikea, ohjelma lukee saldon tiedostosta ja avaa päävalikon.
int main()
{
    char tilinro[100];
    FILE *tiedosto;
    int saldo;
    int tulos;
    char pin[100];
    char tarkista[100];

    do{
        printf("Anna tilinumero > ");
        fgets(tilinro, 100, stdin);
        if (tilinro[strlen(tilinro)-1]=='\n')
            tilinro[strlen(tilinro)-1]='\0';
        else
            clearBuffer();
        strcat(tilinro, ".acc");
        if ((tiedosto = fopen(tilinro, "r"))==NULL){
            while ((tiedosto = fopen(tilinro, "r"))==NULL){
                printf("Vaara tilinumero, yrita uudestaan");
                fgets(tilinro, 100, stdin);
                if (tilinro [strlen(tilinro)-1]=='\n')
                    tilinro[strlen(tilinro)-1] = '\0';
                else
                clearBuffer();
                strcat(tilinro, ".acc");
            }
        }
        if ((tiedosto = fopen(tilinro, "r"))!= NULL){
            printf("Anna pinkoodi > ");
            fgets(pin, 100, stdin);
            if (pin[strlen(pin)-1]=='\n')
                pin[strlen(pin)-1]='\0';
        }else
        clearBuffer();

        fgets(tarkista, 100, tiedosto);
        do {
            if (tarkista[strlen(tarkista)-1] == '\n')
                tarkista[strlen(tarkista)-1] = '\0';

            if (tarkista[strlen(tarkista)-1] == '\r')
                tarkista [strlen(tarkista)-1] = '\0';
            if ((tulos = strcmp(pin, tarkista))==0){
                fscanf(tiedosto, "%d", &saldo);
                paavalikko(saldo);
            }else printf("Vaara pinkoodi, yrita uudestaan\n");

        }while (!tulos);

    }while (1);


}
// Päävalikon kautta käyttäjä voi navigoida nostotapahtumaan tai saldon tarkastukseen syöttämällä 1 tai 2. Päävalikon kautta ohjelman voi myös
// lopettaa syöttämällä 0. Jos käyttäjä syöttää jonkun muun luvun kuin 0, 1, 2 tai 3, funktio antaa virheilmoituksen väärästä syötteestä.
// Navigointi tapahtuu integraalisyötöillä ja switch-casen avulla. Ohjelma myös estää kirjaimien ja erikoismerkkien syötön valinnan aikana
// while-loopilla, joka aloittaa toiminna jos int status on esim. 1a.
void paavalikko(double saldo){
    printf("Syota 1 lukeaksesi saldon\nSyota 2 nostaaksesi rahaa\nSyota 3 ladataksesi puhelinliittymaa\nSyota 0 lopettaaksesi\n");
    int valinta = 0;
    char ch;
    int status;
    while ((status = scanf("%d%c", &valinta, &ch)) == 0 || (2 == status && ch != '\n')) {
        clearBuffer();
        printf("Virhe syotteessa, yrita uudestaan > ");
    }
    switch (valinta) {
    case 1: saldochekc(saldo);
            clearBuffer();
        break;
    case 2: nostotapahtuma(saldo);
            clearBuffer();
        break;
    case 3:
        printf("Ei puhelinta\nPalataan paavalikkoon\n");
        paavalikko(saldo);
        clearBuffer();
        break;
    case 0: exit(0);
        break;
    default:
        printf("Virheellinen syote, palataan paavalikkoon\n");
        paavalikko(saldo);
        clearBuffer();
        break;
    }

}

// Saldocheck funktio: Funktion kautta käyttäjä voi tarkastaa jäljellä olevan tilin saldon. Funktio saa parametrinä saldon main-funktiosta
// ja tulostaa sen käyttäjälle. Funktion kautta käyttäjä voi lopettaa ohjelman tai navigoida takaisin päävalikkoon syöttämällä joko 1 tai 0.
// navigointi tapahtuu switch-casen avulla joka syötöstä riippuen avaa joko päävalikon tai lopettaa ohjelman. Jos syöte ei ole 1 tai 0, ohjelma
// aloittaa while-loopin joka pyytää käyttäjää syöttämään uuden luvun.
double saldochekc(double saldo){
    int valinta;
    printf("Saldosi on %.2lf euroa\nSyota 0 palataksesi paavalikkoon\nSyota 1 lopettaaksesi\n", saldo);
    char ch;
    int status;
    while ((status = scanf("%d%c", &valinta, &ch)) == 0 || (2 == status && ch != '\n')) {
    clearBuffer();
    printf("Virhe syotteessa, yrita uudestaan > ");
    }
    switch (valinta){
    case 0:
        paavalikko(saldo);
        clearBuffer();
        break;
    case 1:
        exit(0);
        break;
    default: while (valinta > 1 || valinta < 0){
            printf("Virheellinen syote, yrita uudestaan\n");
            scanf("%d", &valinta);
            if (valinta == 1){
                exit(0);
            }
            if (valinta == 0){
                paavalikko(saldo);
                clearBuffer();
            }
        }
    }
    return saldo;
}

// Nostotapahtuma funktio: Funktion avulla käyttäjä voi nostaa rahaa tilin saldon loppuun asti määrissä jotka ovat jaollisia 10:llä.
// Funktio lisäksi laskee kuinka monta 20:en ja 50:en euron seteliä käyttäjä nostaa. Funtkion alussa käyttäjä antaa summan jonka hän haluaa
// nostaa, jonka jälkeen funktio tarkastaa onko summa hyväksyttävä. Jos summa on hyväksyttävä funktio miinustaa summan saldosta, laskee setelien määrät ja tulostaa
// käyttäjälle setelien määrät, noston määrän ja lopulta palauttaa saldon uuden määrän main-funktioon.
// Funktiosta voi navigoida pois tai funktion kautta voi lopettaa ohjelman heti nostotapahtuman alussa tai lopussa syöttämälle ohjelmalle joko 1 tai 0.
double nostotapahtuma(double saldo){
    int nosto;
    int seteli50;
    int seteli20;
    int i = 0;
    printf("Syota paljon haluat nostaa\nSyota 0 palataksesi paavalikkoon\nSyota 1 lopettaaksesi\n");
    char ch;
    int status;
    while ((status = scanf("%d%c", &nosto, &ch)) == 0 || (2 == status && ch != '\n')) {
        clearBuffer();
        printf("Virhe syotteessa, yrita uudelleen");
    }
    if (nosto == 0){
        paavalikko(saldo);
        clearBuffer();
    }
    if (nosto == 1){
        exit(0);
    }
    if (nosto > 1000 || nosto > saldo){ // maksimisumma = 1000, tai saldoa pienempi //
        while (nosto > 1000 || nosto > saldo){
        printf("Liian suuri summa, syota uusi summa\nSyota 0 palataksesi paavalikkoon\nSyota 1 lopettaaksesi\n");
            while ((status = scanf("%d%c", &nosto, &ch)) == 0 || (2 == status && ch != '\n')) {
                clearBuffer();
                printf("Virhe syotteessa, yrita uudelleen");
            }
        if (nosto == 0){
            paavalikko(saldo);
            clearBuffer();
        }
        if (nosto == 1){
            exit(0);
        }

        }
    }
    if ((nosto <= 19 && nosto >= 2) || nosto < 0){  // minimisumma = 20, ei negatiivisia nostoja//
        while ((nosto <= 19 && nosto >= 2) || nosto < 0){
            printf("Liian pieni summa, syota uusi summa\nSyota 0 palataksesi paavalikkoon\nSyota 1 lopettaaksesi\n");
                while ((status = scanf("%d%c", &nosto, &ch)) == 0 || (2 == status && ch != '\n')) {
                    clearBuffer();
                    printf("Virhe syotteessa, yrita uudelleen");
                }
            if (nosto == 0){
                paavalikko(saldo);
                clearBuffer();
            }
            if (nosto == 1){
                exit(0);
            }
        }
    }else;
    if (nosto % 10 != 0 || nosto == 30){
        printf("Virheellinen summa, palataan nostotapahtuman alkuun\n");
        nostotapahtuma(saldo);
    }

    if (nosto % 10 == 0){
        i -= nosto;
        seteli50 = nosto / 50 - (nosto > 50 && nosto % 50 % 20); // laskee setelien maarat
        seteli20 = (nosto - seteli50 * 50) / 20;
    }


    saldo = saldo - nosto;
    int valinta;
    printf("Nostit %d 20e setelia ja %d 50e setelia eli %d euroa\nSyota 0 palataksesi paavalikkoon\nSyota 1 lopettaaksesi\n",seteli20, seteli50, nosto);
    while ((status = scanf("%d%c", &valinta, &ch)) == 0 || (2 == status && ch != '\n')) {
    clearBuffer();
    printf("Virhe syotteessa, yrita uudestaan > ");
    }
    switch (valinta){
    case 0:
        paavalikko(saldo);
        clearBuffer();
        break;
    case 1:
        exit(0);
        break;
    default: while (valinta > 1 || valinta < 0){
            printf("Virheellinen syote, yrita uudestaan\n");
            scanf("%d", &valinta);
            if (valinta == 1){
                exit(0);
            }
            if (valinta == 0){
                paavalikko(saldo);
            }
        }
}
    return saldo;

}

// While-loop joka tyhjentää turhat uusirivi merkit muistista.
void clearBuffer(){

    while (fgetc(stdin)!='\n');

}


