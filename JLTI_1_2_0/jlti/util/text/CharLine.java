package jlti.util.text;

public final class CharLine {
    public enum Type {
        I, II, III
    }

    public enum blockType {
        ALL, DOWN, UP, LIT
    }

    public static char LineH(Type type) {
        switch (type) {
            case I:
                return '─';
            case II:
                return '═';
            case III:
                return '━';
            default:
                return 'n';
        }
    }

    public static char LineV(Type type) {
        switch (type) {
            case I:
                return '│';
            case II:
                return '║';
            case III:
                return '┃';
            default:
                return 'n';
        } 
    }

    public static char LineYDR(Type type) {
        switch (type) {
            case I:
                return '┌';
            case II:
                return '╔';
            case III:
                return '┏';
            default:
                return 'n';
        }
    }

    public static char LineYDL(Type type) {
        switch (type) {
            case I:
                return '┐';
            case II:
                return '╗';
            case III:
                return '┓';
            default:
                return 'n';
        }
    }

    public static char LineYUR(Type type) {
        switch (type) {
            case I:
                return '└';
            case II:
                return '╚';
            case III:
                return '┗';
            default:
                return 'n';
        }
    }

    public static char LineYUL(Type type) {
        switch (type) {
            case I:
                return '┘';
            case II:
                return '╝';
            case III:
                return '┛';
            default:
                return 'n';
        }
    }

    public static char LineVR(Type type) {
        switch (type) {
            case I:
                return '├';
            case II:
                return '╠';
            case III:
                return '┣';
            default:
                return 'n';
        }
    }

    public static char LineVL(Type type) {
        switch (type) {
            case I:
                return '┤';
            case II:
                return '╣';
            case III:
                return '┫';
            default:
                return 'n';
        }
    }

    public static char LineHD(Type type) {
        switch (type) {
            case I:
                return '┬';
            case II:
                return '╦';
            case III:
                return '┳';
            default:
                return 'n';
        }
    }

    public static char LineHU(Type type) {
        switch (type) {
            case I:
                return '┴';
            case II:
                return '╩';
            case III:
                return '┻';
            default:
                return 'n';
        }
    }

    public static char LineA(Type type) {
        switch (type) {
            case I:
                return '┼';
            case II:
                return '╬';
            case III:
                return '╋';
            default:
                return 'n';
        }
    }

    public static char LineYDR2() {
        return '╭';
    }

    public static char LineYDL2() {
        return '╮';
    }

    public static char LineYUR2() {
        return '╰';
    }

    public static char LineYUL2() {
        return '╯';
    }

    public static char Block25p() {
        return '░';
    }

    public static char Block50p() {
        return '▒';
    }

    public static char Block100p() {
        return '▓';
    }

    public static char BlockPL(blockType type) {
        switch (type) {
            case ALL:
                return '█';
            case DOWN:
                return '▄';
            case UP:
                return '▀';
            case LIT:
                return '■';
            default:
                return 'n';
        }
    }

    public static char arrowUp() {
        return '▲';
    }

    public static char arrowDown() {
        return '▼';
    }

    public static char arrowLeft() {
        return '◄';
    }

    public static char arrowRight() {
        return '►';
    }

    public static char arrowLeft2() {
        return '◀';
    }

    public static char arrowRight2() {
        return '▶';
    }
}