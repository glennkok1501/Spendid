package sg.edu.np.spendid.Utils.Helpers;

import sg.edu.np.spendid.R;

//match currency code to flags icon
public class CountryFlagsHelper {

    public CountryFlagsHelper() {
    }

    public int setIcon(String currency){
        int img;
        switch (currency){
            case "aud":
                img = R.drawable.flag_australia;
                break;

            case "brl":
                img = R.drawable.flag_brazil;
                break;

            case "cad":
                img = R.drawable.flag_canada;
                break;

            case "clp":
                img = R.drawable.flag_chile;
                break;

            case "cop":
                img = R.drawable.flag_colombia;
                break;

            case "czk":
                img = R.drawable.flag_czech_republic;
                break;

            case "dkk":
                img = R.drawable.flag_denmark;
                break;

            case "eur":
                img = R.drawable.flag_european_union;
                break;

            case "hkd":
                img = R.drawable.flag_hong_kong;
                break;

            case "huf":
                img = R.drawable.flag_hungary;
                break;

            case "inr":
                img = R.drawable.flag_india;
                break;

            case "idr":
                img = R.drawable.flag_indonesia;
                break;

            case "ils":
                img = R.drawable.flag_israel;
                break;

            case "jpy":
                img = R.drawable.flag_japan;
                break;

            case "myr":
                img = R.drawable.flag_malaysia;
                break;

            case "mxn":
                img = R.drawable.flag_mexico;
                break;

            case "twd":
                img = R.drawable.flag_taiwan;
                break;

            case "nzd":
                img = R.drawable.flag_new_zealand;
                break;

            case "nok":
                img = R.drawable.flag_norway;
                break;

            case "php":
                img = R.drawable.flag_philippines;
                break;

            case "pln":
                img = R.drawable.flag_poland;
                break;

            case "gbp":
                img = R.drawable.flag_united_kingdom;
                break;

            case "cny":
                img = R.drawable.flag_china;
                break;

            case "ron":
                img = R.drawable.flag_romania;
                break;

            case "rub":
                img = R.drawable.flag_russia;
                break;

            case "sar":
                img = R.drawable.flag_saudi_arabia;
                break;

            case "sgd":
                img = R.drawable.flag_singapore;
                break;

            case "zar":
                img = R.drawable.flag_south_africa;
                break;

            case "krw":
                img = R.drawable.flag_south_korea;
                break;

            case "sek":
                img = R.drawable.flag_sweden;
                break;

            case "chf":
                img = R.drawable.flag_switzerland;
                break;

            case "thb":
                img = R.drawable.flag_thailand;
                break;

            case "try":
                img = R.drawable.flag_turkey;
                break;

            case "aed":
                img = R.drawable.flag_united_arab_emirates;
                break;

            case "usd":
                img = R.drawable.flag_united_states;
                break;

            default:
                img = R.drawable.ic_launcher_foreground;
        }
        return img;

    }
}
