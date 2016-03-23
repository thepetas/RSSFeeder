package cz.cvut.panskpe1.rssfeeder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by petr on 3/19/16.
 */
public class DataStorage {

    private static DataStorage instance = new DataStorage();
    private Integer counter = 0;

    private Map<Integer, Article> articles = new HashMap<>();

    private DataStorage() {
        for (int i = 0; i < 3; i++) {
            addArticles();
        }

    }

    public static DataStorage getInstance(){
        return instance;
    }


    private void addArticles() {
        articles.put(counter,
                new Article(counter++, "Překvapení, nový Android vylepšil displej. Sony teď funguje plně ve 4K",
                        "http://mobil.idnes.cz/sony-xperia-z5-premium-android-6-0-4k-displej-fj5-/mob_sonyericsson.aspx?c=A160317_181922_mob_sonyericsson_vok#utm_source=rss&utm_medium=feed&utm_campaign=mobil&utm_content=main",
                        "Nestává se moc často, že by aktualizace softwaru dovedla vylepšit displej. Ale u Sony Xperia Z5 Premium tomu tak podle všeho je. Telefon konečně dostal Android 6.0 Marsmallow a díky tomu už je schopen využít své vysoké 4K rozlišení naplno.")
        );

        articles.put(counter,
                new Article(counter++, "Starší lumie se dočkaly. Upgrade na mobilní Windows 10 není úplná hračka",
                        "fdafdafad fad fakldhalkd",
                        "O aktualizaci lumií s Windows Phone 8.1 na nejnovější mobilní windows informoval Microsoft současně s představením špičkových lumií loni v říjnu. V původně avizovaný termín pak oznámil její odklad. Uživatelé se upgradu dočkali až nyní, někteří však mají smůlu. Navíc proces není tak snadný jako u konkurenčních systémů.")
        );
        articles.put(counter,
                new Article(counter++, "Skryté reklamy se neštítí. Samsung ovládl seriál a odstrčil Apple",
                        "IDNES.CZ/blasbalbalbalbslkjlakjfla",
                        "Cílená propagace produktů v seriálech a filmech je již standardem, producenti díky ní snižují náklady. Některých produktů si diváci všimnou jen výjimečně, jiné jsou jim naopak řádně naservírované. Product placement není cizí ani populárnímu seriálu Dům z karet, prim v něm hrají přední mobilní výrobci.")
        );
        articles.put(counter,
                new Article(counter++, "Jejich posedlost je pasovala na pouhou položku na Wikipedii",
                        "webovastrankaidnes.cz/daffd",
                        "Od pořízení nejslavnější selfie s hollywoodskými hvězdami při Oscarech uplynuly již tři roky. Za tu dobu se autoportréty staly doslova hitem sociálních sítí, mnohým však obyčejný snímek nestačí. Ti, kteří za rozhodnutí pořídit fotku za neobvyklé situace či na netradičním místě, zaplatili životem nebo se zranili, se dostali na webovou encyklopedii.")
        );
    }

    public Collection<Article> getAllArticles() {
        return new ArrayList<>(articles.values());
    }

    public Article getArticle(int id){
        return articles.get(id);
    }

}
