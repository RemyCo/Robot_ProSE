/**
 * @file IPAddressValidator.java
 *
 * @brief La classe IPAddressValidator permet de valider le format d’une adresse IPv4.
 *
 * @author mkyong
 *
 * @copyright https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 */

package fr.eseo.i2.prose.ea1.whereisrob.communication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La classe IPAddressValidator permet de valider le format d’une adresse IPv4.
 */
public class IPAddressValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     * Le constructeur de la classe
     */
    public IPAddressValidator(){
        pattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    /**
     * Permet de valider le format de l’adresse IPv4 passé en argument
     *
     * @param ip L'adresse IPv4
     * @return La valdité du format de l'addresse IPv4
     */
    public boolean validate(final String ip){
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

} // End of class
// https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/

