package adventofcode.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Grid<CellT> {
    
    private List<List<CellT>> cells;
    
    public Grid(Grid<CellT> toCopy, Function<CellT, CellT> cellCopier) {
        this();
        for (int x = 0; x < toCopy.getWidth(); x++) {
            for (int y = 0; y < toCopy.getWidth(); y++) {
                addCell(x, y, cellCopier.apply(toCopy.getCell(x, y)));
            }
        }
    }
    
    public Grid() {
        cells = new ArrayList<>();
    }
    
    public void addCell(int x, int y, CellT location) {
        while (y >= cells.size()) {
            cells.add(new ArrayList<>());
        }
        
        List<CellT> row = cells.get(y);
        
        while (x >= row.size()) {
            row.add(null);
        }
        
        if (x < row.size()) {
            row.remove(x);
        }
        
        row.add(x, location);
    }
    
    public CellT getCell(int x, int y) {
        return cells.get(y).get(x);
    }
    
    public int getWidth() {
        if (cells.isEmpty()) {
            return 0;
        }
        return cells.get(0).size();
    }
    
    public int getLength() {
        return cells.size();
    }
    
    @Override
    public String toString() {
        String str = "";
        for (List<CellT> row : cells) {
            String rowStr = row.stream().map(CellT::toString).collect(Collectors.joining());
            str += rowStr;
            str += "\n";
        }
        return str;
    }

}
