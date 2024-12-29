import java.util.Objects;

public class Faculty {
    private String name;
    private int duties;
    private final int id;
    private final Department department;

    private static int anInt = 0;

    private int maxDuties = 0;

    public Faculty(String name, String department,int maxDuties) {
        department = department.toLowerCase().trim();
        this.name = name.trim();
        switch(department){
            case "m.tech","m.tech cse":
                this.department = Department.M_TECH;
                break;
            case "chemical","chem":
                this.department = Department.CHEM;
                break;
            case "cse":
                this.department = Department.CSE;
                break;
            case "csd":
                this.department = Department.CSD;
                break;
            case "aids", "ai&ds":
                this.department = Department.AIDS;
                break;
            case "it":
                this.department = Department.IT;
                break;
            case "civil":
                this.department = Department.CIVIL;
                break;
            case "bio","biotech":
                this.department = Department.BIO;
                break;
            case "eee":
                this.department = Department.EEE;
                break;
            case "agri":
                this.department = Department.AGRI;
                break;
            case "english":
                this.department = Department.ENGLISH;
                break;
            case "eie":
                this.department = Department.EIE;
                break;
            case "ece":
                this.department = Department.ECE;
                break;
            case "mech":
                this.department = Department.MECH;
                break;
            case "rae":
                this.department = Department.RAE;
                break;
            case "bme":
                this.department = Department.BME;
                break;
            case "mca":
                this.department = Department.MCA;
                break;
            case "mba":
                this.department = Department.MBA;
                break;
            case "maths":
                this.department = Department.Maths;
                break;
            case "physics":
                this.department = Department.Physics;
                break;
            case "chemistry":
                this.department = Department.Chemistry;
                break;
            case "tamil":
                this.department = Department.Tamil;
                break;
            case "communication trainer":
                this.department = Department.Communication_Trainer;
                break;
            case "assistant professor":
                this.department = Department.Assistant_Professor;
                break;
            default:
                this.department = Department.DEFAULT;
                break;
        }
        id = anInt++;
        this.maxDuties = maxDuties;
    }

    public int getId() {
        return id;
    }

    public int getMaxDuties() {return maxDuties;}

    public void setMaxDuties(int maxDuties) { this.maxDuties = maxDuties;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDuties(){
        ++duties;
    }

    public int getDuties(){
        return duties;
    }

    @Override
    public String toString() {
        return "%s (%s)".formatted(name, department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return id == faculty.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}