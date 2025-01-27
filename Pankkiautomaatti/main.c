#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void clearBuffer();
void paavalikko(double saldo);
double saldochekc(double saldo);
double nostotapahtuma(double saldo);

// Main-funktion tarkoitus ohjelmassa on lukea k�ytt�j�n sy�tt�m� tilinumero ja avata tilinumeroa vastaavan niminen tiedosto.
// Tiedostosta luetaan tilitiedot joihin kuuluu tilin pinkoodi ja saldo. Tilinumeron sy�t�n j�lkeen k�ytt�j�� pyydet��n sy�tt�m��n
// pin-koodi ja jos k�ytt�j�n sy�tt�m� pin-koodi on oikea, ohjelma lukee saldon tiedostosta ja avaa p��valikon.
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
// P��valikon kautta k�ytt�j� voi navigoida nostotapahtumaan tai saldon tarkastukseen sy�tt�m�ll� 1 tai 2. P��valikon kautta ohjelman voi my�s
// lopettaa sy�tt�m�ll� 0. Jos k�ytt�j� sy�tt�� jonkun muun luvun kuin 0, 1, 2 tai 3, funktio antaa virheilmoituksen v��r�st� sy�tteest�.
// Navigointi tapahtuu integraalisy�t�ill� ja switch-casen avulla. Ohjelma my�s est�� kirjaimien ja erikoismerkkien sy�t�n valinnan aikana
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

// Saldocheck funktio: Funktion kautta k�ytt�j� voi tarkastaa j�ljell� olevan tilin saldon. Funktio saa parametrin� saldon main-funktiosta
// ja tulostaa sen k�ytt�j�lle. Funktion kautta k�ytt�j� voi lopettaa ohjelman tai navigoida takaisin p��valikkoon sy�tt�m�ll� joko 1 tai 0.
// navigointi tapahtuu switch-casen avulla joka sy�t�st� riippuen avaa joko p��valikon tai lopettaa ohjelman. Jos sy�te ei ole 1 tai 0, ohjelma
// aloittaa while-loopin joka pyyt�� k�ytt�j�� sy�tt�m��n uuden luvun.
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

// Nostotapahtuma funktio: Funktion avulla k�ytt�j� voi nostaa rahaa tilin saldon loppuun asti m��riss� jotka ovat jaollisia 10:ll�.
// Funktio lis�ksi laskee kuinka monta 20:en ja 50:en euron seteli� k�ytt�j� nostaa. Funtkion alussa k�ytt�j� antaa summan jonka h�n haluaa
// nostaa, jonka j�lkeen funktio tarkastaa onko summa hyv�ksytt�v�. Jos summa on hyv�ksytt�v� funktio miinustaa summan saldosta, laskee setelien m��r�t ja tulostaa
// k�ytt�j�lle setelien m��r�t, noston m��r�n ja lopulta palauttaa saldon uuden m��r�n main-funktioon.
// Funktiosta voi navigoida pois tai funktion kautta voi lopettaa ohjelman heti nostotapahtuman alussa tai lopussa sy�tt�m�lle ohjelmalle joko 1 tai 0.
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

// While-loop joka tyhjent�� turhat uusirivi merkit muistista.
void clearBuffer(){

    while (fgetc(stdin)!='\n');

}


