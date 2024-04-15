import mnb.MNBArfolyamServiceSoapImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/*
 MNB Rate query CLI for TAX-ing based on foreign currencies.
 The SOAP Service descriptor is taken from https://www.mnb.hu/arfolyamok.asmx
 */
public class Main {

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    static LocalDate adjustDateIfNeeded(LocalDate date) {
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            date = date.minusDays(1);
        } else if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            date = date.minusDays(2);
        }
        return date;
    }

    static double getRateFromMnb(String currency, LocalDate date) throws Exception {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        mnb.MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        String dateStr = date.format(formatter);

        String resp = service.getExchangeRates(dateStr, dateStr, currency);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(resp.getBytes()));
        NodeList rates = doc.getElementsByTagName("Rate");
        if (rates.getLength() == 0) {
            return -1;
        }

        String rate = rates.item(0).getTextContent().replace(',', '.'); // Make it excel friendly

        return Double.parseDouble(rate);
    }

    static public void main(String[] args) throws Exception {
        String currency = "USD";
        String dateStr = LocalDate.now().format(formatter);

        LocalDate date = LocalDate.parse(dateStr, formatter);

        if (args.length > 0) {
            currency = args[0];
        }
        if (args.length > 1) {
            date = LocalDate.parse(args[1], formatter);
        }

        System.out.println("Querying currency: " + currency);

        date = adjustDateIfNeeded(date);
        double rate = getRateFromMnb(currency, date);

        System.out.println(date + " -> " + rate);

        date = date.minusMonths(1).withDayOfMonth(15);

        date = adjustDateIfNeeded(date);
        rate = getRateFromMnb(currency, date);

        System.out.println(date + " -> " + rate);
    }
}
