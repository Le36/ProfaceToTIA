
public class TypeChecker {

    public String tyyppi(String type) {
        if (type.equals("FloatVariable")) {
            return "Real";
        }
        if (type.equals("RealVariable")) {
            return "LReal";
        }
        if (type.equals("BitVariable")) {
            return "Bool";
        }
        if (type.equals("IntegerVariable")) {
            return "DInt";
        }
        if (type.equals("TimerVariable")) {
            return "Skip";
        }
        return "null";
    }
    
}
