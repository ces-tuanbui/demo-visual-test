package com.example.javaserver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@RestController
public class TestController {
  @GetMapping("/")
  public String getString() {

    return "string";
  }

  @PostMapping("/upload")
  public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
    NodeList locList = null;
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file.getInputStream());
      locList = doc.getElementsByTagName("loc");

      for (int i = 0; i < locList.getLength(); i++) {
        Element locElement = (Element) locList.item(i);
        String url = locElement.getTextContent();
        System.out.println("URL: " + url);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Convert NodeList to List<String>
    List<String> locStrings = new ArrayList<>();
    for (int i = 0; i < locList.getLength(); i++) {
      Node node = locList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        locStrings.add(element.getTextContent());
      }
    }

    // Convert List<String> to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      String json = objectMapper.writeValueAsString(locStrings);
      // Write JSON to a file
      File jsonFile = new File("../node-server/urls.json");
      objectMapper.writeValue(jsonFile, locStrings);

      System.out.println("JSON file created: " + jsonFile.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      // Replace "path/to/myscript.sh" with the actual path to your Bash script
      String scriptPath = "script.sh";

      ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", scriptPath);
      Process process = processBuilder.start();

      // Đọc đầu ra từ Bash script
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }

      // Wait for the process to finish
      int exitCode = process.waitFor();
      System.out.println("Exit Code: " + exitCode + "\nOutput:\n" + output.toString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

//    // Đường dẫn đến thư mục chứa lệnh npm
//    String workingDirectory = "/home/ces-user/workspace/demo-visual-test/node-server/";
//
//    // Lệnh npm cần chạy
//    String npmCommand = "npm run g:spec";
//
//    // Gọi hàm thực thi lệnh npm
//    executeCommand(workingDirectory, npmCommand);
    return ResponseEntity.ok("File uploaded successfully");
  }

  private static InputStream readXmlFileIntoInputStream(final String fileName) {
    return TestController.class.getClassLoader().getResourceAsStream(fileName);
  }

  private static void executeCommand(String workingDirectory, String command) {
    ProcessBuilder processBuilder = new ProcessBuilder();

    // Đặt thư mục làm việc cho lệnh
    processBuilder.directory(new File(workingDirectory));

    // Tách lệnh thành các thành phần để thực thi
    String[] commandArray = command.split("\\s+");

    // Đặt lệnh và các đối số
    processBuilder.command(commandArray);

    try {
      // Thực thi lệnh
      Process process = processBuilder.start();

      // Đọc đầu ra từ quá trình
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }

      InputStream errorStream = process.getErrorStream();
      InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
      BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);

      String linex;
      System.out.println("Error Output:");
      while ((linex = errorBufferedReader.readLine()) != null) {
        System.out.println(linex);
      }

      // Chờ quá trình hoàn thành
      int exitCode = process.waitFor();
      System.out.println("Lệnh đã hoàn thành với mã thoát: " + exitCode);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
