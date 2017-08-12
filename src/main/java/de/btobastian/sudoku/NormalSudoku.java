package de.btobastian.sudoku;

import java.util.Stack;

/**
 * This class represents a normal sudoku.
 * Use {@link Sudoku#createEmptySudoku(SudokuType, SudokuSize)} to create an instance of this class.
 */
public class NormalSudoku extends Sudoku {

    // [row][column]
    private int[][] matrix;

    protected NormalSudoku(SudokuSize size) {
        super(SudokuType.NORMAL, size);
        this.matrix = new int[size.getNormalSize()][size.getNormalSize()];
    }

    @Override
    public NormalSudoku clone() {
        NormalSudoku sudoku = new NormalSudoku(getSize());
        int [][] matrixCopy = new int[matrix.length][];
        for (int j = 0; j < matrix.length; j++) {
            matrixCopy[j] = matrix[j].clone();
        }
        sudoku.matrix = matrixCopy;
        return sudoku;
    }

    @Override
    public boolean isValidMove(int field, int number) {
        return isValidMove(field, number, matrix);
    }

    /*
     * For a normal sudoku the field numbers are mapped in the following way:
     * +-----+-----+
     * | 0  1| 2  3|
     * | 4  5| 6  7|
     * +-----+-----+
     * | 8  9|10 11|
     * |12 13|14 15|
     * +-----+-----+
     */
    @Override
    public int getNumberAtField(int field) {
        int row = field % getSize().getNormalSize();
        int column = field / getSize().getNormalSize();
        if (column > getSize().getNormalSize() || field < 0) {
            throw new IllegalArgumentException("Invalid field number!");
        }
        return matrix[row][column];
    }

    @Override
    public NormalSudoku solve() {
        int[][] matrix = this.matrix;
        Stack<int[][]> stack = new Stack<int[][]>();
        stack.push(matrix);
        outer: while (!stack.isEmpty()) {
            matrix = stack.pop();
            for (int field = 0; field < getSize().getNormalSize()*getSize().getNormalSize(); field++) {
                int column = field % getSize().getNormalSize();
                int row = field / getSize().getNormalSize();
                if (matrix[row][column] == 0) {
                    for (int i = 0; i < getSize().getNormalSize(); i++) {
                        int [][] matrixCopy = new int[matrix.length][];
                        for (int j = 0; j < matrix.length; j++) {
                            matrixCopy[j] = matrix[j].clone();
                        }
                        if (isValidMove(field, i + 1, matrix)) {
                            matrixCopy[row][column] = i + 1;
                            stack.push(matrixCopy);
                        }
                    }
                    continue outer;
                }
            }
            // All fields are filled
            NormalSudoku sudoku = new NormalSudoku(getSize());
            sudoku.matrix = matrix;
            return sudoku;
        }
        return null;
    }

    /**
     * Gets the numbers in the given block.
     * A block number is mapped the same way, as field numbers.
     *
     * @param block The number of the block.
     * @return The values inside the block.
     */
    public int[][] getBlock(int block) {
        return getBlock(block, matrix);
    }

    /**
     * Checks if a move for the given matrix is valid to the rules.
     * This method does not check, if the move is correct to solve the sudoku!
     *
     * @param field The field number. Every field in the sudoku is mapped to an unique number.
     * @param number The number to test.
     * @param matrix The matrix to check.
     * @return Whether the move is valid or not.
     */
    private boolean isValidMove(int field, int number, int[][] matrix) {
        int column = field % getSize().getNormalSize();
        int row = field / getSize().getNormalSize();
        if (row > getSize().getNormalSize() || field < 0) {
            throw new IllegalArgumentException("Invalid field number!");
        }
        if (number > getSize().getNormalSize() || number < 1) {
            throw new IllegalArgumentException("The given number is invalid!");
        }

        // Check if there is already this number in the block
        int blockColumn = column / (int) Math.sqrt(getSize().getNormalSize());
        int blockRow = row / (int) Math.sqrt(getSize().getNormalSize());
        int[][] blockFields = getBlock(blockRow * (int) Math.sqrt(getSize().getNormalSize()) + blockColumn, matrix);
        for (int i = 0; i < blockFields.length; i++) {
            for (int j = 0; j < blockFields[i].length; j++) {
                if (blockFields[i][j] == number) {
                    return false;
                }
            }
        }

        // Check if there is already this number in the row
        for (int i : matrix[row]) {
            if (i == number) {
                return false;
            }
        }

        // Check if there is already this number in the column
        for (int i = 0; i < getSize().getNormalSize(); i++) {
            if (matrix[i][column] == number) {
                return false;
            }
        }

        // It passed all checks, so it's a valid move
        return true;
    }

    /**
     * Gets the numbers in the given block.
     * A block number is mapped the same way, as field numbers.
     *
     * @param block The number of the block.
     * @param matrix The matrix to check.
     * @return The values inside the block.
     */
    private int[][] getBlock(int block, int[][] matrix) {
        int column = block % (int) Math.sqrt(getSize().getNormalSize());
        int row = block / (int) Math.sqrt(getSize().getNormalSize());
        if (row > Math.sqrt(getSize().getNormalSize()) || block < 0) {
            throw new IllegalArgumentException("Invalid block number!");
        }
        int[][] fields = new int[(int) Math.sqrt(getSize().getNormalSize())][(int) Math.sqrt(getSize().getNormalSize())];
        for (int i = 0; i < Math.sqrt(getSize().getNormalSize()); i++) {
            for (int j = 0; j < Math.sqrt(getSize().getNormalSize()); j++) {
                fields[i][j] = matrix[row * (int) Math.sqrt(getSize().getNormalSize()) + i][column * (int) Math.sqrt(getSize().getNormalSize()) + j];
            }
        }
        return fields;
    }

    /**
     * Generates an output which may look like this.
     * Requires a monospace font to look good.
     *
     * <pre>
     * +---+---+---+
     * |921|239|255|
     * |621|794|869|
     * |199|773|586|
     * +---+---+---+
     * |487|713|959|
     * |617|256|528|
     * |342|991|624|
     * +---+---+---+
     * |375|791|671|
     * |856|344|698|
     * |445|595|923|
     * +---+---+---+
     * </pre>
     */
    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (int row = 0; row < getSize().getNormalSize(); row++) {
            // Generate the horizontal separation lines between the blocks
            if (row % Math.sqrt(getSize().getNormalSize()) == 0) {
                for (int column = 0; column < getSize().getNormalSize(); column++) {
                    if (column % Math.sqrt(getSize().getNormalSize()) == 0) {
                        strBuilder.append("+");
                    }
                    strBuilder.append("-");
                }
                strBuilder.append("+");
                strBuilder.append("\n");
            }

            for (int column = 0; column < getSize().getNormalSize(); column++) {
                // Generate the vertical separation lines between the blocks
                if (column % Math.sqrt(getSize().getNormalSize()) == 0) {
                    strBuilder.append("|");
                }
                // Append the content of the field.
                strBuilder.append(matrix[row][column] == 0 ? " " : matrix[row][column]);
            }
            // Append the last vertical separation line
            strBuilder.append("|");
            strBuilder.append("\n");
        }

        // Generate the last horizontal separation line
        for (int column = 0; column < getSize().getNormalSize(); column++) {
            if (column % Math.sqrt(getSize().getNormalSize()) == 0) {
                strBuilder.append("+");
            }
            strBuilder.append("-");
        }
        strBuilder.append("+");
        return strBuilder.toString();
    }

}
