import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Solver {
    private int day;

    public static Solver getSolver(int day) throws Exception {
        Solver solver = (Solver)Class.forName(String.format("Day%02d", day)).getConstructor().newInstance();
        solver.day = day;
        return solver;
    }

    protected List<String> getDataLines() {
        try {
            Path filePath = Paths.get(String.format("src/data/Day%02d.txt", day));
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            return new ArrayList<String>();
        }
    }

    public abstract Object solve();
}
