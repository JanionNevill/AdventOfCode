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
        this(0, 0);
    }
    
    public Grid(int width, int length) {
        cells = new ArrayList<>();
        for (int y = 0; y < length; y++) {
            List<CellT> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(null);
            }
            cells.add(row);
        }
    }
    
    public void addCell(Coordinate coordinate, CellT cell) {
        addCell(coordinate.getX(), coordinate.getY(), cell);
    }
    
    public void addCell(int x, int y, CellT cell) {
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
        
        row.add(x, cell);
    }
    
    public void removeCell(int x, int y) {
        if (y >= cells.size() || x >= cells.get(0).size()) {
            throw new IllegalArgumentException(String.format("Coordinates (%d, %d) are outside the grid", x, y));
        }
        
        List<CellT> row = cells.get(y);
        
        if (x < row.size()) {
            row.set(x, null);
        }
    }
    
    public void moveCell(int x, int y, int newX, int newY) {
        if (y >= cells.size() || x >= cells.get(0).size()) {
            throw new IllegalArgumentException(String.format("Coordinates (%d, %d) are outside the grid", x, y));
        } else if (newY >= cells.size() || newX >= cells.get(0).size()) {
            throw new IllegalArgumentException(String.format("Coordinates (%d, %d) are outside the grid", newX, newY));
        }
        
        List<CellT> row = cells.get(y);
        CellT value = row.set(x, null);
        
        List<CellT> newRow = cells.get(newY);
        newRow.set(newX, value);
    }
    
    public CellT getCell(Coordinate coordinate) {
        return getCell(coordinate.getX(), coordinate.getY());
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
            String rowStr = row.stream().map(cell -> cell == null ? "." : cell.toString()).collect(Collectors.joining());
            str += rowStr;
            str += "\n";
        }
        return str;
    }

}
