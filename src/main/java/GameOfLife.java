import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The GameOfLife class represents the game of life simulation.
 * It allows loading a seed file, simulating the game for a given number of steps,
 * and saving the grid state after each step.
 * @author Nico Wang
 */
public class GameOfLife {
    private int rows;
    private int columns;
    private int[][] grid;

    /**
     * Constructs a GameOfLife object with the specified number of rows and columns.
     *
     * @param rows    the number of rows in the grid
     * @param columns the number of columns in the grid
     */
    public GameOfLife(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new int[rows][columns];
    }

    /**
     * Loads the seed file and initializes the grid based on the contents of the file.
     *
     * @param fileName the name of the seed file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadSeed(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        String[] dimensions = line.split(", ");
        this.rows = Integer.parseInt(dimensions[0]);
        this.columns = Integer.parseInt(dimensions[1]);
        this.grid = new int[rows][columns];

        int row = 0;
        while ((line = reader.readLine()) != null) {
            String[] cells = line.split(", ");
            for (int col = 0; col < columns; col++) {
                this.grid[row][col] = Integer.parseInt(cells[col]);
            }
            row++;
        }

        reader.close();
    }

    /**
     * Saves the current grid state to a file with the specified name and tick number.
     *
     * @param fileName the name of the output file
     * @param tick     the current tick number
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void saveGrid(String fileName, int tick) throws IOException {
        FileWriter writer = new FileWriter(fileName + "_" + tick + ".txt");
        writer.write(rows + ", " + columns + "\n");
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                writer.write(grid[row][col] + ", ");
            }
            writer.write("\n");
        }
        writer.close();
    }

    /**
     * Simulates the game for the specified number of steps.
     *
     * @param steps the number of steps to simulate
     */
    public void simulate(int steps, String outFile) {
        for (int tick = 1; tick <= steps; tick++) {
            int[][] nextGeneration = new int[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int liveNeighbors = countLiveNeighbors(row, col);
                    if (grid[row][col] == 1) {
                        if (liveNeighbors < 2 || liveNeighbors > 3) {
                            nextGeneration[row][col] = 0;
                        } else {
                            nextGeneration[row][col] = 1;
                        }
                    } else {
                        if (liveNeighbors == 3) {
                            nextGeneration[row][col] = 1;
                        } else {
                            nextGeneration[row][col] = 0;
                        }
                    }
                }
            }
            grid = nextGeneration;
            try {
                saveGrid(outFile, tick);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Counts the number of live neighbors for the specified cell.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the number of live neighbors
     */
    private int countLiveNeighbors(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int neighborRow = (row + i + rows) % rows;
                int neighborCol = (col + j + columns) % columns;
                if (!(i == 0 && j == 0) && grid[neighborRow][neighborCol] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * The entry point of the program.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java GameOfLife <seed_file> <output_name> <steps>");
            return;
        }

        String seedFile = args[0];
        String outFile = args[1];
        int steps = Integer.parseInt(args[2]);

        if (steps < 1) {
            System.out.println("The number of steps must be greater than 0.");
            return;
        }

        GameOfLife game = new GameOfLife(0, 0);
        try {
            game.loadSeed(seedFile);
            game.simulate(steps, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}