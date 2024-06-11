import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
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
@Command(name = "main")
public class MNBRate {

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Option(name = {"-c", "--currency"}, description = "currency to query")
    private String currency = "USD";

    @Arguments(title = "date")
    private String date = LocalDate.now().format(formatter);

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

    private void run() throws Exception {
        LocalDate date = LocalDate.parse(this.date, formatter);

        System.out.println("Querying currency: " + currency);

        date = adjustDateIfNeeded(date);
        double rate = getRateFromMnb(currency, date);

        System.out.println(date + " -> " + rate);

        date = date.minusMonths(1).withDayOfMonth(15);

        date = adjustDateIfNeeded(date);
        rate = getRateFromMnb(currency, date);

        System.out.println(date + " -> " + rate);
    }

    public static void main(String[] args) throws Exception {
        SingleCommand<MNBRate> parser = SingleCommand.singleCommand(MNBRate.class);
        MNBRate cmd = parser.parse(args);
        cmd.run();
    }
}
