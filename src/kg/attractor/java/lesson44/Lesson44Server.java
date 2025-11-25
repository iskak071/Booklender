package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.lesson44.manager.DataManager;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.lesson44.models.EmployeeRecord;
import kg.attractor.java.lesson44.models.IssueRecord;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    private final DataManager dataManager = new DataManager();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/books", this::bookListHandler);
        registerGet("/book/\\d+", this::bookDetailsHandler);
        registerGet("/employee/\\d+", this::employeeDetailsHandler);

        registerGet("/css/.*", this::fileHandler);
        registerGet("/images/.*", this::fileHandler);
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));

            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {

            Template temp = freemarker.getTemplate(templateFile);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {


                temp.process(dataModel, writer);
                writer.flush();

                var data = stream.toByteArray();

                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private void bookListHandler(HttpExchange exchange) {
        List<Book> books = dataManager.getAllBooks();

        for (Book book : books) {
            dataManager.findCurrentHolder(book).ifPresentOrElse(
                    holder -> book.setCurrentHolder(holder.getFullName()),
                    () -> book.setCurrentHolder("Available")
            );
        }

        Map<String, Object> data = new HashMap<>();
        data.put("books", books);

        renderTemplate(exchange, "book_list.ftlh", data);
    }

    private void bookDetailsHandler(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            int bookId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));

            Book book = dataManager.getBookById(bookId).orElse(null);
            if (book == null) {
                sendTextData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, "Book not found.");
                return;
            }

            Employee holder = dataManager.findCurrentHolder(book).orElse(null);
            if (holder != null) {
                book.setCurrentHolder(holder.getFullName());
            } else {
                book.setCurrentHolder("Available");
            }

            List<Employee> allEmployees = dataManager.getAllEmployees();

            Map<String, Object> data = new HashMap<>();
            data.put("book", book);
            data.put("employees", allEmployees);
            data.put("holder", holder);

            renderTemplate(exchange, "book_details.ftlh", data);
        } catch (NumberFormatException e) {
            sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Invalid book ID.");
        } catch (Exception e) {
            sendTextData(exchange, ResponseCodes.INTERNAL_ERROR, ContentType.TEXT_PLAIN, "Internal server error.");
        }

    }

    private void employeeDetailsHandler(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        int employeeId;
        try {
            employeeId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
        } catch (NumberFormatException e) {
            sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Invalid Employee ID format.");
            return;
        }

        Employee employee = dataManager.getEmployeeById(employeeId).orElse(null);

        if (employee == null) {
            sendTextData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, "Employee not found.");
            return;
        }

        List<IssueRecord> allRecords = dataManager.getRecordsForEmployee(employeeId);
        List<EmployeeRecord> currentBooks = new java.util.ArrayList<>();
        List<EmployeeRecord> pastBooks = new java.util.ArrayList<>();

        for (IssueRecord record : allRecords) {
            Book book = dataManager.getBookById(record.getBookId()).orElse(null);

            if (book != null) {
                EmployeeRecord empRecord = new EmployeeRecord(book, record);

                if (empRecord.isCurrentlyBorrowed()) {
                    currentBooks.add(empRecord);
                } else {
                    pastBooks.add(empRecord);
                }
            }
        }

        Map<String, Object> data = new java.util.HashMap<>();
        data.put("employee", employee);
        data.put("currentBooks", currentBooks);
        data.put("pastBooks", pastBooks);

        renderTemplate(exchange, "employee_details.ftlh", data);
    }

    private void fileHandler(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath().substring(1);
            File file = new File("data", path);

            if (!file.exists() || file.isDirectory()) {
                sendTextData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, "File not found.");
                return;
            }

            ContentType mimeType = ContentType.fromFileName(path);
            byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());
            sendByteData(exchange, ResponseCodes.OK, mimeType, fileBytes);
        } catch (IOException e) {
            sendTextData(exchange, ResponseCodes.INTERNAL_ERROR, ContentType.TEXT_PLAIN, "Internal Server Error.");
        }
    }
}
