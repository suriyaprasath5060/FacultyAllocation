public enum Department {
    DEFAULT,
    M_TECH {
        @Override
        public String toString() {
            return "M.TECH CSE";
        }
    },
    CSE,
    CSD,
    CHEM,
    AIDS{
        @Override
        public String toString() {
            return "AI&DS";
        }
    },
    BIO{
        @Override
        public String toString() {
            return "BIOTECH";
        }
    },
    IT,
    CIVIL,
    AGRI,
    EEE,
    ENGLISH,
    ECE,
    EIE,
    MECH,

    RAE,
    BME,
    MCA,
    MBA,
    Maths,
    Physics,
    Chemistry,
    Tamil,
    Communication_Trainer
    {
        @Override
        public String toString(){
            return "COMMUNICATION TRAINER";
        }
    },
    Assistant_Professor{
        @Override
        public String toString(){
            return "ASSISTANT PROFESSOR";
        }
    };
}