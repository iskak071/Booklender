package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.lesson44.manager.DataManager;
import kg.attractor.java.lesson44.manager.SessionManager;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.lesson44.models.EmployeeRecord;
import kg.attractor.java.lesson44.models.IssueRecord;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.Cookie;
import kg.attractor.java.server.ResponseCodes;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private final DataManager dataManager = new DataManager();
    private final SessionManager sessionManager = SessionManager.getInstance();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/books", this::bookListHandler);
        registerGet("/book/\\d+", this::bookDetailsHandler);
        registerGet("/employee/\\d+", this::employeeDetailsHandler);
        registerGet("/register", this::registerGetHandler);
        registerGet("/login", this::loginGetHandler);
        registerGet("/profile", this::profileGetHandler);

        registerGet("/css/.*", this::fileHandler);
        registerGet("/images/.*", this::fileHandler);

        registerPost("/register", this::registerPostHandler);
        registerPost("/login", this::loginPostHandler);

        registerPost("/borrow/\\d+", this::borrowBookHandler);
        registerPost("/return/\\d+", this::returnBookHandler);
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
            dataManager.findCurrentHolder(book).ifPresentOrElse(holder -> book.setCurrentHolder(holder.getFullName()), () -> book.setCurrentHolder("Available"));
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

    private void registerGetHandler(HttpExchange exchange) {
        try {
            File file = new File("data/register.html");
            byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());
            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            sendTextData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, "Register page not found.");
        }
    }

    private void registerPostHandler(HttpExchange exchange) {
        try {
            String formData = new String(exchange.getRequestBody().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            Map<String, String> params = parseFormData(formData);

            String fullName = params.get("fullName");
            String email = params.get("email");
            String password = params.get("password");

            if (fullName == null || email == null || password == null) {
                sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "All fields are required.");
                return;
            }

            if (dataManager.findEmployeeByEmail(email).isPresent()) {
                Map<String, Object> data = new HashMap<>();
                data.put("success", false);
                data.put("email", email);
                renderTemplate(exchange, "register_result.ftlh", data);
                return;
            }

            Employee newEmployee = new Employee();
            newEmployee.setFullName(fullName);
            newEmployee.setEmail(email);
            newEmployee.setPassword(password);

            boolean success = dataManager.addEmployee(newEmployee);

            Map<String, Object> data = new HashMap<>();
            data.put("success", success);
            if (success) {
                data.put("employee", newEmployee);
            }

            renderTemplate(exchange, "register_result.ftlh", data);

        } catch (Exception e) {
            e.printStackTrace();
            sendTextData(exchange, ResponseCodes.INTERNAL_ERROR, ContentType.TEXT_PLAIN, "Internal server error.");
        }
    }

    private void loginGetHandler(HttpExchange exchange) {
        try {
            File file = new File("data/login.html");
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            sendTextData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, "Login page not found.");
        }
    }

    private void loginPostHandler(HttpExchange exchange) {

        Charset utf8 = StandardCharsets.UTF_8;

        try {
            String formData = new String(exchange.getRequestBody().readAllBytes(), utf8);
            Map<String, String> params = parseFormData(formData);

            String email = params.get("email");
            String password = params.get("password");

            if (email == null || password == null) {
                sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Email and password are required");
                return;
            }

            Optional<Employee> employeeOpt = dataManager.findEmployeeByEmail(email);

            if (employeeOpt.isPresent() && employeeOpt.get().getPassword().equals(password)) {
                String sessionId = sessionManager.createSession(employeeOpt.get().getId());
                Cookie sessionCookie = Cookie.make("sessionId", sessionId);
                sessionCookie.setMaxAge(600);
                sessionCookie.setHttpOnly(true);
                sessionCookie.setPath("/");
                setCookie(exchange, sessionCookie);

                Map<String, Object> data = new HashMap<>();
                data.put("employee", employeeOpt.get());
                renderTemplate(exchange, "profile.ftlh", data);
            } else {
                sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Authorization failed. Invalid email or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendTextData(exchange, ResponseCodes.INTERNAL_ERROR, ContentType.TEXT_PLAIN, "Internal server error.");
        }
    }

    private void profileGetHandler(HttpExchange exchange) {
        Optional<Employee> employeeOpt = getAuthenticatedEmployee(exchange);

        if (employeeOpt.isEmpty()) {
            try {
                exchange.getResponseHeaders().set("Location", "/login");
                exchange.sendResponseHeaders(302, -1);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("employee", employeeOpt.get());
        renderTemplate(exchange, "profile.ftlh", data);
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> params = new HashMap<>();
        if (formData == null || formData.isEmpty()) {
            return params;
        }

        String[] parts = formData.split("&");
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                try {
                    String key = URLDecoder.decode(kv[0], "UTF-8").trim();
                    String value = URLDecoder.decode(kv[1], "UTF-8").trim();
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {

                }
            }
        }
        return params;
    }

    private Optional<Employee> getAuthenticatedEmployee(HttpExchange exchange) {
        String cookiesStr = getCookie(exchange);
        if (cookiesStr == null || cookiesStr.isEmpty()) {
            return Optional.empty();
        }

        Map<String, String> cookies = parseFormData(cookiesStr.replace(";", "&"));

        String sessionId = cookies.get("sessionId");

        if (sessionId == null || sessionId.isEmpty()) {
            return Optional.empty();
        }

        Integer employeeId = sessionManager.getEmployeeId(sessionId);
        if (employeeId == null) {
            return Optional.empty();
        }

        return dataManager.getEmployeeById(employeeId);
    }

    private void borrowBookHandler(HttpExchange exchange) throws IOException {
        Optional<Employee> employeeOpt = getAuthenticatedEmployee(exchange);

        if (employeeOpt.isEmpty()) {
            sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Authorization required.");
            return;
        }

        try {
            String path = exchange.getRequestURI().getPath();
            int bookId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));

            boolean success = dataManager.borrowBook(bookId, employeeOpt.get().getId());

            if (success) {
                exchange.getResponseHeaders().set("Location", "/books");
                exchange.sendResponseHeaders(302, -1);
            } else {
                sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Failed to issue book. Check book availability and limit(2 books)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendTextData(exchange, ResponseCodes.INTERNAL_ERROR, ContentType.TEXT_PLAIN, "Internal server error.");
        }

    }

    private void returnBookHandler(HttpExchange exchange) {
        Optional<Employee> employeeOpt = getAuthenticatedEmployee(exchange);

        if (employeeOpt.isEmpty()) {
            sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Authorization required.");
            return;
        }

        try {
            String path = exchange.getRequestURI().getPath();
            int bookId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));

            boolean success = dataManager.returnBook(bookId, employeeOpt.get().getId());

            if (success) {
                exchange.getResponseHeaders().set("Location", "/books");
                exchange.sendResponseHeaders(302, -1);
            } else {
                sendTextData(exchange, ResponseCodes.BAD_REQUEST, ContentType.TEXT_PLAIN, "Failed to return book. Book not found or already returned");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendTextData(exchange, ResponseCodes.INTERNAL_ERROR, ContentType.TEXT_PLAIN, "Internal server error.");
        }
    }
}
