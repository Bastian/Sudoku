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

    /**
     * Creates a normal sudoku from the given matrix.
     * The matrix first index of the matrix represents the row, the second one the column.
     * The matrix can have the following sizes: 4x4, 9x9, 16x16, 25x25, 36x36 or 49x49.
     * Zeros in the matrix represents empty slots.
     *
     * @param matrix The matrix.
     * @return A normal sudoku for the given matrix.
     */
    public static NormalSudoku createSudokuFromMatrix(int[][] matrix) {
        SudokuSize size = null;
        switch (matrix.length) {
            case 4:
                size = SudokuSize.TINY;
                break;
            case 9:
                size = SudokuSize.NORMAL;
                break;
            case 16:
                size = SudokuSize.LARGE;
                break;
            case 25:
                size = SudokuSize.HUGE;
                break;
            case 36:
                size = SudokuSize.EXTREME;
                break;
            case 49:
                size = SudokuSize.ENORMOUS;
                break;
            default:
                throw new IllegalArgumentException("Invalid matrix size!");
        }

        for (int[] row : matrix) {
            if (row.length != size.getNormalSize()) {
                throw new IllegalArgumentException("Invalid matrix size!");
            }
        }

        NormalSudoku sudoku = new NormalSudoku(size);
        sudoku.matrix = matrix;
        return sudoku.clone();
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
        Stack<int[][]> stack = solve(matrix);
        if (!stack.isEmpty()) {
            NormalSudoku sudoku = new NormalSudoku(getSize());
            sudoku.matrix = stack.pop();
            return sudoku;
        } else {
            return null;
        }
    }

    @Override
    public boolean hasUniqueSolution() {
        Stack<int[][]> stack = solve(matrix);
        return stack.size() == 1;
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
     * Solves the given matrix.
     *
     * @param matrix The matrix to solve.
     * @return A stack where the top element is the solved matrix.
     *         If the stack is empty, there's no solution.
     *         If the stack has more than 1 element, it has more than one possible solution.
     */
    private Stack<int[][]> solve(int[][] matrix) {
        Stack<int[][]> solvedStack = new Stack<int[][]>();
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
            solvedStack.push(matrix);
        }
        return solvedStack;
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
     * Encodes numbers larger than 9 to letters (10 -> A, 11 -> B, ...)
     * @param number The number to encode.
     */
    private char encodeNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Negative numbers are not allowed!");
        }

        if (number >= 0 && number <= 9) {
            return (char) ('0' + number);
        }

        return (char) ('A' + (number-10));
    }

    /**
     * Generates an output which may look like this.
     * Requires a monospace font to look good.
     *
     * <pre>
     * +-------+-------+-------+
     * | 9 8 7 | 6 5 4 | 3 2 1 |
     * | 6 5 4 | 3 2 1 | 9 8 7 |
     * | 3 2 1 | 9 8 7 | 6 5 4 |
     * +-------+-------+-------+
     * | 8 9 6 | 7 4 5 | 2 1 3 |
     * | 7 4 5 | 2 1 3 | 8 9 6 |
     * | 2 1 3 | 8 9 6 | 7 4 5 |
     * +-------+-------+-------+
     * | 5 7 9 | 4 6 8 | 1 3 2 |
     * | 4 6 8 | 1 3 2 | 5 7 9 |
     * | 1 3 2 | 5 7 9 | 4 6 8 |
     * +-------+-------+-------+
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
                        strBuilder.append(column == 0 ? "+" : "-+");
                    }
                    strBuilder.append("--");
                }
                strBuilder.append("-+");
                strBuilder.append("\n");
            }

            for (int column = 0; column < getSize().getNormalSize(); column++) {
                // Generate the vertical separation lines between the blocks
                if (column % Math.sqrt(getSize().getNormalSize()) == 0) {
                    strBuilder.append(column == 0 ? "|" :" |");
                }
                // Append the content of the field.
                strBuilder.append(" ");
                strBuilder.append(matrix[row][column] == 0 ? " " : encodeNumber(matrix[row][column]));
            }
            // Append the last vertical separation line
            strBuilder.append(" |");
            strBuilder.append("\n");
        }

        // Generate the last horizontal separation line
        for (int column = 0; column < getSize().getNormalSize(); column++) {
            if (column % Math.sqrt(getSize().getNormalSize()) == 0) {
                strBuilder.append(column == 0 ? "+" : "-+");
            }
            strBuilder.append("--");
        }
        strBuilder.append("-+");
        return strBuilder.toString();
    }

}
