package de.btobastian.sudoku;

/**
 * Represents the size of a sudoku.
 */
public enum SudokuSize {

    /**
     * A tiny sudoku with a 4x4 grid size.
     */
    TINY(4),

    /**
     * A normal sudoku with a 9x9 grid size.
     */
    NORMAL(9),

    /**
     * A large sudoku with a 16x16 grid size.
     */
    LARGE(16),

    /**
     * A huge sudoku with a 25x25 grid size.
     */
    HUGE(25),

    /**
     * An extreme sudoku with a 36x36 grid size.
     */
    EXTREME(36),

    /**
     * An enormous sudoku with a 49x49 grid size.
     */
    ENORMOUS(49);

    /**
     * The matrix size of a normal sudoku.
     */
    private int normalMatrixSize;

    /**
     * Class constructor.
     *
     * @param normalMatrixSize The matrix size of a normal sudoku.
     */
    SudokuSize(int normalMatrixSize) {
        this.normalMatrixSize = normalMatrixSize;
    }

    /**
     * Gets the size of the grid.
     *
     * @return The size of the grid.
     *         E.g. for a 9x9 grid (normal size) it returns 9.
     */
    public int getNormalSize() {
        return normalMatrixSize;
    }

    /**
     * Gets the amount of fields in the sudoku.
     *
     * @param type The type of the sudoku.
     * @return The amount of fields in the sudoku.
     */
    public int getFields(SudokuType type) {
        switch (type) {
            case NORMAL:
                return normalMatrixSize * normalMatrixSize;
            case SAMURAI:
                return normalMatrixSize * normalMatrixSize * 5
                        - ((int) Math.sqrt(normalMatrixSize) *  (int) Math.sqrt(normalMatrixSize) * 4);
            default:
                return -1;
        }
    }

}
