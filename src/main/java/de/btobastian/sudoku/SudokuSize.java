package de.btobastian.sudoku;

/**
 * Represents the size of a sudoku.
 */
public enum SudokuSize {

    /**
     * A tiny sudoku with a 4x4 grid size (2x2 blocks).
     */
    TINY(4),

    /**
     * A normal sudoku with a 9x9 grid size (3x3 blocks).
     */
    NORMAL(9),

    /**
     * A large sudoku with a 16x16 grid size (4x4 blocks).
     */
    LARGE(16),

    /**
     * A huge sudoku with a 25x25 grid size (5x5 blocks).
     */
    HUGE(25),

    /**
     * An extreme sudoku with a 36x36 grid size (6x6 blocks).
     */
    EXTREME(36),

    /**
     * An enormous sudoku with a 49x49 grid size (7x7 blocks).
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
