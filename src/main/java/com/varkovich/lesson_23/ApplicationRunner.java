package com.varkovich.lesson_23;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.varkovich.lesson_23.model.Book;
import com.varkovich.lesson_23.model.Root;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Используя библиотеку Jackson, напишите Java-программу, которая вычитывает
 * JSON-файл(books.json) и сохранит данные в коллекцию Java.
 * <p>
 * Конвертируйте данные из этой коллекции в XML-формат с помощью
 * библиотеки JAXB. Сохраните полученный XML результат в файл.
 * <p>
 * Используя любой парсер(DOM,SAX,StAX) распарсите данные в Java приложении
 * и выведите в консоль информацию о книге с наибольшим количеством страниц.
 */
public class ApplicationRunner {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        ;
        JAXBContext context;
        DocumentBuilderFactory factory;

        try {
            List<Book> books = mapper.readValue(new File("books.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Book.class));
            Root root = new Root();
            root.setBookList(books);

            context = JAXBContext.newInstance(Root.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(root, new File("books.xml"));

            factory = DocumentBuilderFactory.newInstance();

            Document doc = factory.newDocumentBuilder().parse(new File("books.xml"));
            doc.getDocumentElement().normalize();

            NodeList booksList = doc.getElementsByTagName("student");
            if (booksList.getLength() == books.size()) {
                Element bookWithBiggestPageAmount = (Element) booksList.item(0);
                for (int i = 1; i < booksList.getLength(); i++) {
                    Element bookElement = (Element) booksList.item(i);
                    if (Integer.parseInt(bookElement.getElementsByTagName("pages").item(0).getTextContent()) >
                            Integer.parseInt(bookWithBiggestPageAmount.getElementsByTagName("pages").item(0).getTextContent())) {
                        bookWithBiggestPageAmount = bookElement;
                    }
                }

                String title = bookWithBiggestPageAmount.getElementsByTagName("title").item(0).getTextContent();
                String author = bookWithBiggestPageAmount.getElementsByTagName("author").item(0).getTextContent();
                int year = Integer.parseInt(bookWithBiggestPageAmount.getElementsByTagName("year").item(0).getTextContent());
                int pages = Integer.parseInt(bookWithBiggestPageAmount.getElementsByTagName("pages").item(0).getTextContent());
                String genre = bookWithBiggestPageAmount.getElementsByTagName("genre").item(0).getTextContent();
                System.out.println("title -> " + title);
                System.out.println("author -> " + author);
                System.out.println("year -> " + year);
                System.out.println("pages -> " + pages);
                System.out.println("genre -> " + genre);
            }

        } catch (IOException e) {
            System.out.println("File with books was not found");
        } catch (JAXBException e) {
            System.out.println("Error creating JAXB context");
        } catch (ParserConfigurationException e) {
            System.out.println("Error parsing file by DOM");
        } catch (SAXException e) {
            System.out.println("SAX exception");
        }
    }
}
