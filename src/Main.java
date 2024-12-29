import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final Map<LocalDate, List<Faculty>> facultyList =
            new TreeMap<>(Comparator.naturalOrder());
    private static final HashMap<Faculty, Integer> numberOfDuties = new HashMap<>();
    private static final HashSet<Faculty> foreNoon = new HashSet<>();

    public static void main(String[] args) {

        int numberOfDutiesForeNoon = 0;
        int numberOfDutiesAfterNoon = 0;

        List<Faculty> faculties = new ArrayList<>();
        String line;
        String user = System.getProperty("user.home");
        String path = user+"\\Desktop\\FacultyAllocation\\data.csv";
        int l = 0;
        List<String> strings = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            while ((line = bf.readLine()) != null) {
                if(line.isEmpty()) continue;
                if(l == 0){
                    l++;
                    String[] d = line.split(",");
                    numberOfDutiesForeNoon = Integer.parseInt(d[0].trim());
                    numberOfDutiesAfterNoon = Integer.parseInt(d[1].trim());
                    continue;
                }
                strings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(strings);

        for(String s : strings){
            String[] data = s.split(",");
            //System.out.println(data.length + "===" + Arrays.toString(data));
            Faculty f ;
            if(data.length == 3) f = new Faculty(data[0].trim(), data[1].trim(),
                    Integer.parseInt(data[2].trim()));
            else f = new Faculty(data[0].trim(), data[1].trim(),
                    Integer.MAX_VALUE);
            faculties.add(f);
        }

        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> foreNoonDuties = new ArrayList<>();
        List<Integer> afterNoonDuties = new ArrayList<>();

        String path1 = user+"\\Desktop\\FacultyAllocation\\date.csv";
        try(BufferedReader bf = new BufferedReader(new FileReader(path1))){
            while((line = bf.readLine()) != null){
                String data[] = line.split(",");
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate ld = LocalDate.parse(data[0].trim(),sdf);
                dateList.add(ld);
                foreNoonDuties.add(Integer.parseInt(data[1].trim()));
                afterNoonDuties.add(Integer.parseInt(data[2].trim()));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        for(LocalDate d : dateList) {
            facultyList.put(d,faculties);
        }

        for (Faculty faculty : faculties) {
            numberOfDuties.put(faculty, 0);
        }
        AtomicInteger index = new AtomicInteger(0);
        try {
            PdfWriter writer = new PdfWriter("faculty_duty_allocation.pdf");
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            String imagePath = user + "\\Desktop\\FacultyAllocation\\esecColorLogo.jpg";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image img = new Image(imageData);
            img.scaleToFit(60,60);
            float[] width = new float[]{1,2,1};
            Table t = new Table(width);

            Cell imageCell = new Cell().add(img);
            imageCell.setBorder(null);
            Cell textCell = new Cell().add(new Paragraph("ERODE SENGUNTHAR ENGINEERING COLLEGE")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(14).setBold())
                    .add(new Paragraph("(An Autonomous Institution, Affiliated to Anna University)")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12).setBold())
                    .add(new Paragraph("Approved by AICTE, New Delhi, Permanently Affiliated to Anna University- Chennai,\n" +
                            "Accredited by National Board of Accreditation (NBA), New Delhi &\n" +
                            "National Assessment and Accreditation Council (NAAC), Bangalore with ‘A’ Grade\n")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(8))
                    .add(new Paragraph("PERUNDURAI, ERODE – 638 057.")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12).setBold());
            textCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            textCell.setBorder(null);

            String imagePath1 = user + "\\Desktop\\FacultyAllocation\\naac.jpg";
            ImageData imageData1 = ImageDataFactory.create(imagePath1);
            Image img1 = new Image(imageData1);
            img1.scaleToFit(70,50);

            String imagePath2 = user + "\\Desktop\\FacultyAllocation\\iic.jpg";
            ImageData imageData2 = ImageDataFactory.create(imagePath2);
            Image img2 = new Image(imageData2);
            img2.scaleToFit(70,50);

            Cell imageCell1 = new Cell().add(img1).add(img2);
            imageCell1.setBorder(null);

            t.addCell(imageCell);
            t.addCell(textCell);
            t.addCell(imageCell1);
            t.setWidth(UnitValue.createPercentValue(90));
            t.setHorizontalAlignment(HorizontalAlignment.CENTER);


            Table prin = new Table(new float[]{1,1});
            Paragraph principal = new Paragraph("Dr.V.VENKATACHALAM").setBold().setFontSize(10);
            Paragraph desig = new Paragraph("principal").setFontSize(10);

            LocalDate d = LocalDate.now();
            DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = d.format(dt);
            Paragraph dat = new Paragraph("Date : "+formattedDate);

            Cell a1 = new Cell().add(principal).add(desig).setBorder(null);
            Cell b1 = new Cell().add(dat).setTextAlignment(TextAlignment.RIGHT).setBorder(null);
            b1.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            prin.setWidth(UnitValue.createPercentValue(95));
            prin.addCell(a1);
            prin.addCell(b1);

            Paragraph exam = new Paragraph("AUTONOMOUS -THEORY EXAMINATIONS – NOVEMBER / DECEMBER 2024")
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph paragraph = new Paragraph("Faculty Duty Allocation").setBold().setFontSize(15);
            paragraph.setTextAlignment(TextAlignment.CENTER);

            document.add(t);
            document.add(prin);
            document.add(exam);
            document.add(paragraph);

            AtomicInteger serialNumber = new AtomicInteger(1);
            float[] columnWidths = {1f,4f,1,1};
            Table parentTable = new Table(columnWidths);
            parentTable.setBorder(new SolidBorder(1.5f));
            Cell q = new Cell().add(new Paragraph("S.No").setBold()).setBorder(new SolidBorder(1.5f));
            q.setTextAlignment(TextAlignment.CENTER);
            Cell w = new Cell(2,1).add(new Paragraph("Date").setBold()).setBorder(new SolidBorder(1.5f));
            w.setWidth(70);
            w.setTextAlignment(TextAlignment.CENTER);
            Cell x = new Cell().add(new Paragraph("Session").setBold()).setBorder(new SolidBorder(1.5f));
            x.setTextAlignment(TextAlignment.CENTER);
            Cell y = new Cell().add(new Paragraph("Name of the Hall Superintendent").setBold()).setBorder(new SolidBorder(1.5f));
            y.setTextAlignment(TextAlignment.CENTER);

            parentTable.addHeaderCell(q);
            parentTable.addHeaderCell(w);
            parentTable.addHeaderCell(x);
            parentTable.addHeaderCell(y);

            parentTable.setWidth(UnitValue.createPercentValue(100));

            facultyList.forEach((date, faculty) -> {
                DayOfWeek day = date.getDayOfWeek();
                int sn = serialNumber.getAndIncrement();
                int k = index.getAndIncrement();
                List<Faculty> foreNoonList = allocateDutiesForeNoon(foreNoonDuties.get(k), faculty);
                foreNoonList.sort(Comparator.comparing(Faculty::getDepartment));

                String fnl = foreNoonList.stream()
                        .map(f -> f+" ")
                        .collect(Collectors.joining(","));
                List<Faculty> afterNoonList = allocateDutiesAfterNoon(afterNoonDuties.get(k), faculty);
                afterNoonList.sort(Comparator.comparing(Faculty::getDepartment));

                String anl = afterNoonList.stream()
                        .map(f -> f+" ")
                        .collect(Collectors.joining(","));

                foreNoon.clear();

                parentTable.addCell(new Cell(2,1)
                        .add(new Paragraph(sn+"")
                                .setTextAlignment(TextAlignment.CENTER))
                                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                .setBorder(new SolidBorder(1.5f)));

                parentTable.addCell(new Cell(2,1)
                        .add(new Paragraph(date+"\n("+day+")")
                                .setTextAlignment(TextAlignment.CENTER))
                                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                .setBorder(new SolidBorder(1.5f)));

                parentTable.addCell(new Cell()
                        .add(new Paragraph("F.N")
                                .setTextAlignment(TextAlignment.CENTER))
                                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                .setBorder(new SolidBorder(1.5f)));

                parentTable.addCell(new Cell()
                        .add(new Paragraph(fnl)
                                .setTextAlignment(TextAlignment.JUSTIFIED))
                                .setBorder(new SolidBorder(1.5f)));

                parentTable.addCell(new Cell()
                        .add(new Paragraph("A.N")
                                .setTextAlignment(TextAlignment.CENTER))
                                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                .setBorder(new SolidBorder(1.5f)));

                parentTable.addCell(new Cell()
                        .add(new Paragraph(anl)
                                .setTextAlignment(TextAlignment.JUSTIFIED))
                                .setBorder(new SolidBorder(1.5f)));
            });
            document.add(parentTable);
            Paragraph paragraph1 = new Paragraph("\nPRINCIPAL").setBold();
            Paragraph hod = new Paragraph("\nTo\n" +
                    "All HOD’s (with request to Circulation among their staff members)");
            Paragraph copy = new Paragraph("\nCopy to\t\tAutonomous Exam Section\t\tCOE Office\t\tCollege Office File");
            document.add(paragraph1);
            document.add(hod);
            document.add(copy);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PdfWriter writer = new PdfWriter("faculty_duties_summary.pdf");
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            String imagePath = user + "\\Desktop\\FacultyAllocation\\esecColorLogo.jpg";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image img = new Image(imageData);
            img.scaleToFit(60,60);
            float[] width = new float[]{1,2,1};
            Table ta = new Table(width);

            Cell imageCell = new Cell().add(img);
            imageCell.setBorder(null);
            Cell textCell = new Cell().add(new Paragraph("ERODE SENGUNTHAR ENGINEERING COLLEGE")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(14).setBold())
                    .add(new Paragraph("(An Autonomous Institution, Affiliated to Anna University)")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12).setBold())
                    .add(new Paragraph("Approved by AICTE, New Delhi, Permanently Affiliated to Anna University- Chennai,\n" +
                            "Accredited by National Board of Accreditation (NBA), New Delhi &\n" +
                            "National Assessment and Accreditation Council (NAAC), Bangalore with ‘A’ Grade\n")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(8))
                    .add(new Paragraph("PERUNDURAI, ERODE – 638 057.")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12).setBold());
            textCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            textCell.setBorder(null);

            String imagePath1 = user + "\\Desktop\\FacultyAllocation\\naac.jpg";
            ImageData imageData1 = ImageDataFactory.create(imagePath1);
            Image img1 = new Image(imageData1);
            img1.scaleToFit(70,50);

            String imagePath2 = user + "\\Desktop\\FacultyAllocation\\iic.jpg";
            ImageData imageData2 = ImageDataFactory.create(imagePath2);
            Image img2 = new Image(imageData2);
            img2.scaleToFit(70,50);

            Cell imageCell1 = new Cell().add(img1).add(img2);
            imageCell1.setBorder(null);

            ta.addCell(imageCell);
            ta.addCell(textCell);
            ta.addCell(imageCell1);
            ta.setWidth(UnitValue.createPercentValue(90));
            ta.setHorizontalAlignment(HorizontalAlignment.CENTER);


            Table prin = new Table(new float[]{1,1});
            Paragraph principal = new Paragraph("Dr.V.VENKATACHALAM").setBold().setFontSize(10);
            Paragraph desig = new Paragraph("principal").setFontSize(10);

            LocalDate de = LocalDate.now();
            DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = de.format(dt);
            Paragraph dat = new Paragraph("Date : "+formattedDate);

            Cell a1 = new Cell().add(principal).add(desig).setBorder(null);
            Cell b1 = new Cell().add(dat).setTextAlignment(TextAlignment.RIGHT).setBorder(null);
            b1.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            prin.setWidth(UnitValue.createPercentValue(95));
            prin.addCell(a1);
            prin.addCell(b1);

            Paragraph exam = new Paragraph("AUTONOMOUS -THEORY EXAMINATIONS – NOVEMBER / DECEMBER 2024")
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph paragraph = new Paragraph("Faculty Duties Summary\n\n").setBold().setFontSize(15);
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(ta);
            document.add(prin);
            document.add(exam);
            document.add(paragraph);

            float[] columnWidths = {1f,1f,1f,1f};
            Table table = new Table(columnWidths);

            Cell a = new Cell().add(new Paragraph("S.No").setBold());
            a.setTextAlignment(TextAlignment.CENTER);

            Cell b = new Cell().add(new Paragraph("Name").setBold());
            b.setTextAlignment(TextAlignment.CENTER);

            Cell c = new Cell().add(new Paragraph("Department").setBold());
            c.setTextAlignment(TextAlignment.CENTER);

            Cell d = new Cell().add(new Paragraph("Duties").setBold());
            d.setTextAlignment(TextAlignment.CENTER);

            table.addHeaderCell(a);
            table.addHeaderCell(b);
            table.addHeaderCell(c);
            table.addHeaderCell(d);

            var set = numberOfDuties.entrySet().stream().
                    sorted(Comparator.comparing(t -> t.getKey().getDepartment()))
                    .collect(Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing((Map.Entry<Faculty, Integer> e) -> e.getKey().getDepartment())
                                    .thenComparing(e -> e.getKey().getName())
                    )));
            int o = 1;
            for (Map.Entry<Faculty, Integer> entry : set) {
                Faculty faculty = entry.getKey();
                Integer dutiesCount = entry.getValue();
                table.addCell((o++) + ".");
                table.addCell(faculty.getName());
                table.addCell(faculty.getDepartment()+"");
                table.addCell(dutiesCount.toString());

            }
            table.setWidth(UnitValue.createPercentValue(80));
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Faculty> allocateDutiesForeNoon(int duties, List<Faculty> faculties) {
        List<Faculty> allocatedFaculties = new ArrayList<>();
        Set<Department> allocatedDepartment = new HashSet<>();

        for (int i = 0; i < duties; i++) {
            List<Faculty> newFaculties = new ArrayList<>();
            for(Faculty f : faculties){
                if(numberOfDuties.get(f) < f.getMaxDuties()
                        && !allocatedFaculties.contains(f)) newFaculties.add(f);
            }
            Collections.shuffle(newFaculties);
            newFaculties.sort(Comparator.comparingInt(Faculty::getDuties).thenComparing(Faculty::getId));
            Faculty selected = newFaculties.getFirst();
            int duty = selected.getDuties();
            List<Faculty> o = new ArrayList<>();
            for(Faculty f : newFaculties){
                if(duty == f.getDuties()) o.add(f);
            }
            int j = 0;
            while (j < o.size()) {
                Faculty test = newFaculties.get(j);
                if (!allocatedDepartment.contains(test.getDepartment())) {
                    selected = newFaculties.get(j);
                    break;
                }
                j++;
            }
            allocatedDepartment.add(selected.getDepartment());
            selected.setDuties();
            foreNoon.add(selected);
            numberOfDuties.put(selected, selected.getDuties());
            allocatedFaculties.add(selected);
        }
        return allocatedFaculties;
    }

    public static List<Faculty> allocateDutiesAfterNoon(int duties, List<Faculty> faculties) {
        List<Faculty> allocatedFaculties = new ArrayList<>();
        Set<Department> allocatedDepartment = new HashSet<>();

        for (int i = 0; i < duties; i++) {
            List<Faculty> newFaculties = new ArrayList<>();
            for(Faculty f : faculties){
                if(numberOfDuties.get(f) < f.getMaxDuties()
                        && !allocatedFaculties.contains(f)) newFaculties.add(f);
            }
            Collections.shuffle(newFaculties);
            newFaculties.sort(Comparator.comparingInt(Faculty::getDuties).thenComparing(Faculty::getId));
            Faculty faculty = newFaculties.getFirst();
            int duty = faculty.getDuties();
            List<Faculty> o = new ArrayList<>();
            for(Faculty f : newFaculties){
                if(duty == f.getDuties() && !foreNoon.contains(f)) o.add(f);
            }
            int j = 1;
            while (allocatedDepartment.contains(faculty.getDepartment())
                    && j<o.size()) {
                faculty = o.get(j++);
            }
            allocatedFaculties.add(faculty);
            allocatedDepartment.add(faculty.getDepartment());
            faculty.setDuties();
            numberOfDuties.put(faculty, faculty.getDuties());
        }
        return allocatedFaculties;
    }
}