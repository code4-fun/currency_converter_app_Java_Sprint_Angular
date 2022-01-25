package com.backend.service;

import com.backend.domain.Currency;
import com.backend.domain.ExRateToRub;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataProcessingService {
  @NonNull CurrencyService currencyService;
  @NonNull ExRateToRubService exRateToRubService;

  /**
   * Parses exchange rate data from the official cbr website and
   * saves it to the database.
   * @throws IOException
   */
  public void parse() throws IOException {
    try {
      InputStream is = new URL("http://www.cbr.ru/scripts/XML_daily.asp").openStream();
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(is);

      while (reader.hasNext()) {
        reader.next();
        if(reader.isStartElement() && reader.getLocalName().equals("ValCurs")){
          LocalDate dateOfRate = Utilities
              .convertStringToLocalDate(reader.getAttributeValue(null, "Date"));

          boolean endElementValCurs = false;
          do{
            reader.next();
            if(reader.isStartElement() && reader.getLocalName().equals("Valute")){

              String id = reader.getAttributeValue(null, "ID");
              String numCode = null;
              String charCode = null;
              Long nominal = null;
              String name = null;
              Double value = null;

              boolean endElementValute = false;
              do{
                reader.next();
                if(reader.isStartElement() && reader.getLocalName().equals("NumCode")){
                  reader.next();
                  numCode = reader.getText();
                }
                if(reader.isStartElement() && reader.getLocalName().equals("CharCode")){
                  reader.next();
                  charCode = reader.getText();
                }
                if(reader.isStartElement() && reader.getLocalName().equals("Nominal")){
                  reader.next();
                  nominal = Long.parseLong(reader.getText());
                }
                if(reader.isStartElement() && reader.getLocalName().equals("Name")){
                  reader.next();
                  name = reader.getText();
                }
                if(reader.isStartElement() && reader.getLocalName().equals("Value")){
                  reader.next();
                  String stringValue = reader.getText();
                  value = Double.parseDouble(stringValue.replace(",", "."));
                }
                if (reader.isEndElement()) {
                  endElementValute = reader.getLocalName().equals("Valute");
                }
              }while(!endElementValute);

              Currency currency = saveCurrencyToDb(id, numCode, charCode, name);
              saveExRateToRubToDb(dateOfRate, nominal, value, currency);
            }
            if (reader.isEndElement()) {
              endElementValCurs = reader.getLocalName().equals("ValCurs");
            }
          }while (!endElementValCurs);
        }
      }
      reader.close();
      is.close();
    } catch ( XMLStreamException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves Currency object with given parameters to the database if
   * there is no Currency with provided id in the database.
   * @param id id of the currency
   * @param numCode numCode of the currency
   * @param charCode charCode of the currency
   * @param name name of the currency
   * @return currency created of existing in the database
   */
  private Currency saveCurrencyToDb(String id, String numCode,
                                    String charCode, String name){
    Currency currency = Currency
        .builder()
        .id(id)
        .numCode(numCode)
        .charCode(charCode)
        .name(name)
        .build();
    return currencyService.createCurrency(currency);
  }

  /**
   * Saves ExRateToRub object with given parameters to the database if
   * there is no ExRateToRub with provided id in the database.
   * @param dateOfRate date of rate
   * @param nominal nominal
   * @param value rubles for the nominal of the currency
   * @param currency Currency
   * @return ExRateToRub created of existing in the database
   */
  private ExRateToRub saveExRateToRubToDb(LocalDate dateOfRate, Long nominal,
                                          Double value, Currency currency){
    ExRateToRub exRateToRub = ExRateToRub
        .builder()
        .date(dateOfRate)
        .nominal(nominal)
        .value(value)
        .currency(currency)
        .build();
    return exRateToRubService.createExRateToRub(exRateToRub);
  }
}