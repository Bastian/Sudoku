package de.btobastian.sudoku;

/**
 * This class represents a sudoku.
 */
public abstract class Sudoku {

    private final SudokuType type;
    private final SudokuSize size;

    protected Sudoku(SudokuType type, SudokuSize size) {
        this.type = type;
        this.size = size;
    }

    /**
     * Creates an empty normal sudoku with normal size.
     *
     * @return An empty sudoku.
     */
    public static Sudoku createEmptySudoku() {
        return createEmptySudoku(SudokuType.NORMAL, SudokuSize.NORMAL);
    }

    /**
     * Creates an empty sudoku with the given type and size.
     *
     * @param type The type of the sudoku.
     * @param size The size of the sudoku.
     * @return An empty sudoku.
     */
    public static Sudoku createEmptySudoku(SudokuType type, SudokuSize size) {
        switch (type) {
            case NORMAL:
                return new NormalSudoku(size);
            case SAMURAI:
                // TODO
                return null;
            default:
                throw new IllegalArgumentException("Unknown sudoku type!");
        }
    }

    /**
     * Creates an exact copy of the sudoku.
     *
     * @return An exact copy of the sudoku.
     */
    public abstract Sudoku clone();

    /**
     * Checks if a move is valid to the rules.
     * This method does not check, if the move is correct to solve the sudoku!
     *
     * @param field The field number. Every field in the sudoku is mapped to an unique number.
     * @param number The number to test.
     * @return Whether the move is valid or not.
     */
    public abstract boolean isValidMove(int field, int number);

    /**
     * Gets the number at the given field.
     *
     * @param field The field number. Every field in the sudoku is mapped to an unique number.
     * @return The number at the given field.
     *         Returns 0 if the field is empty.
     */
    public abstract int getNumberAtField(int field);

    /**
     * Gets the type of the sudoku.
     *
     * @return The type of the sudoku.
     */
    public SudokuType getType() {
        return type;
    }

    /**
     * Gets the size of the sudoku.
     *
     * @return The size of the sudoku.
     */
    public SudokuSize getSize() {
        return size;
    }

    /**
     * Tries to solve the given sudoku.
     *
     * @return The solved sudoku or <code>null</code> if it is not solveable.
     */
    public abstract Sudoku solve();

}
