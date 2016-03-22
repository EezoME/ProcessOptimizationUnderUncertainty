package main.java.com.eezo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public final class Messaging {
    public static void showMessageDialog(String message) {
        showMessageDialog(message, "info");
    }

    public static void showMessageDialog(String message, String type) {
        int t;
        if (type.equalsIgnoreCase("warn")) {
            t = JOptionPane.WARNING_MESSAGE;
        } else if (type.equalsIgnoreCase("err")) {
            t = JOptionPane.ERROR_MESSAGE;
        } else if (type.equalsIgnoreCase("?")) {
            t = JOptionPane.QUESTION_MESSAGE;
        } else {
            t = JOptionPane.INFORMATION_MESSAGE;
        }
        JOptionPane.showMessageDialog(null, message, "Program message", t);
    }

    public static String showInputDialog(String message) {
        return JOptionPane.showInputDialog(null, message);
    }

    public static void log(String message) {
        log(message, "info");
    }

    public static void log(String message, String type) {
        if (type.equalsIgnoreCase("err")) {
            System.err.println("ERROR: " + message);
        } else if (type.equalsIgnoreCase("warn")) {
            System.out.println("WARN: " + message);
        } else {
            System.out.println("INFO: " + message);
        }
    }


    /**
     * Writes trans. data to XML file.
     * @param fileName a name of XML file
     */
    public static void writeFile(String fileName) {
        //throw new UnsupportedOperationException("XML writer not written yet, sorry :(");

        if (fileName == null || fileName.isEmpty()) {
            showMessageDialog("Введите имя файла в текстовом поле.");
            //tfOutputFileName.grabFocus();
            return;
        }
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);

            Element child = document.createElement("vendors");
            rootElement.appendChild(child);
            for (int i = 0; i < TransData.staticInstance.getVendorsList().size(); i++) {
                Element vendor = document.createElement("vendor");
                child.appendChild(vendor);
                vendor.setAttribute("name", TransData.staticInstance.getVendorsList().get(i));
                vendor.setAttribute("volume", TransData.staticInstance.getVendorsVolumeList().get(i).toString());
            }

            child = document.createElement("customers");
            rootElement.appendChild(child);
            for (int i = 0; i < TransData.staticInstance.getCustomersList().size(); i++) {
                Element customer = document.createElement("customer");
                child.appendChild(customer);
                customer.setAttribute("name", TransData.staticInstance.getCustomersList().get(i));
                customer.setAttribute("volume", TransData.staticInstance.getCustomersVolumeList().get(i).toString());
            }

            child = document.createElement("matrix");
            rootElement.appendChild(child);
            for (int i = 0; i < TransData.staticInstance.getMatrixOfCosts().length; i++) {
                Element row = document.createElement("row");
                String s = "";
                for (int j = 0; j < TransData.staticInstance.getMatrixOfCosts()[i].length; j++) {
                    s += TransData.staticInstance.getMatrixOfCosts()[i][j]+" ";
                }
                row.appendChild(document.createTextNode(s));
                child.appendChild(row);
            }

            child = document.createElement("fuzzy");
            rootElement.appendChild(child);
            for (int i = 0; i < TransData.staticInstance.getFuzzyMatrixOfCosts().length; i++) {
                Element row = document.createElement("row");
                for (int j = 0; j < TransData.staticInstance.getFuzzyMatrixOfCosts()[i].length; j++) {
                    Element cell = document.createElement("cell");
                    String s = "";
                    s += TransData.staticInstance.getFuzzyMatrixOfCosts()[i][j].toParseFormat()+" ";
                    cell.setTextContent(s);
                    row.appendChild(cell);
                }
                child.appendChild(row);
            }

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            log("Exception while writing file: "+e.getLocalizedMessage(), "err");
            return;
        }
        log("Данные в файл '"+fileName+"' записаны успешно.");
    }

    /**
     * Reads trans. data from XML file.<br/>
     * <i>NOTE:</i> if you don't know the structure, which this method can read,
     * try to input some data on the form and write it to the file.
     * @param file an XML file
     */
    public static void readFile(File file){
        Messaging.log("Чтение данных с файла.");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            FileInputStream fin = new FileInputStream(file);
            Document doc = dBuilder.parse(fin, "UTF-8");
            loadNode(doc);
        } catch (FileNotFoundException e){
            log("Файл '"+file.getAbsolutePath()+" не найден.", "err");
            showMessageDialog("Файл '"+file.getAbsolutePath()+" не найден.", "err");
            return;
        } catch (ParserConfigurationException | SAXException | IOException e){
            log("Error while reading file '"+file.getAbsolutePath()+"': "+e.getLocalizedMessage());
            return;
        }
        log("Данные с файла '"+file.getAbsolutePath()+"' прочитаны успешно.");
    }

    /**
     * Reads the necessary nodes of current document.<br/>
     * <i>Used to call recursively, but but this is not applicable for the current read data format.</i>
     * @param document current document
     */
    private static void loadNode(Document document) {
        Element element = document.getDocumentElement();
        if (element.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(element.getNodeName());
        }

        NodeList nodes = element.getChildNodes();
        List<String> vendors = new ArrayList<>();
        List<Integer> vendorsVolume = new ArrayList<>();
        List<String> customers = new ArrayList<>();
        List<Integer> customersVolume = new ArrayList<>();
        int[][] matrix = null;
        TriangularNumber[][] fuzzy = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            NodeList childNodes = nodes.item(i).getChildNodes();
            if (nodes.item(i).getNodeName().equals("vendors")){
                for (int j = 0; j < childNodes.getLength(); j++) {
                    vendors.add(childNodes.item(j).getAttributes().getNamedItem("name").getNodeValue());
                    vendorsVolume.add(Integer.parseInt(childNodes.item(j).getAttributes().getNamedItem("volume").getNodeValue()));
                }
            }
            if (nodes.item(i).getNodeName().equals("customers")){
                for (int j = 0; j < childNodes.getLength(); j++) {
                    customers.add(childNodes.item(j).getAttributes().getNamedItem("name").getNodeValue());
                    customersVolume.add(Integer.parseInt(childNodes.item(j).getAttributes().getNamedItem("volume").getNodeValue()));
                }
            }
            if (nodes.item(i).getNodeName().equals("matrix")){
                matrix = new int[vendors.size()][customers.size()];
                for (int j = 0; j < childNodes.getLength(); j++) {
                    StringTokenizer st = new StringTokenizer(childNodes.item(j).getFirstChild().getNodeValue().trim());
                    int k = 0;
                    while (st.hasMoreTokens()) {
                        matrix[j][k++] = Integer.parseInt(st.nextToken());
                    }
                }
            }
            if (nodes.item(i).getNodeName().equals("fuzzy")){
                fuzzy = new TriangularNumber[vendors.size()][customers.size()];
                NodeList rows = nodes.item(i).getChildNodes();
                for (int j = 0; j < rows.getLength(); j++) {
                    NodeList cells = rows.item(j).getChildNodes();
                    for (int k = 0; k < cells.getLength(); k++) {
                        fuzzy[j][k] = new TriangularNumber(cells.item(k).getFirstChild().getNodeValue());
                    }
                }
            }
            if (TransData.staticInstance == null) {
                TransData.staticInstance = new TransData();
            }
            TransData.staticInstance.setVendorsList(vendors);
            TransData.staticInstance.setVendorsVolumeList(vendorsVolume);
            TransData.staticInstance.setCustomersList(customers);
            TransData.staticInstance.setCustomersVolumeList(customersVolume);
            TransData.staticInstance.setMatrixOfCosts(matrix);
            TransData.staticInstance.setFuzzyMatrixOfCosts(fuzzy);
            /*Node child = nodes.item(i);
            loadNode(child, "-" + prefix);*/
        }
    }
}
